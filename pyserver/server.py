#!/usr/bin/env python

import socket, sys, pdb

ESC = bytes([240])
MESS_ESC = bytes([241])
GET = bytes([242])
BOARDS = bytes([243])
THREADS = bytes([244])
POSTS = bytes([245])
SET = bytes([246])

class Post():
    
    def __init__(self, header, body):
        if isinstance(header, bytes):
            self.__header__ = header.decode('utf-8')
        else:
            self.__header__ = header
        if isinstance(body, bytes):
            self.__body__ = body.decode('utf-8')
        else:
            self.__body__ = body

    def get_header(self):
        return self.__header__

    def het_body(self):
        return self.__body__


class Thread():

    def __init__(self, init_post, bumplimit):
        self.__posts__ = [init_post]
        self.__limit__ = bumplimit

    def add_post(self, post):
        if len(self.__posts__) == self.__limit__:
            return false
        self.__posts__.append(post)

    def get_posts(self):
        return self.__posts__

    def get_header(self):
        return self.__posts__[0].get_header()

class Board():

    def __init__(self, name, bumplimit):
        self.__name__ = name
        self.__threads__ = {}
        self.__limit__ = bumplimit

    def get_name(self):
        return self.__name__

    def add_thread(self, init_post):
        new_thread = Thread(init_post, self.__limit__)
        new_id = len(self.__threads__)
        self.__threads__[new_id] = new_thread
        return new_id

    def delete_thread(self, thread_id):
        self.__threads__.pop(thread_id)

    def get_thread(self, thread_id):
        return self.__threads__.get(thread_id)

    def send_threads(self, sock):
        for key in self.__threads__:
            sock.send(bytes([key]))
            sock.send(self.__threads__[key].get_header().encode('utf-8'))
            print(self.__threads__[key].get_header().encode('utf-8'))
            sock.send(MESS_ESC)
        sock.send(ESC)

    def dumps(self):
        result = {}
        for key in self.__threads__:
            pass

class Server():

    def __init__(self):
        self.__boards__ = {}
        self.__exit__ = False

    def add_board(self, name, bumplimit):
        new_board = Board(name, bumplimit)
        new_id = len(self.__boards__)
        self.__boards__[new_id] = new_board
        return new_id

    def delete_board(self, board_id):
        self.__boards__.pop(board_id)

    def get_board(self, board_id):
        return self.__boards__.get(board_id)

    def connect_handle(self):
       listener = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
       listener.bind(('', 5005))
       listener.listen()
       while not self.__exit__:
           pdb.set_trace()
           (client, address) = listener.accept()
           self.handle_input(client, address)

    def handle_input(self, sock, address):
        buf = sock.recv(1)
        chain = b''
        while buf != ESC:
            chain += buf
            buf = sock.recv(1)
        print(chain)
        if chain[1] == THREADS[0]:
            board = self.__boards__[chain[2]]
            if chain[0] == SET[0]:
                [head, body] = chain[3:].split(MESS_ESC)
                initial = Post(head, body)
                board.add_thread(initial)
            if chain[0] == GET[0]:
                board.send_threads(sock)
        elif chain[1] == POSTS[0]:
            pass
        elif chain[1] == BOARDS[0]:
            pass
        else:
            print('Bad op')

def main():
    serv = Server()
    serv.add_board('Test1', 100)
    serv.add_board('Test2', 100)
    serv.add_board('Test3', 100)
    board = serv.get_board(0)
    board.add_thread(Post('Head1', 'Body1'))
    board.add_thread(Post('Head2', 'Body2'))
    board.add_thread(Post('Head3', 'Body3'))
    board.add_thread(Post('Head4', 'Body4'))
    serv.connect_handle()
    return 0

if (__name__ == "__main__"):
    sys.exit(main())

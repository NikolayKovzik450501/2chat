#!/usr/bin/env python

import socket

ESC = bytes([240])
MESS_ESC = bytes([241])

class Post():
    
    def __init__(self, header, body):
        self.__header__ = header
        self.__body__ = body

    def get_header(self):
        return self.header

    def het_body(self):
        return self.body


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
        return self.__posts__[0]

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
           (client, address) = listener.accept()
           self.handle_input(client, address)
    
    def handle_input(self, sock, address):
        buf = sock.recv(1)
        chain = b''
        while buf != ESC:
            chain += buf
            buf = sock.recv(1)
        for key in self.__boards__:
            name = self.__boards__[key].get_name().encode('utf-8')
            sock.send(bytes([key]) + name)
            sock.send(MESS_ESC)
        sock.send(ESC)


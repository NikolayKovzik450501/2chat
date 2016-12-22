#!/usr/bin/env python

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
        if len(self.__posts__) == limit:
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



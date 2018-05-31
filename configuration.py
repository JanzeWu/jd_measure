#!/usr/bin/env python
# coding=utf-8

import sys, os
from ConfigParser import ConfigParser

root_dir = os.path.dirname(os.path.abspath(__file__))
sys.path.append(root_dir)

CONF_FILE='conf/config.ini'

class Configuration(object):
    '''
    读取配置文件
    '''
    def __init__(self):
        self._properties = {}
        self._conf_path = os.path.join(root_dir, CONF_FILE)
        self._parser = ConfigParser()

    def __cache(self):
        '''
        读取配置文件
        '''
        self._parser.read(self._conf_path)
        for section in self._parser.sections():
            items = self._parser.items(section)
            for item in items:
                self._properties.setdefault(section, {})[item[0]]  = item[1]

    def get_props(self):
        if not self._properties:
            self.__cache()
        return self._properties

    def get_section_props(self, section):
        return self.get_props().get(section) 

    def get_prop(self, section, prop_key):
        sec = self.get_props().get(section)
        return sec.get(prop_key) if sec else None


if __name__=='__main__':
    print __name__
    c = Configuration()
    print c.get_props()


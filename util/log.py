#!/usr/bin/env python
# coding=utf-8
import os
import logging

root_dir = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))

def getLogger(name):
    logging.basicConfig(
                    filename= '/tmp/jd_measure.log', 
                    format = '%(asctime)-15s %(levelname)s %(filename)s  %(funcName)s %(lineno)d %(message)s',
                    level = logging.INFO)
    return logging.getLogger(name)

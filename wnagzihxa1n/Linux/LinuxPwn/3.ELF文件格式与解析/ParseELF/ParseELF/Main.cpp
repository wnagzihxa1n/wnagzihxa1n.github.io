#include <iostream>
#include <cstdio>
#include <Windows.h>
#include <list>
#include <vector>
#include <map>
#include <string>
#include "ELF.h"

using namespace std;

Elf32_Ehdr* e32_hdr = NULL;
vector <Elf32_Phdr> e32_phdr_VECTOR;
vector <Elf32_Shdr> e32_shdr_VECTOR;

//Parse ELF Header
bool ParseELFHeader(FILE* fp_elf)
{
	int32_t dwSizeofStructElf32_Ehdr = sizeof(Elf32_Ehdr);
	printf("The size of STRUCT Elf32_Ehdr is %d\n", dwSizeofStructElf32_Ehdr);
	e32_hdr = (Elf32_Ehdr*) malloc(dwSizeofStructElf32_Ehdr);
	if (e32_hdr == NULL)
	{
		perror("Malloc e32_hdr's memory failed");
		exit(1);
	}
	memset(e32_hdr, 0, dwSizeofStructElf32_Ehdr);
	fseek(fp_elf, 0, SEEK_SET);
	int32_t READ_COUNT = fread((uint8_t*) e32_hdr, sizeof(uint8_t), dwSizeofStructElf32_Ehdr, fp_elf);
    //cout << READ_COUNT << endl;
	
	LPSTR ELFFILETYPE[] = {"ET_NONE", "ET_REL", "ET_EXEC", "ET_DYN(Shared object file)", "ET_CORE"};
	
	string ELFTARGETMACHINE_Pre[23] = {"EM_NONE", "EM_M32", "EM_SPARC", "EM_386", "EM_68K", "EM_88K", 
		"EM_486", "EM_860", "EM_MIPS", "UNKNOWN", "EM_MIPS_RS4_BE", 
		"UNKNOWN", "UNKNOWN", "UNKNOWN", "UNKNOWN", "EM_PARISC", "UNKNOWN", 
		"UNKNOWN", "EM_SPARC32PLUS", "UNKNOWN", "EM_PPC", "EM_PPC64", "EM_S390"};
	map <int32_t, string> ELFTARGETMACHINE;
	for (int32_t i = 0; i < sizeof(ELFTARGETMACHINE_Pre) / sizeof(ELFTARGETMACHINE_Pre[0]); i++)
	{
		ELFTARGETMACHINE.insert(make_pair(0, ELFTARGETMACHINE_Pre[i]));
	}
	ELFTARGETMACHINE.insert(make_pair(40, "EM_ARM"));
	ELFTARGETMACHINE.insert(make_pair(42, "EM_SH"));
	ELFTARGETMACHINE.insert(make_pair(43, "EM_SPARCV9"));
	ELFTARGETMACHINE.insert(make_pair(47, "EM_H8_300H"));
	ELFTARGETMACHINE.insert(make_pair(48, "EM_H8S"));
	ELFTARGETMACHINE.insert(make_pair(50, "EM_IA_64"));
	ELFTARGETMACHINE.insert(make_pair(62, "EM_X86_64"));
	ELFTARGETMACHINE.insert(make_pair(76, "EM_CRIS"));
	ELFTARGETMACHINE.insert(make_pair(87, "EM_V850"));	
	
	cout << "####################ELF Header####################" << endl;
	cout << "ELF Header:" << endl;
	cout << "Magic                                 : " 
		<< e32_hdr->e_ident[EI_MAG0]
		<< e32_hdr->e_ident[EI_MAG1]
		<< e32_hdr->e_ident[EI_MAG2]
		<< e32_hdr->e_ident[EI_MAG3] << endl;
	printf("Class                                 : %s\n", ((int32_t) e32_hdr->e_ident[4] == 0) ? "Invalid" : ((int32_t) e32_hdr->e_ident[4] == 1) ? "ELF32" : "ELF64");
	printf("Data                                  : %s\n", ((int32_t) e32_hdr->e_ident[5] == 0) ? "Invalid" : ((int32_t) e32_hdr->e_ident[5] == 1) ? "little endian" : "big endian");
	printf("Version                               : %d (current)\n", (int32_t)e32_hdr->e_ident[6]);
	printf("OS / ABI                              : %d\n", e32_hdr->e_ident);
	printf("Type                                  : %s\n", (e32_hdr->e_type) < 5 ? ELFFILETYPE[e32_hdr->e_type] : (e32_hdr->e_type == 0xff00) ? "ET_LOPROC" : "ET_HIPROC");
	printf("Machine                               : %s\n", ELFTARGETMACHINE[e32_hdr->e_machine].c_str());
	printf("Version                               : 0x%x\n", e32_hdr->e_version);
	printf("Entry point address                   : 0x%x\n", e32_hdr->e_entry);
	printf("Start of program headers              : %d (bytes into file)\n", e32_hdr->e_phoff);
	printf("Start of section headers              : %d (bytes into file)\n", e32_hdr->e_shoff);
	printf("Flags                                 : %d\n", e32_hdr->e_flags);
	printf("Size of this header                   : %d (bytes)\n", e32_hdr->e_ehsize);
	printf("Size of program headers               : %d (bytes)\n", e32_hdr->e_phentsize);
	printf("Number of program headers             : %d\n", e32_hdr->e_phnum);
	printf("Size of section headers               : %d (bytes)\n", e32_hdr->e_shentsize);
	printf("Number of section headers             : %d\n", e32_hdr->e_shnum);
	printf("Section header string table index     : %d\n", e32_hdr->e_shstrndx);

	/*fseek(fp_elf, 0, seek_set);
	for (int8_t i = 0; i < dwsizeofstructelf32_ehdr; i++)
	{
		printf("%02x ", *((uint8_t*)e32_hdr + i));
		if (((i + 1) % 16 == 0) || (i == dwsizeofstructelf32_ehdr - 1))
		{
			printf("\n");
		}
	}*/
	cout << endl;
	return true;

	/*
	Magic:   7f 45 4c 46 01 01 01 00 00 00 00 00 00 00 00 00
	Class : ELF32
	Data : 2's complement, little endian
	Version : 1 (current)
	OS / ABI : UNIX - System V
	ABI Version : 0
	Type : DYN(Shared object file)
	Machine : ARM
	Version : 0x1
	Entry point address : 0x0
	Start of program headers : 52 (bytes into file)
	Start of section headers : 12660 (bytes into file)
	Flags : 0x5000200, Version5 EABI, soft - float ABI
	Size of this header : 52 (bytes)
	Size of program headers : 32 (bytes)
	Number of program headers : 9
	Size of section headers : 40 (bytes)
	Number of section headers : 26
	Section header string table index : 25
	*/
}

//Parse Program Headers Table
bool ParseProgramHeadersTable(FILE* fp_elf)
{
	fseek(fp_elf, e32_hdr->e_phoff, SEEK_SET);
	Elf32_Phdr* e32_phdr_temp = NULL;
	e32_phdr_temp = (Elf32_Phdr*) malloc(sizeof(Elf32_Phdr));
	e32_phdr_VECTOR.clear();
	for (int32_t i = 0; i < e32_hdr->e_phnum; i++)
	{
		memset(e32_phdr_temp, 0, sizeof(Elf32_Phdr));
		int32_t READ_COUNT = fread((uint8_t*) e32_phdr_temp, sizeof(uint8_t), e32_hdr->e_phentsize, fp_elf);
		e32_phdr_VECTOR.push_back((Elf32_Phdr) *e32_phdr_temp);
	}

	string ELFSEGMENTTYPE_Pre[7] = {"PT_NULL", "PT_LOAD", "PT_DYNAMIC", "PT_INTERP", "PT_NOTE", "PT_SHLIB", "PT_PHDR"};
	map <int32_t, string> ELFSEGMENTTYPE;
	for (int32_t i = 0; i < sizeof(ELFSEGMENTTYPE_Pre) / sizeof(ELFSEGMENTTYPE_Pre[0]); i++)
	{
		ELFSEGMENTTYPE.insert(make_pair(i, ELFSEGMENTTYPE_Pre[i]));
	}
	ELFSEGMENTTYPE.insert(make_pair(0x70000000, "PT_LOPROC"));
	ELFSEGMENTTYPE.insert(make_pair(0x7fffffff, "PT_HIPROC"));

	map <int, string> PROGRAMHEADERSFLAG;
	PROGRAMHEADERSFLAG.insert(make_pair(4, "R  "));
	PROGRAMHEADERSFLAG.insert(make_pair(2, " W"));
	PROGRAMHEADERSFLAG.insert(make_pair(1, "  E"));
	PROGRAMHEADERSFLAG.insert(make_pair(6, "RW "));
	PROGRAMHEADERSFLAG.insert(make_pair(5, "R E"));
	PROGRAMHEADERSFLAG.insert(make_pair(3, " WE"));
	PROGRAMHEADERSFLAG.insert(make_pair(7, "RWE"));

	cout << "####################ELF Program Headers Table####################" << endl;
	cout << "Program Headers:" << endl;
	cout << "Type            Offset       VirtAddr     PhysAddr     FileSiz      MemSiz       Flg   Align" << endl;
	for (int32_t i = 0; i < e32_phdr_VECTOR.size(); i++)
	{
		printf("%-16s", ELFSEGMENTTYPE[e32_phdr_VECTOR[i].p_type].c_str());
		printf("0x%p   ", e32_phdr_VECTOR[i].p_offset);
		printf("0x%p   ", e32_phdr_VECTOR[i].p_vaddr);
		printf("0x%p   ", e32_phdr_VECTOR[i].p_paddr);
		printf("0x%p   ", e32_phdr_VECTOR[i].p_filesz);
		printf("0x%p   ", e32_phdr_VECTOR[i].p_memsz);
		printf("%s   ", PROGRAMHEADERSFLAG[e32_phdr_VECTOR[i].p_flags].c_str());
		printf("0x%x\n", e32_phdr_VECTOR[i].p_align);
	}
	free(e32_phdr_temp);
	e32_phdr_temp = NULL;
	cout << endl;
	return true;
	/*
	Program Headers:
	Type           Offset   VirtAddr   PhysAddr   FileSiz MemSiz  Flg Align
	PHDR           0x000034 0x00000034 0x00000034 0x00120 0x00120 R   0x4
	INTERP         0x000154 0x00000154 0x00000154 0x00013 0x00013 R   0x1
	[Requesting program interpreter: /system/bin/linker]
	LOAD           0x000000 0x00000000 0x00000000 0x02450 0x02450 R E 0x1000
	LOAD           0x002e78 0x00003e78 0x00003e78 0x0018c 0x0018c RW  0x1000
	DYNAMIC        0x002e84 0x00003e84 0x00003e84 0x00138 0x00138 RW  0x4
	NOTE           0x000168 0x00000168 0x00000168 0x00024 0x00024 R   0x4
	GNU_STACK      0x000000 0x00000000 0x00000000 0x00000 0x00000 RW  0
	EXIDX          0x002338 0x00002338 0x00002338 0x00108 0x00108 R   0x4
	GNU_RELRO      0x002e78 0x00003e78 0x00003e78 0x00188 0x00188 RW  0x4

	Section to Segment mapping:
	Segment Sections...
	00
	01     .interp
	02     .interp .note.gnu.build-id .dynsym .dynstr .hash .gnu.version .gnu.version_d .gnu.version_r .rel.dyn .rel.plt .plt .text .ARM.extab .ARM.exidx .rodata
	03     .fini_array .init_array .dynamic .got .data
	04     .dynamic
	05     .note.gnu.build-id
	06
	07     .ARM.exidx
	08     .fini_array .init_array .dynamic .got
	*/
}

//Parse Section Headers Table
bool ParseSectionHeadersTable(FILE* fp_elf)
{
	fseek(fp_elf, e32_hdr->e_shoff, SEEK_SET);
	Elf32_Shdr* e32_shdr_temp = NULL;
	e32_shdr_temp = (Elf32_Shdr*) malloc(sizeof(Elf32_Shdr));
	e32_shdr_VECTOR.clear();
	for (int32_t i = 0; i < e32_hdr->e_shnum; i++)
	{
		memset(e32_shdr_temp, 0, sizeof(Elf32_Shdr));
		int32_t READ_COUNT = fread((uint8_t*) e32_shdr_temp, sizeof(uint8_t), e32_hdr->e_shentsize, fp_elf);
		e32_shdr_VECTOR.push_back((Elf32_Shdr) *e32_shdr_temp);
	}

	string ELFSEGMENTTYPE_Pre[13] = { "SHT_NULL", "SHT_PROGBITS", "SHT_SYMTAB", "SHT_STRTAB", "SHT_RELA", "SHT_HASH", "SHT_DYNAMIC", "SHT_NOTE", "SHT_NOBITS", "SHT_REL", "SHT_SHLIB", "SHT_DYNSYM", "SHT_NUM"};
	map <int32_t, string> ELFSEGMENTTYPE;
	for (int32_t i = 0; i < sizeof(ELFSEGMENTTYPE_Pre) / sizeof(ELFSEGMENTTYPE_Pre[0]); i++)
	{
		ELFSEGMENTTYPE.insert(make_pair(i, ELFSEGMENTTYPE_Pre[i]));
	}
	ELFSEGMENTTYPE.insert(make_pair(0x70000000, "SHT_LOPROC"));
	ELFSEGMENTTYPE.insert(make_pair(0x7fffffff, "SHT_HIPROC"));
	ELFSEGMENTTYPE.insert(make_pair(0x80000000, "SHT_LOUSER"));
	ELFSEGMENTTYPE.insert(make_pair(0xffffffff, "SHT_HIUSER"));

	cout << "####################ELF Section Headers Table####################" << endl;
	cout << "Section Headers:" << endl;
	cout << "[Nr] Name                     Type            Addr       Off        Size       ES Flg  Lk Inf Al" << endl;
	
	int32_t STRING_Offset = e32_shdr_VECTOR[e32_hdr->e_shstrndx].sh_offset;
	//printf("%d\n", STRING_Offset);

	char* lpStringBuffer = NULL;
	lpStringBuffer = (char*) malloc(1024);
	for (int32_t i = 0; i < e32_hdr->e_shnum; i++)
	{
		printf("[%2d] ", i);
		memset(lpStringBuffer, 0, strlen(lpStringBuffer));
		int index = 0;
		fseek(fp_elf, 0, SEEK_SET);
		fseek(fp_elf, STRING_Offset + e32_shdr_VECTOR[i].sh_name, SEEK_SET);
		while (fread(lpStringBuffer + index, 1, 1, fp_elf))
		{
			if (*(lpStringBuffer + index++) == '\0')
			{
				break;
			}
		}
		printf("%-24s ", lpStringBuffer);
		printf("%-15s ", ELFSEGMENTTYPE[e32_shdr_VECTOR[i].sh_type].c_str());
		printf("0x%p ", e32_shdr_VECTOR[i].sh_addr);
		printf("0x%p ", e32_shdr_VECTOR[i].sh_offset);
		printf("0x%p ", e32_shdr_VECTOR[i].sh_size);
		printf("%02x ", e32_shdr_VECTOR[i].sh_entsize);
		printf("%03d  ", e32_shdr_VECTOR[i].sh_flags);
		printf("%2d ", e32_shdr_VECTOR[i].sh_link);
		printf("%2d  ", e32_shdr_VECTOR[i].sh_info);
		printf("%02x\n", e32_shdr_VECTOR[i].sh_addralign);
	}
	free(lpStringBuffer);
	free(e32_shdr_temp);
	lpStringBuffer = NULL;
	e32_shdr_temp = NULL;
	cout << endl;
	return true;
	/*
	Section Headers:
	[Nr] Name              Type            Addr     Off    Size   ES Flg Lk Inf Al
	[ 0]                   NULL            00000000 000000 000000 00      0   0  0
	[ 1] .interp           PROGBITS        00000154 000154 000013 00   A  0   0  1
	[ 2] .note.gnu.build-i NOTE            00000168 000168 000024 00   A  0   0  4
	[ 3] .dynsym           DYNSYM          0000018c 00018c 000380 10   A  4   1  4
	[ 4] .dynstr           STRTAB          0000050c 00050c 00053e 00   A  0   0  1
	[ 5] .hash             HASH            00000a4c 000a4c 00017c 04   A  3   0  4
	[ 6] .gnu.version      VERSYM          00000bc8 000bc8 000070 02   A  3   0  2
	[ 7] .gnu.version_d    VERDEF          00000c38 000c38 00001c 00   A  4   1  4
	[ 8] .gnu.version_r    VERNEED         00000c54 000c54 000030 00   A  4   1  4
	[ 9] .rel.dyn          REL             00000c84 000c84 000040 08   A  3   0  4
	[10] .rel.plt          REL             00000cc4 000cc4 000038 08  AI  3  11  4
	[11] .plt              PROGBITS        00000cfc 000cfc 000068 00  AX  0   0  4
	[12] .text             PROGBITS        00000d64 000d64 00158c 00  AX  0   0  4
	[13] .ARM.extab        PROGBITS        000022f0 0022f0 000048 00   A  0   0  4
	[14] .ARM.exidx        ARM_EXIDX       00002338 002338 000108 08  AL 12   0  4
	[15] .rodata           PROGBITS        00002440 002440 000010 01 AMS  0   0  1
	[16] .fini_array       FINI_ARRAY      00003e78 002e78 000008 00  WA  0   0  4
	[17] .init_array       INIT_ARRAY      00003e80 002e80 000004 00  WA  0   0  1
	[18] .dynamic          DYNAMIC         00003e84 002e84 000138 08  WA  4   0  4
	[19] .got              PROGBITS        00003fbc 002fbc 000044 00  WA  0   0  4
	[20] .data             PROGBITS        00004000 003000 000004 00  WA  0   0  4
	[21] .bss              NOBITS          00004004 003004 000000 00  WA  0   0  1
	[22] .comment          PROGBITS        00000000 003004 000028 01  MS  0   0  1
	[23] .note.gnu.gold-ve NOTE            00000000 00302c 00001c 00      0   0  4
	[24] .ARM.attributes   ARM_ATTRIBUTES  00000000 003048 00002b 00      0   0  1
	[25] .shstrtab         STRTAB          00000000 003073 0000fe 00      0   0  1
	Key to Flags:
	W (write), A (alloc), X (execute), M (merge), S (strings)
	I (info), L (link order), G (group), T (TLS), E (exclude), x (unknown)
	O (extra OS processing required) o (OS specific), p (processor specific)
	*/
}

bool ParseELF(FILE* fp_elf)
{
	ParseELFHeader(fp_elf);
	ParseProgramHeadersTable(fp_elf);
	ParseSectionHeadersTable(fp_elf);
	return true;
}

bool FreeAllStructPoint()
{
	free(e32_hdr);
	e32_hdr = NULL;
	vector<Elf32_Phdr>(e32_phdr_VECTOR).swap(e32_phdr_VECTOR);
	return true;
}

int main()
{
	LPSTR lpELFPath = "libtotoc.so";
	FILE* fp_elf;
	
	//Open libtotoc.so and get the file point (FILE*)fp_elf
	errno_t err;
	if ((err = fopen_s(&fp_elf, lpELFPath, "rb")) != 0)
	{
		perror("Open ELF:lbtotoc.so failed");
		free(fp_elf);
		exit(1);
	}
	else
	{
		printf("Open ELF:%s successfully\n", lpELFPath);
	}

	//Get the size of libtotoc.so
	fseek(fp_elf, 0, SEEK_END);
	int32_t dwSizeofELF = ftell(fp_elf);
	printf("The size of ELF:%s is:%p\n", lpELFPath, dwSizeofELF);

	//Start parse the ELF file libtotoc.so
	if (!ParseELF(fp_elf))
	{
		perror("Parse ELF failed");
	}
	else
	{
		printf("Parse ELF:%s successfully\n", lpELFPath);
	}
	
	//Free
	if (!FreeAllStructPoint())
	{
		perror("Free failed");
		exit(1);
	}
	else
	{
		printf("Free successfully\n");
	}
	int32_t Close_STREAM = _fcloseall();
	//printf("%d\n", Close_STREAM);
	return 0;
}


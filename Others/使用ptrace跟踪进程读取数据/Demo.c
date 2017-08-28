#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/mman.h>
#include <fcntl.h>
#include <string.h>
#include <sys/ptrace.h>

int main(int argc, char **argv)
{
	int32_t pid = 68827;
    char buf[200];
    bzero(buf, 200);
    sprintf(buf, "/proc/%d/mem", pid);
    ptrace(PTRACE_ATTACH, pid, 0, 0);//use ptrace to attach target process
    int fd = open(buf, O_RDONLY);
    if(fd == -1)
		printf("open error : %m\n");

    /////////////////////////////////////////////////////////////
    int var1 = 0;
    off_t r = lseek64(fd, 0x7ffc8e8c7d4c, SEEK_SET);//must using lseek64 else would be wrong
    if(r == -1)
		printf("lseek error : %m\n");
    printf("Address : %p\n", r);
    ssize_t size = read(fd, &var1, sizeof(int));
    if(size == -1)
		printf("read error : %m\n");
    printf("var1 : %d\n", var1);

    /////////////////////////////////////////////////////////////
    char target_string[12];
    bzero(target_string, 12);
    r = lseek64(fd, 0x400664, SEEK_SET);//must using lseek64 else would be wrong
    if(r == -1)
		printf("lseek error : %m\n");
    printf("Address : %p\n", r);
    size = read(fd, target_string, sizeof(char) * 12);
    if(size == -1)
		printf("read error : %m\n");
    printf("target_string : %s\n", target_string);
    close(fd);
    return 0;
}












#include<stdio.h>
#include<unistd.h>
#include<sys/mman.h>
#include<stdlib.h>

int main(int argc, char **argv)
{
	int var1 = 1111;
	char *target_string = "You got it!";
    printf("Process ID : %d\n", getpid());
    printf("var1 : %p\n", &var1);
    printf("target_string : %p\n", target_string);
    while(1);
    return 0;
}

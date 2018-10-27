#include <stdio.h>
#include <string.h>

int main()
{
	char buffer[10];
	printf("I am Str one\n");
	memset(buffer, 0, strlen(buffer));
	printf("I am Str two\n");
	return 0;
}
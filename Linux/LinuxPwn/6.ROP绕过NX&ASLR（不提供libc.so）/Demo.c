#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

void Overflow()
{
    char buffer[128];
    read(STDIN_FILENO, buffer, 256);
}

int main()
{
    Overflow();
    write(STDOUT_FILENO, "Hello,World\n", 13);
    return 0;
}
#include <stdio.h>
#include <stdlib.h>

#define __PAIR__(high, low) (((unsigned long long)(high) << sizeof(high)*8) | low)

int main()
{
	int pid = 0;
	unsigned long long result = (__PAIR__(pid, pid) - __PAIR__(pid - 1, 1)) >> 32;
	printf("__PAIR__(pid, pid) = %lld\n", __PAIR__(pid, pid));
	printf("__PAIR__(pid - 1, 1) = %lld\n", __PAIR__(pid - 1, 1));
	printf("%lld\n", result);
    return 0;
}

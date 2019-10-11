#include <iostream>
#include <cstdio>
using namespace std;

int main()
{
	int index = 1;
	int p, e, k, d;
	int i;
	int a = 28 * 33, b = 23 * 33, c = 23 * 28;
	for (i = 1; ; i++) if ((a * i % 23) == 1) break; a *= i;
	for (i = 1; ; i++) if ((b * i % 28) == 1) break; b *= i;
	for (i = 1; ; i++) if ((c * i % 33) == 1) break; c *= i;
	while (scanf("%d %d %d %d", &p, &e, &k, &d))
	{
		int result;
		if (p == -1 && e== -1&& k == -1 && d == -1)
			break;
		result = (p * a + e * b + k * c - d) % (23 * 28 * 33);
		result = (result + 23 * 28 * 33 - 1) % (23 * 28 * 33) + 1;
		printf("Case %d: the next triple peak occurs in %d days.\n", index++, result);
	}
	return 0;
}

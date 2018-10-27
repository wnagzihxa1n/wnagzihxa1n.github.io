#include <iostream>
#include <cstdio>
using namespace std;

int main()
{
	double sum = 0, money;
	for (int i = 0; i < 12; i++)
	{
		scanf("%lf", &money);
		sum += money;
	}
	printf("$%0.2f\n", sum / 12.0);
	return 0;
}

#include <iostream>
#include <cstdio>
#include <cmath>
using namespace std;

int main()
{
	int t, index = 1;
	double x, y;
	double a[10000], r;
	for (int i = 1; i <= 10000; i++)
	{
		r = sqrt(100.0 * i / 3.1415926);
		a[i] = r;
	}
	scanf("%d", &t);
	while (t--)
	{
		scanf("%lf %lf", &x, &y);
		for (int i = 1; i <= 10000; i++)
		{
			if (sqrt(x * x + y * y) < a[i])
			{
				printf("Property %d: This property will begin eroding in year %d.\n", index++, i);
				break;
			}
		}
	}
	printf("END OF OUTPUT.\n");
	return 0;
}

#include <iostream>
#include <cstdio>
using namespace std;

int main()
{
	double n;
	while (~scanf("%lf", &n))
	{
		if (n == 0.00)
		{
			break;
		}
		double sum = 0.0, index = 2.0;
		while (1)
		{
			sum += 1/index;
			if (sum >= n) {
				break;
			}
			index++;
		}
		printf("%0.0f card(s)\n", index - 1);
	}
}

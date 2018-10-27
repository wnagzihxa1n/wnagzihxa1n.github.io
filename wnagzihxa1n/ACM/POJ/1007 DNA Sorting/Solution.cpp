#include <iostream>
#include <cstdio>
#include <stdlib.h>
#include <algorithm>
#include <cstring>
using namespace std;

struct DNASQUE {
    char sque[60];
    int sorted;
} dnasque[128];

int mycmp(DNASQUE dna1, DNASQUE dna2) {
    return dna1.sorted < dna2.sorted;
}

int main() {
    freopen("in.txt", "r", stdin);
    int n, m;
    while (~scanf("%d %d", &n, &m)) {
        getchar();
        for (int i = 0; i < m; i++) {
            gets(dnasque[i].sque);
        }
//		for (int i = 0; i < m; i++)
//		{
//			printf("%d => %s\n", i, dnasque[i].sque);
//		}
        for (int i = 0; i < m; i++) {
            dnasque[i].sorted = 0;
            for (int j = 0; j < n; j++) {
                for (int k = j + 1; k < n; k++) {
                    if (dnasque[i].sque[j] > dnasque[i].sque[k]) {
                        dnasque[i].sorted++;
                    }
                }
            }
        }

        sort(dnasque, dnasque + m, mycmp);
        for (int i = 0; i < m; i++) {
            printf("%s\n", dnasque[i].sque);
        }
    }
    return 0;
}

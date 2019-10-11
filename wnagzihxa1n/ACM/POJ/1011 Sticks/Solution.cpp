#include <iostream>
#include <cstdio>
#include <cstring>
#include <cmath>
#include <queue>
#include <vector>
#include <map>
#include <string>
#include <stack>
#include <stdlib.h>
#include <algorithm>
using namespace std;

int n;
bool flag = false;
int sticks[128], visit[128], stickLength;

int myCmp(int a, int b) {
    return a > b;
}

void dfs(int currentStickIndex, int usedStickNum, int lengthTotal) {
    if (flag) return;
    if (lengthTotal == 0) {
        int i = 0;
        while (visit[i] == 1) i++;
        visit[i] = 1;
        dfs(i + 1, usedStickNum + 1, sticks[i]);
        visit[i] = 0;
        return;
    }
    if (lengthTotal == stickLength) {
        if (usedStickNum == n) {
            flag = true;
        } else {
            dfs(0, usedStickNum, 0);
        }
        return;
    }
    for (int i = currentStickIndex; i < n; i++) {
        if (visit[i] == 0 && (lengthTotal + sticks[i]) <= stickLength) {
            if (visit[i - 1] == 0 && sticks[i - 1] == sticks[i]) continue;
            visit[i] = 1;
            dfs(i + 1, usedStickNum + 1, lengthTotal + sticks[i]);
            visit[i] = 0;
        }
    }
}

int main() {
    freopen("in.txt", "r", stdin);
    while (~scanf("%d", &n)) {
        flag = false;
        if (n == 0) break;
        memset(sticks, 0, sizeof(sticks) / 4);
        memset(visit, 0, sizeof(visit) / 4);
        int sum = 0;
        for (int i = 0; i < n; i++) {
            scanf("%d", sticks + i);
            sum += sticks[i];
        }
        sort(sticks, sticks + n, myCmp);
        for (stickLength = sticks[0]; stickLength <= sum; stickLength++) {
            if (sum % stickLength == 0) {
                dfs(0, 0, 0);
                if (flag) {
                    break;
                }
            }
        }
        printf("%d\n", stickLength);
    }
    return 0;
}

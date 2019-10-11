#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char totalPhoneNum[100000][9];
int index = 0;

void printResult() {
	int i, j = 1;
    int sum = 1;
    for (i = 0; i < index; i++) {
        if (strcmp(totalPhoneNum[i], totalPhoneNum[i + 1]) == 0) {
            sum++;
        } else {
        	if (sum != 1) {
        		j = 0;
        		printf("%s %d\n", totalPhoneNum[i], sum);
        	}
            sum = 1;
        }
    }
    index = 0;
    if (j) {
		printf("No duplicates.\n");
    }
}

int searchChar(char Str[], char pattern) {
	int i;
    for (i = 0; i < strlen(Str); i++) {
        if (pattern == Str[i]) {
            return 1;
        }
    }
    return 0;
}

char getMap(char Key) {
    if (searchChar("ABC", Key)) {
        return '2';
    } else if (searchChar("DEF", Key)) {
        return '3';
    } else if (searchChar("GHI", Key)) {
        return '4';
    } else if (searchChar("JKL", Key)) {
        return '5';
    } else if (searchChar("MNO", Key)) {
        return '6';
    } else if (searchChar("PRS", Key)) {
        return '7';
    } else if (searchChar("TUV", Key)) {
        return '8';
    } else if (searchChar("WXY", Key)) {
        return '9';
    } else {
        return '0';
    }
}

void foramt(char s[], int len) {
    int i, j = 0;
    char temp[9];
    for (i = 0; i < len; i++) {
        if (j == 3) {
            temp[j] = '-';
            j++;
        }
        if (s[i] != '-') {
            temp[j] = s[i];
            j++;
        }
    }
    temp[j] = '\0';
    for(i = 0; i < 8; i++) {
        if (searchChar("ABCDEFGHIJKLMNOPQRSTUVWXYZ", temp[i])) {
            temp[i] = getMap(temp[i]);
        }
    }
    for (i = 0; i < 8; i++) {
		totalPhoneNum[index][i] = temp[i];
	}
	totalPhoneNum[index++][8] = '\0';
}

int main() {
	int i, j;
//    freopen("in.txt", "r", stdin);
    int t, m, n;
    char s[1024];
    while (~scanf("%d", &t)) {
        getchar();
        for (i = 0; i < t; i++) {
            memset(s, 0, strlen(s));
            gets(s);
            foramt(s, strlen(s));
        }
        qsort(totalPhoneNum, t, 9, strcmp);
        printResult();
    }
    return 0;
}




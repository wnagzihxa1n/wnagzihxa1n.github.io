#include <stdio.h>
#include <string.h>
#include <pthread.h>
#include <unistd.h>

int param_test_race_condition = 0;

void* threadA() {
	param_test_race_condition = 1;
	sleep(1);
	if (param_test_race_condition == 1) {
		printf("It's ok!\n");
	}
	else if (param_test_race_condition == 2) {
		printf("Wow, there is a race condition!\n");
	}
}

void* threadB() {
	sleep(0.5);
	param_test_race_condition = 2;
}

int main(int argc, char const *argv[])
{
	pthread_t threada, threadb;
	pthread_create(&threada, NULL, (void *)threadA, NULL);
	pthread_create(&threadb, NULL, (void *)threadB, NULL);
	pthread_join(threada, NULL);
	pthread_join(threadb, NULL);
	return 0;
}


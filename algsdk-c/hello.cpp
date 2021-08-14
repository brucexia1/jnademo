#include "hello.h"

FUNC_API void getBool(bool x) {
	// 255
	printf("bool: %d", x);
	if (x) {
		printf(" in true\n");
	}else {
		printf(" in false\n");
	}
}

FUNC_API int max(int num1, int num2) {
	return num1 > num2 ? num1 : num2;
}


FUNC_API void testArray(uint16_t* vals, int len) {
	for (int j = 0; j < len; j++) {
		printf("vals[%d]: %d \n", j, vals[j]);
	}
}


FUNC_API void testStruct(ArrInfo arrInfo) {
	for (int j = 0; j < arrInfo.len; j++) {
		printf("arrInfo[%d]: %d \n", j, arrInfo.vals[j]);
	}
}


FUNC_API void printUser(User user) {
	printf("printUser user: %s height: %d weight: %.2f \n", user.name, user.height, user.weight);
}
FUNC_API void printUserRef(User& user) {
	printf("printUserRef user: %s height: %d weight: %.2f \n", user.name, user.height, user.weight);
}





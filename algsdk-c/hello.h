#ifndef __HELLO_H__
#define __HELLO_H__

#include <stdio.h>
#include <stdint.h>

#define FUNC_API  extern "C" __declspec(dllexport)


struct User
{
	char* name;
	int height;
	double weight;
};


struct ArrInfo
{
	uint16_t* vals;
	int len;
};


FUNC_API void printUser(User user);

FUNC_API void printUserRef(User& user);

FUNC_API int max(int num1, int num2);

FUNC_API void testArray(uint16_t* vals, int len);

FUNC_API void testStruct(ArrInfo arrInfo);

FUNC_API void getBool(bool x);


#endif
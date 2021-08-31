#include <stdio.h>

#include "hiknetsdk.h"

#define NET_DVR_TRAFFIC_DATA_QUERY_RESULT_STATUS 2
#define NET_SDK_CALLBACK_FINISH_STATUS  1000

NET_DVR_API LONG __stdcall NET_DVR_StartRemoteConfig(LONG lUserID, DWORD dwCommand, LPVOID lpInBuffer, DWORD dwInBufferLen, fRemoteConfigCallback cbStateCallback, LPVOID pUserData)
{
    printf("lUserID[%ld] dwCommand[%d] lpInBuffer[%p] dwInBufferLen[%d] cbStateCallback[%p] pUserData[%p]\n", lUserID, dwCommand, lpInBuffer, dwInBufferLen, cbStateCallback, pUserData);

    if (NET_DVR_GET_TRAFFIC_DATA == dwCommand) {
        if (dwInBufferLen != sizeof(NET_DVR_TRAFFIC_DATA_QUERY_COND)) {
            printf("sizeof(NET_DVR_TRAFFIC_DATA_QUERY_COND) %ld != inbuflen %ld", 
                sizeof(NET_DVR_TRAFFIC_DATA_QUERY_COND), dwInBufferLen);
            return -1;
        }
        NET_DVR_TRAFFIC_DATA_QUERY_COND* pCond = (NET_DVR_TRAFFIC_DATA_QUERY_COND*)lpInBuffer;
        printf("dwChannel %d \n", pCond->dwChannel);
        printf("dwPlateType %d \n", pCond->dwPlateType);
        printf("dwPlateColor %d \n", pCond->dwPlateColor);
        printf("dwVehicleType %d \n", pCond->dwVehicleType);
        printf("dwQueryCond %d \n", pCond->dwQueryCond);
        printf("dwSize %d \n", pCond->dwSize);

        NET_DVR_TRAFFIC_DATA_QUERY_RESULT slt = { {0} };
        char license[] = "ÍîA123456";
        slt.dwSize = sizeof(NET_DVR_TRAFFIC_DATA_QUERY_RESULT);
        strncpy_s(slt.sLicense, license, min(MAX_LICENSE_LEN, sizeof(license)));
        cbStateCallback(NET_DVR_TRAFFIC_DATA_QUERY_RESULT_STATUS, &slt, sizeof(NET_DVR_TRAFFIC_DATA_QUERY_RESULT), NULL);
        slt.dwSize = NET_SDK_CALLBACK_FINISH_STATUS;
        cbStateCallback(NET_SDK_CALLBACK_TYPE_STATUS, &slt, 0, NULL);

        NET_DVR_TIME_V30 sat = pCond->struEndTime;
        NET_DVR_TIME_V30 ent = pCond->struEndTime;
        printf("start time : %04d-%02d-%02d %02d:%02d:%02d \n", sat.wYear, sat.byMonth, sat.byDay, sat.byHour, sat.byMinute, sat.bySecond);
        printf("end time : %04d-%02d-%02d %02d:%02d:%02d \n", ent.wYear, ent.byMonth, ent.byDay, ent.byHour, ent.byMinute, ent.bySecond);
    }

    return 0;
}

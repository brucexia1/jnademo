#include <stdio.h>

#include "hiknetsdk.h"


NET_DVR_API LONG __stdcall NET_DVR_StartRemoteConfig(LONG lUserID, DWORD dwCommand, LPVOID lpInBuffer, DWORD dwInBufferLen, fRemoteConfigCallback cbStateCallback, LPVOID pUserData)
{
    printf("lUserID[%ld] dwCommand[%d] lpInBuffer[%p] dwInBufferLen[%d] cbStateCallback[%p] pUserData[%p]\n", lUserID, dwCommand, lpInBuffer, dwInBufferLen, cbStateCallback, pUserData);

    if (NET_DVR_GET_TRAFFIC_DATA == dwCommand) {
        NET_DVR_TRAFFIC_DATA_QUERY_COND* pCond = (NET_DVR_TRAFFIC_DATA_QUERY_COND*)lpInBuffer;
        printf("dwChannel %d \n", pCond->dwChannel);
        printf("dwPlateType %d \n", pCond->dwPlateType);
        printf("dwPlateColor %d \n", pCond->dwPlateColor);
        printf("dwVehicleType %d \n", pCond->dwVehicleType);
        printf("dwQueryCond %d \n", pCond->dwQueryCond);

        NET_DVR_TIME_V30 sat = pCond->struEndTime;
        NET_DVR_TIME_V30 ent = pCond->struEndTime;
        printf("start time : %04d-%02d-%02d %02d:%02d:%02d \n", sat.wYear, sat.byMonth, sat.byDay, sat.byHour, sat.byMinute, sat.bySecond);
        printf("end time : %04d-%02d-%02d %02d:%02d:%02d \n", ent.wYear, ent.byMonth, ent.byDay, ent.byHour, ent.byMinute, ent.bySecond);
    }

    return 0;
}


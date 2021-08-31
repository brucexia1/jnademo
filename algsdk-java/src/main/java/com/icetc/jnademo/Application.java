package com.icetc.jnademo;

import com.sun.jna.*;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Bruce Xia
 * @date 2021/08/1325
 */
@Slf4j
public class Application {
    private static AlgSDK algSDK = AlgSDK.INSTANCE;

    public static void main(String[] args) {
        testBool();
        testMax();
        testArray();
        testUser();
        testPointer();
        DVR_StartRemoteConfig();
    }

    private static  int NET_DVR_GET_TRAFFIC_DATA = 3141;
    public static int NET_DVR_TRAFFIC_DATA_QUERY_RESULT_STATUS = 2;
    public static int  NET_SDK_CALLBACK_TYPE_STATUS = 0;
    public static int  NET_SDK_CALLBACK_FINISH_STATUS = 1000;
    public static void DVR_StartRemoteConfig() {
        int userId = -1;

        Date startTime = new Date();
        Date endTime = new Date();
        AlgSDK.NET_DVR_TRAFFIC_DATA_QUERY_COND struAcsEventCond = new AlgSDK.NET_DVR_TRAFFIC_DATA_QUERY_COND();
        struAcsEventCond.read();
        struAcsEventCond.dwSize = struAcsEventCond.size();
        struAcsEventCond.dwChannel = 1;
        struAcsEventCond.struStartTime = getHkTime_V30(startTime);
        struAcsEventCond.struEndTime = getHkTime_V30(endTime);
        struAcsEventCond.dwPlateType = (1 << 0) +(1 << 1) + (1 << 2) + (1 << 3) + (1 << 5) + (1 << 7);
        struAcsEventCond.dwPlateColor = (1 << 0);
        struAcsEventCond.dwVehicleType = (0 << 1) + (1 << 1) + (1 << 2) + (1 << 3) + (1 << 5) + (1 << 6) + (1 << 7) + (1 << 8) + (1 << 9) + (1 << 10);
        struAcsEventCond.dwQueryCond = (1 << 0) + (1 << 1) + (1 << 3) + (1 << 4) + (1 << 6);
        struAcsEventCond.write();
        Pointer ptrStruEventCond = struAcsEventCond.getPointer();

        int loadHandle = algSDK.NET_DVR_StartRemoteConfig(userId, NET_DVR_GET_TRAFFIC_DATA, ptrStruEventCond, struAcsEventCond.size(), new GetTrafficDataCallBack(), null);
    }

    /**
     * 获取交通数据回调
     */
    private static class GetTrafficDataCallBack implements AlgSDK.FRemoteConfigCallback {
        @Override
        public void invoke(int dwType, AlgSDK.NET_DVR_TRAFFIC_DATA_QUERY_RESULT lpBuffer, int dwBufLen, Pointer pUserData) {
            System.out.println("111");
            if (dwType == NET_DVR_TRAFFIC_DATA_QUERY_RESULT_STATUS) {
                System.out.println("222");
                if (dwBufLen < lpBuffer.size()) { return; }
                System.out.println("333");
                AlgSDK.NET_DVR_TIME_V30 time =  lpBuffer.struTrafficPic[0].struAbsTime;
                System.out.println("车牌号-" + new String(lpBuffer.sLicense, Charset.forName("GBK")).replaceAll("\\u0000","")
                        + " |通道号-" + intToBit(lpBuffer.dwChannel) + " |车牌颜色-"
                        + dwPlateColorStr(lpBuffer.dwPlateColor) + " |车道号-" + lpBuffer.byLaneNo
                        + " |抓拍时间-" + String.format("%02d", time.wYear) + "-" + String.format("%02d", time.byMonth) + "-" + String.format("%02d", time.byDay) + " "
                        + String.format("%02d", time.byHour)+":"+String.format("%02d", time.byMinute)+":"+String.format("%02d", time.bySecond)
                        + " |图片名称-" + (lpBuffer.struTrafficPic.length>0?byteToString(lpBuffer.struTrafficPic[0].szPicName):"无"));
            } else if (dwType == NET_SDK_CALLBACK_TYPE_STATUS && lpBuffer.dwSize == NET_SDK_CALLBACK_FINISH_STATUS) {
                System.out.println("获取交通数据完成");
            }
        }
    }

    /**
     * byte数组转String
     * @param bytes
     * @return
     */
    private static String byteToString(byte[] bytes) {
        if (null == bytes || bytes.length == 0) {
            return "";
        }
        String strContent = "";
        try {
            strContent = new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strContent;
    }

    private static byte intToByte(int x) {
        return (byte) x;
    }

    /**
     * Byte转Bit
     */
    private static String byteToBit(byte b) {
        return "" + (byte) ((b >> 0) & 0x1) +
                (byte) ((b >> 1) & 0x1) +
                (byte) ((b >> 2) & 0x1) +
                (byte) ((b >> 3) & 0x1) +
                (byte) ((b >> 4) & 0x1) +
                (byte) ((b >> 5) & 0x1) +
                (byte) ((b >> 6) & 0x1) +
                (byte) ((b >> 7) & 0x1);
    }

    /**
     * int转bit
     * @param x
     * @return
     */
    private static int intToBit(int x) {
        return byteToBit(intToByte(x)).indexOf("1")+1;
    }
    /* Bit0-未知（其他）
    Bit1-黄色
    Bit2-白色
    Bit3-黑色
    Bit4-绿色
    Bit5-蓝色*/
    public static String dwPlateColorStr(int dwPlateColor){
        List<String> l = new ArrayList<>();
        l.add("未知（其他）");
        l.add("黄色");
        l.add("白色");
        l.add("黑色");
        l.add("绿色");
        l.add("蓝色");
        try {
            return l.get(intToBit(dwPlateColor)-1);
        } catch (Exception e) {
            return l.get(0);
        }
    }

    public static void testBool() {
        // c++ output:
        // bool: 255 in true
        algSDK.getBool(true);
    }

    public static void testMax() {
        int max = algSDK.max(100, 200);
        // out: 200
        System.out.println(max);
    }

    public static void testArray() {
        algSDK.testArray(new short[]{1, 2, 3, 4}, 4);
    }

    public static void testUser() {
        AlgSDK.User.UserValue user1 = new AlgSDK.User.UserValue("user1", 186, 65.2);
        algSDK.printUserRef(user1);
        algSDK.printUser(user1);
    }

    public static void testPointer() {
        // java main test
        int len = 3;
        int shortSize = Native.getNativeSize(Short.class);
        Pointer pointer = new Memory(len * shortSize);
        for (int i = 0; i < len; i++) {
            pointer.setShort(shortSize * i, (short) i);
        }
        AlgSDK.ArrInfo arrInfo = new AlgSDK.ArrInfo(pointer, len);
        algSDK.testStruct(arrInfo);
    }

    /**
     * 获取海康录像机格式的时间
     * @param time
     * @return
     */
    private static AlgSDK.NET_DVR_TIME_V30 getHkTime_V30(Date time) {
        AlgSDK.NET_DVR_TIME_V30 structTime = new AlgSDK.NET_DVR_TIME_V30();
        String str = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS").format(time);
        String[] times = str.split("-");
        structTime.wYear = Short.parseShort(times[0]);
        structTime.byMonth = new Byte(times[1]);
        structTime.byDay = new Byte(times[2]);;
        structTime.byHour = new Byte(times[3]);
        structTime.byMinute =new Byte(times[4]);
        structTime.bySecond = new Byte(times[5]);
        return structTime;
    }
}

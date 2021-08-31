package com.icetc.jnademo;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.win32.StdCallLibrary;
import lombok.Data;

public interface AlgSDK extends StdCallLibrary {
    // JNA 为 dll 名称
    AlgSDK INSTANCE = Native.load("AlgSDK", AlgSDK.class);

    int max(int a, int b);
    void getBool(boolean x);
    void testArray(short[] vals, int len);
    void testStruct(ArrInfo arrInfo);
    void printUser(User.ByValue user);
    void printUserRef(User user);

    @Data
    @Structure.FieldOrder({"name", "height", "weight"})
    public static class User extends Structure {
        public static class UserValue extends User implements Structure.ByValue {
            public UserValue(String name, int height, double weight) {
                super(name, height, weight);
            }
        }

        public User(String name, int height, double weight) {
            this.name = name;
            this.height = height;
            this.weight = weight;
        }

        public String name;
        public int height;
        public double weight;
    }

    @Data
    @Structure.FieldOrder({"vals", "len"})
    public static class ArrInfo extends Structure {
        public Pointer vals;
        public int len;

        public ArrInfo(Pointer vals, int len) {
            this.vals = vals;
            this.len = len;
        }
    }

    @Structure.FieldOrder({"wYear","byMonth","byDay","byHour","byMinute","bySecond","byRes","wMilliSec","byRes1"})
    public static class NET_DVR_TIME_V30 extends Structure
    {
        public short wYear;
        public byte byMonth;
        public byte byDay;
        public byte byHour;
        public byte byMinute;
        public byte bySecond;
        public byte byRes;
        public short wMilliSec;
        public byte[] byRes1 = new byte[2];
    }

    public static final int MAX_LICENSE_LEN = 16;

    //交通图片参数子结构
    public static final int PICTURE_NAME_LEN = 64;
    @Structure.FieldOrder({"struRelativeTime","struAbsTime","szPicName","byPicType","byRes"})
    public static class NET_DVR_TRAFFIC_PICTURE_PARAM extends Structure
    {
        public NET_DVR_TIME_V30 struRelativeTime = new NET_DVR_TIME_V30(); //抓拍相对时标
        public NET_DVR_TIME_V30 struAbsTime = new NET_DVR_TIME_V30();  //抓拍绝对时标
        public byte[] szPicName = new byte[PICTURE_NAME_LEN/*64*/];
        public byte byPicType;//图片类型 0-车牌图，1-抓拍原图，2-合成图，3-特写图
        public byte[] byRes = new byte[63];
    }
    //获取交通数据条件结构
    @Structure.FieldOrder({"dwSize","dwQueryCond","dwChannel","struStartTime","struEndTime","sLicense",
            "dwPlateType","dwPlateColor","dwVehicleColor","dwVehicleType","dwIllegalType","dwEventType",
            "dwForensiceType","wVehicleLogoRecog","byLaneNo","byDirection","wMinSpeed","wMaxSpeed","byDataType","byExecuteCtrl","byRes"})
    public static class NET_DVR_TRAFFIC_DATA_QUERY_COND extends Structure {
        public int dwSize;
        /*
    Bit0-通道有效
    Bit1-时间有效
    Bit2-车牌号有效
    Bit3-车牌类型有效
    Bit4-车牌颜色有效
    Bit5-车身颜色有效
    Bit6-车辆类型有效
    Bit7-车辆品牌有效
    Bit8-车道号有效
    Bit9-监测方向有效
    Bit10-最低速度有效
    Bit11-最高速度有效
    Bit12-数据类型有效
    Bit13-布控方式类型有效
    Bit14-违法取证有效
    Bit15-事件类型有效
    Bit16-取证类型有效
    */
        public int dwQueryCond;//查询条件 0表示无效，1表示有效
        public int dwChannel;//默认是1（[1~32]，bit0表示通道1，依次类推bit31表示通道32）
        public NET_DVR_TIME_V30 struStartTime;//开始时间
        public NET_DVR_TIME_V30 struEndTime;//结束时间
        public byte[] sLicense= new byte[MAX_LICENSE_LEN/*16*/];//(设备支持模糊查询, GB2312编码)
        /*
    Bit0-未知（其他）
    Bit1-标准民用车与军车
    Bit2-02式民用车牌
    Bit3-武警车
    Bit4-警车
    Bit5-民用车双行尾牌
    Bit6-使馆车牌
    Bit7-农用车
    Bit8-摩托车
    */
        public int dwPlateType;//车牌类型（支持按位表示，可以复选）
        /*
        Bit0-未知（其他）
        Bit1-黄色
        Bit2-白色
        Bit3-黑色
        Bit4-绿色
        Bit5-蓝色
        */
        public int dwPlateColor;//车牌颜色（支持按位表示，可以复选）
        /*
    Bit0-未知（其他）
    Bit1-白色
    Bit2-银色
    Bit3-灰色
    Bit4-黑色
    Bit5-红色
    Bit6-深蓝色
    Bit7-蓝色
    Bit8-黄色
    Bit9-绿色
    Bit10-棕色
    Bit11-粉色
    Bit12-紫色
    Bit13-深灰色
    */
        public int dwVehicleColor;//车身颜色（支持按位表示，可以复选）
        /*
       Bit0-未知（其他）
       Bit1-客车
       Bit2-大货车
       Bit3-轿车
       Bit4-面包车
       Bit5-小货车
       Bit6-行人
       Bit7-二轮车
       Bit8-三轮车
       Bit9-SUV/MPV
       Bit10-中型客车
       */
        public int dwVehicleType;//车辆类型（支持按位表示，可以复选）
        /**
         Bit0-其他（保留）
         Bit1-低速
         Bit2-超速
         Bit3-逆行
         Bit4-闯红灯
         Bit5-压车道线
         Bit6-不按导向
         Bit7-路口滞留
         Bit8-机占非
         Bit9-违法变道
         Bit10-不按车道
         Bit11-违反禁令
         Bit12-路口停车
         Bit13-绿灯停车
         Bit14-未礼让行人
         Bit15-违章停车
         Bit16-违章掉头
         Bit17-占用应急车道
         Bit18-未系安全带
         */
        public int dwIllegalType;
        /**
         Bit0-其他（保留）
         Bit1-拥堵
         Bit2-停车
         Bit3-逆行
         Bit4-行人
         Bit5-抛洒物
         Bit6-烟雾
         Bit7-压线
         Bit8-黑名单
         Bit9-超速
         Bit10-变道
         Bit11-掉头
         Bit12-机占非
         Bit13-加塞
         */
        public int dwEventType;
        /**
         Bit0-其他（保留）
         Bit1-城市公路违法停车
         Bit2-高速公路违法停车
         Bit3-压线
         Bit4-逆行
         Bit5-违法变道
         Bit6-机占非
         */
        public int dwForensiceType;
        public short wVehicleLogoRecog;  //车辆主品牌，参考"车辆主品牌.xlsx" （仅单选）
        public byte byLaneNo;//车道号（0~255,0号车道 表示 车道号未知）
        public byte byDirection;//监测方向，1-上行，2-下行，3-双向，4-由东向西，5-由南向北,6-由西向东，7-由北向南
        public short wMinSpeed;//最低速度（0~999）单位km/h
        public short wMaxSpeed;//最高速度（0~999）单位km/h
        public byte byDataType;//数据类型 0-卡口数据，1-违法数据，2-交通事件，3-取证数据 （仅单选）
        public byte byExecuteCtrl;//布控 0-白名单，1-黑名单，0xff-其他
        public byte[] byRes = new byte[254];
    }

    public static final int MAX_TRAFFIC_PICTURE_NUM = 8; //交通图片数量
    //交通数据结构体
    @Structure.FieldOrder({"dwSize","dwChannel","sLicense","dwPlateType","dwPlateColor","dwVehicleColor","dwVehicleType",
            "dwIllegalType","dwEventType","dwForensiceType","wVehicleLogoRecog","byLaneNo","byDirection","wSpeed","byDataType","byRes","struTrafficPic"})
    public static class NET_DVR_TRAFFIC_DATA_QUERY_RESULT extends Structure
    {
        public int dwSize;
        public int dwChannel;//默认是1（[1~32]）
        public byte[] sLicense = new byte[MAX_LICENSE_LEN/*16*/];
        /*
    Bit0-未知（其他）
    Bit1-标准民用车与军车
    Bit2-02式民用车牌
    Bit3-武警车
    Bit4-警车
    Bit5-民用车双行尾牌
    Bit6-使馆车牌
    Bit7-农用车
    Bit8-摩托车
    */
        public int dwPlateType;//车牌类型
        /*
    Bit0-未知（其他）
    Bit1-黄色
    Bit2-白色
    Bit3-黑色
    Bit4-绿色
    Bit5-蓝色
    */
        public int dwPlateColor;//车牌颜色
        /*
    Bit0-未知（其他）
    Bit1-白色
    Bit2-银色
    Bit3-灰色
    Bit4-黑色
    Bit5-红色
    Bit6-深蓝色
    Bit7-蓝色
    Bit8-黄色
    Bit9-绿色
    Bit10-棕色
    Bit11-粉色
    Bit12-紫色
    Bit13-深灰色
    */
        public int dwVehicleColor;//车身颜色
        /*
    Bit0-未知（其他）
    Bit1-客车
    Bit2-大货车
    Bit3-轿车
    Bit4-面包车
    Bit5-小货车
    Bit6-行人
    Bit7-二轮车
    Bit8-三轮车
    Bit9-SUV/MPV
    Bit10-中型客车
    Bit11-机动车
    Bit12-非机动车
    Bit13-小型轿车
    Bit14-微型轿车
    Bit15-皮卡车
    Bit16-集装箱卡车
    Bit17-微卡，栏板卡
    Bit18-渣土车
    Bit19-吊车，工程车
    Bit20-油罐车
    Bit21-混凝土搅拌车
    Bit22-平板拖车
    Bit23-两厢轿车
    Bit24-三厢轿车
    Bit25-轿跑
    Bit26-小型客车
    */
        public int dwVehicleType;//车辆类型
        /**
         Bit0-其他（保留）
         Bit1-低速
         Bit2-超速
         Bit3-逆行
         Bit4-闯红灯
         Bit5-压车道线
         Bit6-不按导向
         Bit7-路口滞留
         Bit8-机占非
         Bit9-违法变道
         Bit10-不按车道
         Bit11-违反禁令
         Bit12-路口停车
         Bit13-绿灯停车
         Bit14-未礼让行人
         Bit15-违章停车
         Bit16-违章掉头
         Bit17-占用应急车道
         Bit18-未系安全带
         */
        public int dwIllegalType;
        /**
         Bit0-其他（保留）
         Bit1-拥堵
         Bit2-停车
         Bit3-逆行
         Bit4-行人
         Bit5-抛洒物
         Bit6-烟雾
         Bit7-压线
         Bit8-黑名单
         Bit9-超速
         Bit10-变道
         Bit11-掉头
         Bit12-机占非
         Bit13-加塞
         */
        public int dwEventType;
        /**
         Bit0-其他（保留）
         Bit1-城市公路违法停车
         Bit2-高速公路违法停车
         Bit3-压线
         Bit4-逆行
         Bit5-违法变道
         Bit6-机占非
         */
        public int dwForensiceType;
        public short wVehicleLogoRecog;  //车辆主品牌，参考"车辆主品牌.xlsx"
        public byte byLaneNo;//车道号（0~255,0号车道 表示 车道号未知）
        public byte byDirection;//监测方向，1-上行，2-下行，3-双向，4-由东向西，5-由南向北,6-由西向东，7-由北向南
        public short wSpeed;//速度（0~999）单位km/h
        public byte byDataType;//数据类型: 0-卡口 1-违法 2-事件 3-取证
        public byte[] byRes = new byte[253];
        public NET_DVR_TRAFFIC_PICTURE_PARAM[] struTrafficPic = new NET_DVR_TRAFFIC_PICTURE_PARAM[MAX_TRAFFIC_PICTURE_NUM/*8*/];
    }

    public static interface FRemoteConfigCallback extends StdCallCallback {
        public void invoke(int dwType, NET_DVR_TRAFFIC_DATA_QUERY_RESULT lpBuffer, int dwBufLen, Pointer pUserData);
    }

    int NET_DVR_StartRemoteConfig(int lUserID, int dwCommand, Pointer lpInBuffer, long dwInBufferLen, FRemoteConfigCallback cbStateCallback, Pointer pUserData);
}

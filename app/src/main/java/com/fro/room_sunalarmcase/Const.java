package com.fro.room_sunalarmcase;
public class Const {

	public static String TAG="CASE";

	//光照度
	public static String SUN_CHK= "01 03 00 2a 00 01 a5 c2";
	public static int SUN_NUM=1;
	public static int SUN_LEN=7;
	public static Integer sun=null;
	public static Integer maxLim=500;
	
	//数码管
	public static String TUBE_CMD= "01 10 00 5e 00 02 04 12 11 03 17 62 9c";
	
	//蜂鸣器
	public static String BUZZER_ON= "01 10 00 5a 00 02 04 01 00 00 00 77 10";
	public static String BUZZER_OFF= "01 10 00 5a 00 02 04 00 00 00 00 76 ec";
	public static boolean isBuzzerOn=false;
	
	//窗帘
	public static String CURTAIN_ON= "01 10 00 4a 00 01 02 02 bb e9 29";
	public static boolean isCurtainOn=false;

	//人体
	public static String BODY_CHK= "01 03 00 3c 00 01 44 06";
	public static int BODY_NUM=1;
	public static int BODY_LEN=7;
	public static Boolean BODY=null;

	//风扇
	public static String FAN_ON= "01 10 00 48 00 01 02 00 01 68 18";
	public static String FAN_OFF= "01 10 00 48 00 01 02 00 02 28 19";
	public static boolean isFanOn=false;

	//IP端口
	public static String SUN_IP= "192.168.0.103";
	public static int SUN_PORT=4001;
	public static String TUBE_IP= "192.168.0.106";
	public static int TUBE_PORT=4001;
	public static String BUZZER_IP= "192.168.0.107";
	public static int BUZZER_PORT=4001;
	public static String CURTAIN_IP= "192.168.0.108";
	public static int CURTAIN_PORT=4001;
	public static String BODY_IP= "192.168.0.104";
	public static int BODY_PORT=4001;
	public static String FAN_IP= "192.168.0.105";
	public static int FAN_PORT=4001;
	
	//配置
	public static Integer time=500;
	public static Boolean linkage=true;
}

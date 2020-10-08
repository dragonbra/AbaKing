package com.fro.util;

import android.util.Log;

/**
 * Created by Jorble on 2016/5/26.
 */
public class FRODigTube {
	private static final String TAG = "FRODigTube";

	/**
	 * 整型数据转成数码管命令，1234->"01 10 00 5e 00 02 04 11 12 13 14 df 19"
	 *
	 * @param value
	 * @return
	 */
	public static String intToCmdString(int value) {

		// 返回命令
		String cmd = null;

		//如果在正常范围内才进行操作
		if (value >= 0 && value <= 9999) {

			// 默认命令字符串,未加CRC
			StringBuffer cmdSb = new StringBuffer("01 10 00 5e 00 02 04 10 10 10 10");

			// 整型数据转成字符串
			// 1234->"1234"

			String valueStr = String.valueOf(value).toString();

			// 得到数据转换成字符串后的长度
			// valueLen=5
			int valueLen = valueStr.length();

			// 将字符串切成字符串数组
			// "1234"->{"","1","2","3","4"}
			String[] valueStrArr = valueStr.split("");

			// 根据字符串数组长度得出数据命令
			// {"1"}->"01 10 00 5e 00 02 04 10 10 10 11"
			// {"1","2","3","4"}->"01 10 00 5e 00 02 04 11 12 13 14"
			switch (valueStrArr.length) {
			case 2:
				cmdSb.replace(31, 32, valueStrArr[1]).toString();
				break;
			case 3:
				cmdSb.replace(28, 29, valueStrArr[1]).toString();
				cmdSb.replace(31, 32, valueStrArr[2]).toString();
				break;
			case 4:
				cmdSb.replace(25, 26, valueStrArr[1]).toString();
				cmdSb.replace(28, 29, valueStrArr[2]).toString();
				cmdSb.replace(31, 32, valueStrArr[3]).toString();
				break;
			case 5:
				cmdSb.replace(22, 23, valueStrArr[1]).toString();
				cmdSb.replace(25, 26, valueStrArr[2]).toString();
				cmdSb.replace(28, 29, valueStrArr[3]).toString();
				cmdSb.replace(31, 32, valueStrArr[4]).toString();
				break;

			default:
				break;
			}
			
			//计算crc，"df 19"
			String crcStr=CRCValidate.calculateStringCRC(cmdSb.toString());
			
			//cmdSb加上crc
			//"01 10 00 5e 00 02 04 11 12 13 14"->"01 10 00 5e 00 02 04 11 12 13 14 df 19"
			cmdSb.append(" "+crcStr);
			
			//得到完整命令，"01 10 00 5e 00 02 04 11 12 13 14 df 19"
			cmd=cmdSb.toString();
			
		} else {
			Log.i(TAG, "数据不在0~9999之间！");
		}

		return cmd;
	}
	
}

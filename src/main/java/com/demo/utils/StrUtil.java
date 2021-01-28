package com.demo.utils;



public class StrUtil {
	
	/**
	 * 性别转换
	 */
	public static String genderToString(int gender){
		String str = "";
		
		if (gender == 0) {
			str = "-";
		}
		
		if (gender == 1) {
			str = "男";
		}
		
		if (gender == 2) {
			str = "女";
		}
		
		return str;
	}


}

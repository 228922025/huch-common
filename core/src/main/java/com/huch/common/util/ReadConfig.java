package com.huch.common.util;

import java.util.ResourceBundle;

/**
 * 获取properties属性文件中的值
 * @author admin
 *
 */
public class ReadConfig {
	//设置读取文件路径
	private static final ResourceBundle bundle = ResourceBundle.getBundle("db");

	/**
	 * 通过键获取值
	 * @param key
	 * @return
	 */
	public static final String get(String key) {
		return bundle.getString(key);
	}

	
	public static void main(String[] args) {
		String dbString = get("url");
		System.out.println(dbString);
	}

}

package com.areaofit.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 
 * @Description 读取properties文件的工具类
 * @Author Huangjinwen
 * @Date 2017年11月18日-下午4:15:50
 */
public class PropertiesUtil {

	// 文件地址
	private static final String PROPERTIES_PATH = "/jdbc.properties";

	private static Properties properties = null;

	static {
		if (properties == null) {
			load(PROPERTIES_PATH);
		}
	}

	/**
	 * 加载 properties 文件
	 * 
	 * @param fileName
	 */
	public static void load(String fileName) {
		properties = new Properties();
		InputStream inputStream = PropertiesUtil.class.getResourceAsStream(fileName);
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 获取 properties 文件的值
	 * 
	 * @param key
	 * @return
	 */
	public static String getString(String key) {
		return properties.getProperty(key);
	}

	/**
	 * 获取 properties 文件的数字值
	 * @param key
	 * @return
	 */
	public static Integer getInt(String key) {
		try {
			return Integer.parseInt(properties.getProperty(key));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}
}

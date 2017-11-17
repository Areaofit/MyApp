package com.areaofit.app.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 
 * @Description 获取IP地址所属地理位置
 * @Author Huangjinwen
 * @Date 2017年11月17日-下午3:34:24
 */
public class AddressUtils {
	
	//查询 url
	private static final String TAOBAO_VALIDATEIP_PATH = "http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json";
	
	//编码
	private static final String ENCODEING = "utf-8";
	
	/**
	 * 获取所属城市
	 * @param params
	 * @return
	 */
	public static String getAddress(String params) {
		if(params == null || params.length() < 1 || "null".equalsIgnoreCase(params)){
			return "";
		}
		params = "&ip=" + params;
		String returnStr = AddressUtils.getRs(AddressUtils.TAOBAO_VALIDATEIP_PATH, params, AddressUtils.ENCODEING);
		JSONObject json = null;
		String province_code = "";
		String city_code = "";
		if(returnStr != null){
			try {
				json = new JSONObject(returnStr);
				province_code = json.getString("province");
				city_code = json.getString("city");
			} catch (JSONException e) {
				e.printStackTrace();
				return "";
			}
		}
		return province_code + ":" + city_code;
	}
	
	/**
	 * 从url获取结果
	 * @param path
	 * @param params
	 * @param encoding
	 * @return
	 */
	public static String getRs(String path, String params, String encoding){
		URL url = null;
		HttpURLConnection connection = null;
		try {
			url = new URL(path);
			connection = (HttpURLConnection)url.openConnection(); // 新建连接实例
			connection.setConnectTimeout(2000); // 设置连接超时时间，单位毫秒
			connection.setReadTimeout(2000); // 设置读取数据超时时间，单位毫秒
			connection.setDoInput(true); // 是否打开输出流 true|false
			connection.setDoOutput(true); // 是否打开输入流true|false
			connection.setRequestMethod("POST"); // 提交方法POST|GET
			connection.setUseCaches(false); // 是否缓存true|false
			connection.connect(); // 打开连接端口
			DataOutputStream out = new DataOutputStream(connection.getOutputStream());
			out.writeBytes(params);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(),encoding));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine())!= null) {
				buffer.append(line);
			}
			reader.close();
			return buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			connection.disconnect(); // 关闭连接
		}
		return null;
	}
	
	/**
	 * 字符转码
	 * @param theString
	 * @return
	 */
	public static String decodeUnicode(String theString){
		char aChar;
		int len = theString.length();
		StringBuffer buffer = new StringBuffer(len);
		for (int i = 0; i < len;) {
			aChar = theString.charAt(i++);
			if(aChar == '\\'){
				aChar = theString.charAt(i++);
				if(aChar == 'u'){
					int val = 0;
					for(int j = 0; j < 4; j++){
						aChar = theString.charAt(i++);
						switch (aChar) {
							case '0':
							case '1':
							case '2':
							case '3':
							case '4':
							case '5':
							case '6':
							case '7':
							case '8':
							case '9':
								val = (val << 4) + aChar - '0';
								break;
							case 'a':
							case 'b':
							case 'c':
							case 'd':
							case 'e':
							case 'f':
								val = (val << 4) + 10 + aChar - 'a';
								break;
							case 'A':
							case 'B':
							case 'C':
							case 'D':
							case 'E':
							case 'F':
								val = (val << 4) + 10 + aChar - 'A';
								break;
							default:
							throw new IllegalArgumentException(
								"Malformed      encoding.");
						    }
						}
						buffer.append((char) val);
					}else{
						if(aChar == 't'){
							aChar = '\t';
						}
						if(aChar == 'r'){
							aChar = '\r';
						}
						if(aChar == 'n'){
							aChar = '\n';
						}
						if(aChar == 'f'){
							aChar = '\f';
						}
						buffer.append(aChar);
					}
				
				}else{
					buffer.append(aChar);
				}
			}
		return buffer.toString();
	}

}

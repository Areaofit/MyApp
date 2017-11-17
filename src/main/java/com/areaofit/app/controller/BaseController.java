package com.areaofit.app.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 
 * @Description Controller基类
 * @Author Huangjinwen
 * @Date 2017年11月17日-下午4:40:11
 */
public class BaseController {

	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest();
	}

	public HttpSession getSession() {
		return ((ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes()).getRequest().getSession();
	}

	public ServletContext getServletContext() {
		return ContextLoader.getCurrentWebApplicationContext()
				.getServletContext();
	}

	public int getInt(String name) {
		return getInt(name, 0);
	}

	public int getInt(String name, int defaultValue) {
		String resultStr = getRequest().getParameter(name);
		if (resultStr != null) {
			try {
				return Integer.parseInt(resultStr);
			} catch (Exception e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public BigDecimal getBigDecimal(String name) {
		return getBigDecimal(name, null);
	}

	public BigDecimal getBigDecimal(String name, BigDecimal defaultValue) {
		String resultStr = getRequest().getParameter(name);
		if (resultStr != null) {
			try {
				return BigDecimal.valueOf(Double.parseDouble(resultStr));
			} catch (Exception e) {
				return defaultValue;
			}
		}
		return defaultValue;
	}

	public String getString(String name) {
		return getString(name, null);
	}

	public String getString(String name, String defaultValue) {
		String resultStr = getRequest().getParameter(name);
		if (resultStr == null || "".equals(resultStr)
				|| "null".equals(resultStr) || "undefined".equals(resultStr)) {
			return defaultValue;
		} else {
			return resultStr;
		}
	}
	
	public void outPrint(HttpServletResponse response, Object result) {
		try {
			response.setCharacterEncoding("utf-8");
			PrintWriter out = response.getWriter();
			out.print(result.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 得到内置对象中的值
	 * @param <T>
	 * @param key
	 * @param scop
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getBean(String key, String scop) {
		HttpServletRequest req = this.getRequest();
		T t = null;
		if (null == scop) {
			t = (T) req.getAttribute(key);
			if (null == t)
				t = (T) req.getSession().getAttribute(key);
			if (null == t)
				t = (T) getServletContext().getAttribute(key);
		} else {

			if ("request".equals(scop)) {
				t = (T) req.getAttribute(key);
			} else if ("session".equals(scop)) {
				t = (T) req.getSession().getAttribute(key);
			} else if ("application".equals(scop)) {
				t = (T) getServletContext().getAttribute(key);
			}
		}
		return t;
	}

	/**
	 * 放入内置对象
	 * @param key
	 * @param obj
	 * @param scop
	 */
	public void setBean(String key, Object obj, String scop) {
		if (null == key) {
			key = "request";
		}
		HttpServletRequest req = this.getRequest();
		if ("request".equals(scop)) {
			req.setAttribute(key, obj);
		} else if ("session".equals(scop)) {
			req.getSession().setAttribute(key, obj);
		} else if ("application".equals(scop)) {
			this.getServletContext().setAttribute(key, obj);
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String,Object> getParameterMap(HttpServletRequest request) {
	    // 参数Map
	    Map<String,Object> properties = request.getParameterMap();
	    // 返回值Map
	    Map<String,Object> returnMap = new HashMap<String,Object>();
	    Iterator entries = properties.entrySet().iterator();
	    Map.Entry entry;
	    String name = "";
	    String value = "";
	    while (entries.hasNext()) {
	        entry = (Map.Entry) entries.next();
	        name = (String) entry.getKey();
	        Object valueObj = entry.getValue();
	        if(null == valueObj){
	            value = "";
	        }else if(valueObj instanceof String[]){
	            String[] values = (String[])valueObj;
	            for(int i=0;i<values.length;i++){
	                value = values[i] + ",";
	            }
	            value = value.substring(0, value.length()-1);
	        }else{
	            value = valueObj.toString();
	        }
	        returnMap.put(name, value);
	    }
	    return returnMap;
	}

}

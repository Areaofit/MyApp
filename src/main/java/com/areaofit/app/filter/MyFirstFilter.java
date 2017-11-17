package com.areaofit.app.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * 
 * @Description 第一个Filter学习
 * @Author Huangjinwen
 * @Date 2017年11月17日-下午10:49:44
 */
public class MyFirstFilter implements Filter {
	
	/**
	 * 用户在配置filter时，可以使用为filter配置一些初始化参数，当web容器实例化Filter对象,
	 * 调用其init方法时，会把封装了filter初始化参数的filterConfig对象传递进来
	 * 在编写filter时，通过filterConfig对象的方法，就可获得以下内容:
	 * 		String getFilterName();//得到filter的名称
	 * 		String getInitParameter(String name);//返回在部署描述中指定名称的初始化参数的值。如果不存在返回null
	 * 		Enumeration getInitParameterNames();//返回过滤器的所有初始化参数的名字的枚举集合
	 * 		public ServletContext getServletContext();//返回Servlet上下文对象的引用
	 */
	public FilterConfig filterConfig;

	@Override
	public void destroy() {
		this.filterConfig = null;
	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper((HttpServletResponse) arg1);
		String disableFilter = filterConfig.getInitParameter("disableFilter");
		if (disableFilter != null && disableFilter.toLowerCase().equals("true")) {
			System.out.println("过滤器失效");
			chain.doFilter(arg0, arg1);
			return;
		}
		System.out.println("过滤器生效");
		chain.doFilter(arg0, arg1);
		return;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		this.filterConfig = arg0;
	}

}

package com.areaofit.app.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 
 * @Description 学习HttpServletRequest对象
 * @Author Huangjinwen
 * @Date 2017年11月17日-下午9:53:22
 */
@Controller
@RequestMapping("/test")
public class HttpServletRequestController extends BaseController{

	@RequestMapping("/request")
	public void testRequest(ModelMap modelMap){
		HttpServletRequest request = getRequest();
		String requestURL = request.getRequestURL().toString();
		System.out.println(requestURL);
	}
}

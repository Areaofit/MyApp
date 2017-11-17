package com.areaofit.app.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.areaofit.app.dao.UserDao;

@Controller
@RequestMapping("/app")
public class BaseController {
	
	@Resource
	private UserDao userDao;

	@RequestMapping("/test")
	public void test(){
		System.out.println("ok");
	}
	
}

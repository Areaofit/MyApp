package com.areaofit.app.dao;

import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public interface UserDao {
	
	public Map<String, Object> getUserById(String id);

}

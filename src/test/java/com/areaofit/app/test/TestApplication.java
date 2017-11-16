package com.areaofit.app.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-mybatis.xml"})
public class TestApplication {
	
	/**
	 * 测试spring-mybatis.xml配置文件能否正常启动
	 */
	@Test
	public void test() {
		System.out.println("hello");
	}

}

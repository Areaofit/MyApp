package com.areaofit.app.ftp.model;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

/**
 * 
 * @Description FTP对象池
 * @Author Huangjinwen
 * @Date 2017年12月8日-下午1:05:34
 */
public class FTPPool extends Pool<FTPClient>{

	public FTPPool(GenericObjectPoolConfig poolConfig, String host,int port,String user,String password,String passiveModeConf) {
		super(poolConfig, new FTPPoolableObjectFactory(host, port, user, password, passiveModeConf));
	}

	
}

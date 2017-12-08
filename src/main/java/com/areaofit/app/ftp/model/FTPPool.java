package com.areaofit.app.ftp.model;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

public class FTPPool extends Pool<FTPClient>{

	public FTPPool(GenericObjectPoolConfig poolConfig, String host,int port,String user,String password,String passiveModeConf) {
		super(poolConfig, new FTPPoolableObjectFactory(host, port, user, password, passiveModeConf));
	}

	
}

package com.areaofit.app.ftp.model;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;

/**
 * 
 * @Description TODO
 * @Author Huangjinwen
 * @Date 2017年12月8日-上午11:39:40
 */
public class FTPPoolableObjectFactory extends BasePooledObjectFactory<FTPClient> {

	private static Logger logger = Logger.getLogger(FTPPoolableObjectFactory.class);
	private String host;
	private int port;
	private String user;
	private String password;
	private String passiveModeConf;

	public FTPPoolableObjectFactory(String host, int port, String user, String password, String passiveModeConf) {
		this.host = host; // ftp服务器地址
		this.port = port; // 端口号
		this.user = user; // 用户名
		this.password = password; // 密码
		this.passiveModeConf = passiveModeConf; // ftp服务器被动模式(Passive Mode)
		logger.debug("passiveModeConf:" + passiveModeConf);
	}

	/**
	 * 创建FTPClient对象
	 */
	@Override
	public FTPClient create() throws Exception {
		FTPClient ftpClient = new FTPClient();
		ftpClient.connect(host, port);
		ftpClient.setControlKeepAliveTimeout(300); // set timeout to 5 minutes
		ftpClient.login(user, password);
		boolean passiveMode = false;
		if (passiveModeConf == null || Boolean.parseBoolean(passiveModeConf) == true) {
			passiveMode = true;
		}
		if (passiveMode) {
			ftpClient.enterLocalPassiveMode();
		}
		ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
		return ftpClient;
	}

	/**
	 * 包装FTPClient对象
	 */
	@Override
	public PooledObject<FTPClient> wrap(FTPClient paramT) {
		return new DefaultPooledObject<FTPClient>(paramT);
	}

	/**
	 * 销毁FTP对象
	 */
	@Override
	public void destroyObject(PooledObject<FTPClient> p) throws Exception {
		FTPClient ftpClient = p.getObject();
		if (!ftpClient.isConnected()) {
			// 没有连接，直接销毁对象
		} else {
			try {
				ftpClient.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		super.destroyObject(p);
	}

	/**
	 * 验证FTPClient是否连接成功
	 */
	@Override
	public boolean validateObject(PooledObject<FTPClient> p) {
		try {
			FTPClient ftpClient = p.getObject();
			boolean flag= ftpClient.isConnected();
			ftpClient.changeWorkingDirectory("/");
			return flag;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}

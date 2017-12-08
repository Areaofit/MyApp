package com.areaofit.app.ftp.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.log4j.Logger;

import com.areaofit.app.ftp.model.FTPPool;

/**
 * 
 * @Description 文件上传（图片，视频，文件）
 * @Author Huangjinwen
 * @Date 2017年12月8日-下午2:40:15
 */
public class FileUploadServiceImpl implements FileUploadService {

	private String host;

	private int port;

	private String user;

	private String password;

	private String passiveModeConf;

	private int maxTotal;

	private String accessServer;

	private FTPPool pool;

	private final static Logger logger = Logger.getLogger(FileUploadServiceImpl.class);

	public FTPPool getFTPPool() {
		if (StringUtils.isBlank(host)) {
			throw new RuntimeException("上传配置不能空！");
		}
		if (pool == null) {
			GenericObjectPoolConfig config = new GenericObjectPoolConfig();
			// 最大池容量,最大连接数
			config.setMaxTotal(maxTotal);
			// 最大空闲数
			config.setMaxIdle(maxTotal);
			// 最小空闲数
			config.setMinIdle(0);
			// 池为空时取对象等待的最大毫秒数.
			config.setMaxWaitMillis(60 * 1000);
			// 连接超时时是否阻塞，false时报异常；ture时阻塞直到超时； 默认true
			config.setBlockWhenExhausted(true);
			// 取出对象时验证(此处设置成验证ftp是否处于连接状态).
			config.setTestOnBorrow(true);
			// 还回对象时验证(此处设置成验证ftp是否处于连接状态).
			config.setTestOnReturn(true);
			pool = new FTPPool(config, host, port, user, password, passiveModeConf);
		}
		return pool;
	}

	@Override
	public String uploadFile(byte[] bytes, String targetFolderPath, String targetFileName) {
		if(bytes==null || bytes.length==0 ){
			return "ERROR";
		}
		InputStream inputStream = null;
		FTPClient ftpClient = getFTPPool().getResource();
		try {
			long start = System.currentTimeMillis();			
			ftpClient = changeDirectory(ftpClient,targetFolderPath);	
			inputStream = new ByteArrayInputStream(bytes);
			ftpClient.storeFile(targetFileName, inputStream);
			long end = System.currentTimeMillis();
			logger.info("getFtpClient time = " + (end-start));
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return "ERROR";
		}finally {
			try {
				inputStream.close();
			} catch (Exception e2) {
				logger.error(e2.getMessage(),e2);
			}
			try {
				pool.returnResource(ftpClient);
			} catch (Exception e2) {
				logger.error(e2.getMessage(),e2);
			}
        }
		return accessServer + "/" + targetFolderPath + "/" + targetFileName;
	}

	/**
	 * 改变路径
	 * @param path 新路径
	 * @throws Exception
	 */
	private FTPClient changeDirectory(FTPClient ftpClient, String path) throws Exception {
		try {
			ftpClient.changeWorkingDirectory("/");
			if (!ftpClient.changeWorkingDirectory(path)) {
				String[] temp = path.split("/");
				String tmpStr = "";
				for (int i = 0; i < temp.length; i++) {
					tmpStr += "/" + temp[i];
					if (ftpClient.changeWorkingDirectory(tmpStr)) {
						continue;
					} else {
						ftpClient.makeDirectory(tmpStr);
					}
				}
				if (!ftpClient.changeWorkingDirectory(path)) {
					pool.returnResource(ftpClient);
					ftpClient = getFTPPool().getResource();
					ftpClient.changeWorkingDirectory(path);
				}
			}
		} catch (Exception e) {
			logger.error("改变ftp路径出错" + e.getStackTrace(), e);
			throw new RuntimeException(e);

		}
		return ftpClient;
	}

	@Override
	public boolean copyFile(String sourceFilePath, String targetFilePath) {
		return false;
	}

	@Override
	public String getHttpUrl(String targetFolderPath, String targetFileName) {
		return accessServer+ "/" + targetFolderPath + "/{size}/" + targetFileName;
	}

	@Override
	public String getHttpFileUrl(String targetFolderPath, String targetFileName) {
		
		return accessServer + "/" + targetFolderPath + "/" + targetFileName;
	}

	@Override
	public void deletePathAndFileName(String pathAndFileName) {
		FTPClient ftpClient = getFTPPool().getResource();	
		try {
			ftpClient.deleteFile(pathAndFileName);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
			pool.returnResource(ftpClient);
        }
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPassiveModeConf() {
		return passiveModeConf;
	}

	public void setPassiveModeConf(String passiveModeConf) {
		this.passiveModeConf = passiveModeConf;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public String getAccessServer() {
		return accessServer;
	}

	public void setAccessServer(String accessServer) {
		this.accessServer = accessServer;
	}

	public FTPPool getPool() {
		return pool;
	}

	public void setPool(FTPPool pool) {
		this.pool = pool;
	}
	
	

}

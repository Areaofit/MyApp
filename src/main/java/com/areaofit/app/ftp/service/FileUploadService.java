package com.areaofit.app.ftp.service;

/**
 * 
 * @Description 文件上传（图片，视频，文件）
 * @Author Huangjinwen
 * @Date 2017年12月8日-下午2:00:15
 */
public interface FileUploadService {
	
	/**
	 * 上传文件
	 * @param bytes 要上传的文件
	 * @param targetFolderPath 要上传到FTP服务器上的文件路径
	 * @param targetFileName 传到FTP服务器上的文件名
	 * @return 返回文件的HTTP路径
	 */
	String uploadFile(byte[] bytes, String targetFolderPath, String targetFileName);

	/**
	 * FTP服务器上复制文件
	 * @param sourceFilePath 源文件路径  
	 * @param targetFilePath 目标文件路径
	 * @return
	 */
	boolean copyFile(String sourceFilePath, String targetFilePath);
	
	/**
	 * 获取文件HTTP路径 
	 * @param targetFolderPath
	 * @param targetFileName
	 * @return
	 */
	String getHttpUrl(String targetFolderPath, String targetFileName);
	
	/**
	 * 获取文件HTTP路径 
	 * @param targetFolderPath
	 * @param targetFileName
	 * @return
	 */
	String getHttpFileUrl(String targetFolderPath, String targetFileName);
	
	/**
	 * @param pathAndFileName 根据路径 删除文件
	 */
	void deletePathAndFileName(String pathAndFileName);
}

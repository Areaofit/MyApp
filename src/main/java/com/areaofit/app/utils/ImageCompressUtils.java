package com.areaofit.app.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.im4java.core.CompositeCmd;
import org.im4java.core.ConvertCmd;
import org.im4java.core.IM4JavaException;
import org.im4java.core.IMOperation;
import org.im4java.core.IdentifyCmd;
import org.im4java.process.ArrayListOutputConsumer;

/**
 * 
 * @Description ImageMagick 压缩工具类
 * @Author Huangjinwen
 * @Date 2017年12月8日-下午3:32:30
 */
public class ImageCompressUtils {

	private final static Logger logger = Logger.getLogger(ImageCompressUtils.class);
	
	public  final static String IMAGEMAGICK = "C:/Program Files (x86)/GraphicsMagick-1.3.21-Q8";
	
	static {
		System.setProperty("jmagick.systemclassloader", "no");
	}
	
	
	/**
	 * 压缩图片
	 * @param sourceImage 原图片
	 * @param width 压缩后的宽
	 * @param height 压缩后的高
	 * @param isEqualRatio 是否等比压缩
	 * @return
	 */
	public static byte[] resize(byte[] sourceImage, Integer width, Integer height, boolean isEqualRatio) {
		try {
			if(width == null || height == null) return sourceImage;
			String tempDir = System.getProperty("java.io.tmpdir"); //默认的临时文件路径
			String imageName = UUID.randomUUID().toString();
			File sourceFile = new File(tempDir + "/" + imageName);  
			FileUtils.writeByteArrayToFile(sourceFile, sourceImage);
			String sourcePath = sourceFile.getAbsolutePath();
			String targetPath = sourcePath + "_target";
			resize(sourcePath, targetPath, width, height, isEqualRatio);
			File targetFile = new File(targetPath);
			byte[] result = FileUtils.readFileToByteArray(targetFile);
			FileUtils.deleteQuietly(sourceFile);
			FileUtils.deleteQuietly(targetFile);
			return result;
		} catch (IOException e) {
			logger.error(e.toString(), e);
		}
		return null;
	}
	
	/**
	 * 加完水印后压缩图片
	 * @param sourceImage 原图片
	 * @param logoPath 水印图片path
	 * @param gravity 水印位置
	 * @param dissolve 清晰度
	 * @param width 压缩后的宽
	 * @param height 压缩后的高
	 * @param isEqualRatio 是否等比压缩
	 * @return
	 */
	public static byte[] resizeWatermark(byte[] sourceImage, String logoPath,GravityTypeEnum gravity,Integer dissolve, Integer width, Integer height, boolean isEqualRatio) {
		try {
			if(width == null || height == null) return sourceImage;
			String tempDir = System.getProperty("java.io.tmpdir");
			String imageName = UUID.randomUUID().toString();
			File sourceFile = new File(tempDir + "/" + imageName);  
			FileUtils.writeByteArrayToFile(sourceFile, sourceImage);
			String sourcePath = sourceFile.getAbsolutePath();
			String targetPath = sourcePath + "_target";
			resizeWatermark(sourcePath, targetPath, logoPath, gravity.name(), dissolve, width, height, isEqualRatio); 
			File targetFile = new File(targetPath);
			byte[] result = FileUtils.readFileToByteArray(targetFile);
			FileUtils.deleteQuietly(sourceFile);
			FileUtils.deleteQuietly(targetFile);
			return result;
		} catch (IOException e) {
			logger.error(e.toString(), e);
		}
		return null;
	}
	
	/**
	 * 压缩加水印加白边的图片
	 * @param sourceImage 原图片
	 * @param logoPath 水印图片path
	 * @param gravity 水印位置
	 * @param dissolve 清晰度
	 * @param width 压缩后的宽
	 * @param height 压缩后的高
	 * @param isEqualRatio 是否等比压缩
	 * @return
	 */
	public static byte[] resizeWatermarkAddWhite(byte[] sourceImage, String logoPath,GravityTypeEnum gravity,Integer dissolve, Integer width, Integer height, boolean isEqualRatio) {
		try {
			if(width == null || height == null) return sourceImage;
			
			String tempDir = System.getProperty("java.io.tmpdir");
			String imageName = UUID.randomUUID().toString();
			File sourceFile = new File(tempDir + "/" + imageName);  
			FileUtils.writeByteArrayToFile(sourceFile, sourceImage);  
			String sourcePath = sourceFile.getAbsolutePath();
			String targetPath = sourcePath + "_target";
			String watermarkTargetPath = sourcePath + "_watermark_target";
			resizeAddWhite(sourcePath, targetPath, width, height);
			
			watermark(targetPath, watermarkTargetPath, logoPath, gravity.name(), dissolve);
			
			File targetFile = new File(targetPath);
			File watermarkTargetFile=new File(watermarkTargetPath);
			
			byte[] result = FileUtils.readFileToByteArray(watermarkTargetFile);
			
			FileUtils.deleteQuietly(sourceFile);
			FileUtils.deleteQuietly(targetFile);
			FileUtils.deleteQuietly(watermarkTargetFile);
			return result;
		} catch (IOException e) {
			logger.error(e.toString(), e);
		}
		return null;
	}
	
	
	/**
	 * 图片加水印
	 * @param sourceImage 原图
	 * @param logoPath 水印图片
	 * @return
	 */
	public static byte[] watermark(byte[] sourceImage, String logoPath,String align) {
		try {
			String tempDir = System.getProperty("java.io.tmpdir");
			String imageName = UUID.randomUUID().toString();
			File sourceFile = new File(tempDir + "/" + imageName);  
			FileUtils.writeByteArrayToFile(sourceFile, sourceImage);
			String sourcePath = sourceFile.getAbsolutePath();
			String targetPath = sourcePath + "_target";
			if(align.equals("left")){
				watermark(sourcePath, targetPath, logoPath,GravityTypeEnum.northwest.name(),100);
			}else if(align.equals("center")){
				watermark(sourcePath, targetPath, logoPath,GravityTypeEnum.center.name(),100);
			}else if(align.equals("center")){
				watermark(sourcePath, targetPath, logoPath,GravityTypeEnum.west.name(),100);
			}
			else{
				watermark(sourcePath, targetPath, logoPath,GravityTypeEnum.southeast.name(),100);
			}
			File targetFile = new File(targetPath);
			byte[] result = FileUtils.readFileToByteArray(targetFile);
			if(sourceFile.exists()){
				sourceFile.delete();
			}
			if(targetFile.exists()){
				targetFile.delete();
			}
			return result;
		} catch (IOException e) {
			logger.error(e.toString(), e);
		}
		return null;
	}
	
	/**
	 * 压缩图片
	 * @param sourcePath 源文件路径
	 * @param targetPath 缩略图路径
	 * @param width 设定宽
	 * @param height 设定长
	 * @param isEqualRatio 是否等比缩放
	 */
	public static void resize(String sourcePath, String targetPath, Integer width, Integer height, boolean isEqualRatio) {
		try {
			IMOperation  op = new IMOperation(); 
			if(isEqualRatio){
				 op.resize(width, height);
			}else{
				op.resize(width, height,'!');
			}
		    
	        op.addImage(sourcePath);  
	        op.addImage(targetPath);  
	        ConvertCmd convert = new ConvertCmd(true); 
	        if(isWindows()){
	        	convert.setSearchPath(IMAGEMAGICK);
	        }
	        
	        convert.run(op);  
		} catch(Exception e) {
			logger.error(e.toString(), e);
		}  
	}

	
	/**
	 * 图片加水印
	 * @param sourcePath 源文件路径
	 * @param targetPath 修改后路径
	 * @param logoPath logo图路径
	 * @param logoPath logo图路径
	 * @param gravity 位置
	 * @throws MagickException
	 */
	public static void watermark(String sourcePath, String targetPath, String logoPath,String gravity,Integer dissolve )  {
		
		IMOperation op = new IMOperation();
    	op.gravity(gravity);
    	op.dissolve(dissolve);
    	op.addImage(logoPath);    
    	op.addImage(sourcePath);
    	op.addImage(targetPath);
    	CompositeCmd cmd = new CompositeCmd(true);
		try {
			if(isWindows()){
				cmd.setSearchPath(IMAGEMAGICK);
		    }
			cmd.run(op);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IM4JavaException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 压缩加水印的图片
	 * @param sourcePath 原图片path
	 * @param targetPath 加完水印后图片的path
	 * @param logoPath 水印path
	 * @param gravity 水印位置
	 * @param dissolve 清晰度
	 * @param width 压缩后的宽
	 * @param height 压缩后的高
	 * @param isEqualRatio 是否等比压缩
	 */
    public static void resizeWatermark(String sourcePath, String targetPath, String logoPath,String gravity,Integer dissolve, Integer width, Integer height, boolean isEqualRatio){
    	IMOperation op = new IMOperation();
    	op.gravity(gravity);
    	//op.geometry(0);
    	op.dissolve(dissolve);
    	op.addImage(logoPath);    
    	op.addImage(sourcePath);
    	
    	if(isEqualRatio){
			 op.resize(width, height);
		}else{
			op.resize(width, height,'!');
		}
	    
    	op.addImage(targetPath);
    	CompositeCmd cmd = new CompositeCmd(true);
    	try {
			if(isWindows()){
				cmd.setSearchPath(IMAGEMAGICK);
		    }
			cmd.run(op);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (IM4JavaException e) {
			e.printStackTrace();
		}
    }

	/** 
     * 根据坐标裁剪图片 
     * @param srcPath   要裁剪图片的路径 
     * @param newPath   裁剪图片后的路径 
     * @param x   起始横坐标 
     * @param y   起始纵坐标 
     * @param x1  结束横坐标 
     * @param y1  结束纵坐标 
     */ 
	public static void cut(String srcPath, String newPath, int x, int y, int x1,int y1) throws Exception {
		  int width = x1 - x;  
	      int height = y1 - y;  
	      IMOperation op = new IMOperation();  
	      op.addImage(srcPath);  
	      /**  width：裁剪的宽度    * height：裁剪的高度 * x：裁剪的横坐标 * y：裁剪纵坐标  */  
	      op.crop(width, height, x, y);  
	      op.addImage(newPath);  
	      ConvertCmd convert = new ConvertCmd(true);  
	      if(isWindows()){
	    	  convert.setSearchPath(IMAGEMAGICK);
		  }
	      convert.run(op);  

	}
	
	/**
	 * 
	 * @Description 水印方向
	 * @Author Huangjinwen
	 * @Date 2017年12月8日-下午3:35:27
	 */
	public  enum  GravityTypeEnum{
		east,south,west,north,southeast,southwest,northwest,northeast,center
	}
	
	/**
	 * 判断是否是windows系统
	 * @return
	 */
	private static  boolean isWindows(){
		Properties prop = System.getProperties();
		final String os = prop.getProperty("os.name");
		if(os.startsWith("win") || os.startsWith("Win")){
			return true;
		}
		return false;
	}
	
	 /**
	  * 图片信息
	  * @param imagePath
	  * @return
	  */
    public static String showImageInfo(String imagePath) {
        String line = null;
        try {
            IMOperation op = new IMOperation();
            op.format("width:%w,height:%h,path:%d%f,size:%b%[EXIF:DateTimeOriginal]");
            op.addImage(1);
            IdentifyCmd identifyCmd = new IdentifyCmd(true);
            identifyCmd.setSearchPath(IMAGEMAGICK);
            ArrayListOutputConsumer output = new ArrayListOutputConsumer();
            identifyCmd.setOutputConsumer(output);
            identifyCmd.run(op, imagePath);
            ArrayList<String> cmdOutput = output.getOutput();
            assert cmdOutput.size() == 1;
            line = cmdOutput.get(0);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
        return line;
    }
    
    /**
     * 图片旋转
     * @param srcImagePath
     * @param destImagePath
     * @param angle
     */
    public static void rotate(String srcImagePath, String destImagePath, double angle) {
        try {
            IMOperation op = new IMOperation();
            op.rotate(angle);
            op.addImage(srcImagePath);
            op.addImage(destImagePath);
            ConvertCmd cmd = new ConvertCmd(true);
            if(isWindows()){
            	cmd.setSearchPath(IMAGEMAGICK);
            }
            cmd.run(op);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 图片带白边生成尺寸
     * @param srcImagePath
     * @param destImagePath
     * @param angle
     */
    public static void resizeAddWhite(String sourcePath, String targetPath, Integer width, Integer height) {
        try {
        	IMOperation op = new IMOperation();
    		op.addImage(sourcePath);
    		op.thumbnail(width, height);
    		op.background("white");
    		op.gravity("center");
    		op.extent(width, height);
            op.addImage(targetPath);            
            
            ConvertCmd cmd = new ConvertCmd(true);
            if(isWindows()){
            	cmd.setSearchPath(IMAGEMAGICK);
            }
            cmd.run(op);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //测试
	public static void main(String[] args) throws IOException, InterruptedException, IM4JavaException {
       watermark("G:\\face3.jpg", "G:\\target_picture.jpg", "G:\\logo_water_big.png", GravityTypeEnum.southeast.name(), 100);
	}	
	
}

package com.areaofit.app.controller;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.areaofit.app.utils.ValidateCode;

/**
 * 
 * @Description 生成验证码控制器
 * @Author Huangjinwen
 * @Date 2017年11月17日-下午3:16:13
 */
@Controller
@RequestMapping("/common")
public class ValidateCodeController {

	@RequestMapping("/validateCode")
	public void validateCode(HttpServletRequest request, HttpServletResponse response) {
		// 设置不缓存图片
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "No-cache");
		response.setDateHeader("Expires", 0);
		// 指定生成的相应图片
		response.setContentType("image/jpeg");
		ValidateCode vCode = new ValidateCode();
		BufferedImage image = new BufferedImage(vCode.getWidth(), vCode.getHeight(), BufferedImage.TYPE_INT_BGR);
		Graphics2D g = image.createGraphics();
		// 定义字体样式
		Font myFont = new Font("黑体", Font.BOLD, 24);
		// 设置字体
		g.setFont(myFont);
		g.setColor(new Color(255, 255, 255));
		// 绘制背景
		g.fillRect(0, 0, vCode.getWidth(), vCode.getHeight());
		g.setColor(new Color(255, 255, 255));
		vCode.drawRandomLines(g, 3);
		// 验证码字符串
		String validCode = vCode.drawRandomString(4, g);
		
		//TODO 此处可以将生成的验证码存储起来，用来下次验证
		System.out.println("验证码是："+validCode);
		
		g.dispose();
		try {
			ImageIO.write(image, "JPEG", response.getOutputStream());
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

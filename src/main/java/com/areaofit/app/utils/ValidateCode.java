package com.areaofit.app.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

/**
 * 
 * @Description 图形验证码生成
 * @Author Huangjinwen
 * @Date 2017年11月17日-下午3:01:02
 */
public class ValidateCode {

	/**
	 * 验证码图片的宽度
	 */
	private int width = 90;

	/**
	 * 验证码图片的高度
	 */
	private int height = 35;

	/**
	 * 验证码的数量
	 */
	private Random random = new Random();

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Random getRandom() {
		return random;
	}

	public void setRandom(Random random) {
		this.random = random;
	}

	/**
	 * 绘制干扰线
	 * 
	 * @param g
	 *            Graphics2D对象，用来绘制图像
	 * @param nums
	 *            干扰线的条数
	 */
	public void drawRandomLines(Graphics2D g, int nums) {
		g.setColor(new Color(0, 0, 0));
		for (int i = 0; i < nums; i++) {
			int x1 = random.nextInt(width);
			int y1 = random.nextInt(height);
			int x2 = random.nextInt(12);
			int y2 = random.nextInt(12);
			g.drawLine(x1, y1, x2, y2);
		}
	}

	/**
	 * 获取随机字符串， 此函数可以产生由大小写字母，汉字，数字组成的字符串
	 * 
	 * @param length
	 *            随机字符串的长度
	 * @return 随机字符串
	 */
	public String drawRandomString(int length, Graphics2D g) {
		StringBuffer strbuf = new StringBuffer();
		String temp = "";
		int itmp = 0;
		for (int i = 0; i < length; i++) {
			itmp = random.nextInt(26) + 65;
			temp = String.valueOf((char) itmp);
			Color color = new Color(20 + random.nextInt(20), 20 + random.nextInt(20), 20 + random.nextInt(20));
			g.setColor(color);
			// 想文字旋转一定的角度
			AffineTransform trans = new AffineTransform();
			trans.rotate(random.nextInt(45) * 3.14 / 180, 15 * i + 8, 7);
			// 缩放文字
			float scaleSize = random.nextFloat() + 0.8f;
			if (scaleSize > 1f)
				scaleSize = 1f;
			trans.scale(scaleSize, scaleSize);
			g.setTransform(trans);
			g.drawString(temp, 15 * i + 18, 14);
			strbuf.append(temp);
		}
		g.dispose();
		return strbuf.toString();
	}

}

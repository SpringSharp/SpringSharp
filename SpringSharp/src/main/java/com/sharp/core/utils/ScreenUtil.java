/**
 * 工程名称:SpringSharp
 * 文件名称:ScreenUtil.java
 * 包名称： com.sharp.core.utils
 * 创建时间: 2016-7-8上午09:51:55
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.utils;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

/**
 * 类名称: ScreenUtil
 * 类描述：屏幕操作工具类
 * 创建时间: 2016-7-8 上午09:52:23
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class ScreenUtil {
	/**
	 * 方法名称：captureScreen
	 * 方法描述：抓屏
	 * 创建人： 邢凌霄
	 * 创建时间：2016-7-8 上午09:47:12
	 * @param fileName
	 * @throws Exception
	 * @since JDK 1.5
	 */
	public static void captureScreen(String fileName) throws Exception {  
		 
		   Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();  
		   Rectangle screenRectangle = new Rectangle(screenSize);  
		   Robot robot = new Robot();  
		   BufferedImage image = robot.createScreenCapture(screenRectangle);  
		   ImageIO.write(image, "png", new File(fileName));  
		 
		} 
}


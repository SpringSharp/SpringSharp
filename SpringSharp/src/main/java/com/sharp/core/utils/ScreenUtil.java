/**
 * ��������:SpringSharp
 * �ļ�����:ScreenUtil.java
 * �����ƣ� com.sharp.core.utils
 * ����ʱ��: 2016-7-8����09:51:55
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
 * ������: ScreenUtil
 * ����������Ļ����������
 * ����ʱ��: 2016-7-8 ����09:52:23
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class ScreenUtil {
	/**
	 * �������ƣ�captureScreen
	 * ����������ץ��
	 * �����ˣ� ������
	 * ����ʱ�䣺2016-7-8 ����09:47:12
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


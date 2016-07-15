/**
 * ��������:SpringSharp
 * �ļ�����:SpringUtil.java
 * �����ƣ� com.sharp.core.utils
 * ����ʱ��: 2016��7��7������10:24:23
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * ������: SpringUtil
 * ��������Spring�����࣬����bean���ƻ�ȡbeanʵ��
 * ����ʱ��: 2016��7��7�� ����10:24:26
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class SpringUtil {
	 private static ApplicationContext  ctx = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
	    
	    public static Object getBean(String beanName){
	         return ctx.getBean(beanName);
	    }  
}


/**
 * 工程名称:SpringSharp
 * 文件名称:SpringUtil.java
 * 包名称： com.sharp.core.utils
 * 创建时间: 2016年7月7日上午10:24:23
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.utils;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 类名称: SpringUtil
 * 类描述：Spring工具类，根据bean名称获取bean实例
 * 创建时间: 2016年7月7日 上午10:24:26
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class SpringUtil {
	 private static ApplicationContext  ctx = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
	    
	    public static Object getBean(String beanName){
	         return ctx.getBean(beanName);
	    }  
}


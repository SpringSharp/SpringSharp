/**
 * 工程名称:springejb
 * 文件名称:UserService.java
 * 包名称： com.module.auth.service
 * 创建时间: 2016-7-4下午05:25:44
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.module.auth.service;

import com.sharp.module.auth.dao.UserDao;
import com.sharp.module.auth.entity.User;

public class UserService {
	UserDao dao = new UserDao();
	
	/**
	 * 方法名称：add
	 * 方法描述：新增
	 * 创建人： 邢凌霄
	 * 创建时间：2016-7-4 下午05:31:11
	 * @param user
	 * @return
	 * @throws Exception 
	 * @since JDK 1.5
	 */
	public long add(User user) throws Exception{
		return dao.add(user);
	}
	

}


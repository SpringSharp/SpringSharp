/**
 * 工程名称:springejb
 * 文件名称:UserDao.java
 * 包名称： com.module.auth.dao
 * 创建时间: 2016-7-4下午05:05:51
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.module.auth.dao;

import com.sharp.core.dao.BaseDao;
import com.sharp.module.auth.entity.User;

public class UserDao extends BaseDao<User>{
	
	public UserDao(){
		super.strTableName = "SYS_AUTH_USER";
		this.setUseMaxID();
	}
}


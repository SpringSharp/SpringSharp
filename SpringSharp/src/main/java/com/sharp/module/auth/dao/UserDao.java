/**
 * ��������:springejb
 * �ļ�����:UserDao.java
 * �����ƣ� com.module.auth.dao
 * ����ʱ��: 2016-7-4����05:05:51
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


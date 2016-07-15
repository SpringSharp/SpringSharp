/**
 * ��������:springejb
 * �ļ�����:UserService.java
 * �����ƣ� com.module.auth.service
 * ����ʱ��: 2016-7-4����05:25:44
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.module.auth.service;

import com.sharp.module.auth.dao.UserDao;
import com.sharp.module.auth.entity.User;

public class UserService {
	UserDao dao = new UserDao();
	
	/**
	 * �������ƣ�add
	 * ��������������
	 * �����ˣ� ������
	 * ����ʱ�䣺2016-7-4 ����05:31:11
	 * @param user
	 * @return
	 * @throws Exception 
	 * @since JDK 1.5
	 */
	public long add(User user) throws Exception{
		return dao.add(user);
	}
	

}


/**
 * ��������:springejb
 * �ļ�����:UserAction.java
 * �����ƣ� com.module.auth.web
 * ����ʱ��: 2016-7-4����05:33:40
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.module.auth.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sharp.core.sysframe.pager.entity.PagerInfo;
import com.sharp.core.sysframe.pager.entity.ResultInfo;
import com.sharp.module.auth.entity.User;
import com.sharp.module.auth.service.UserService;
public class UserAction {
	
	
	UserService service = new UserService();
	public long add(User user) throws Exception{
		return service.add(user);
	}
}


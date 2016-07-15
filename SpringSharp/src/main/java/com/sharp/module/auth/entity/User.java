/**
 * ��������:springejb
 * �ļ�����:UserInfo.java
 * �����ƣ� com.module.auth.entity
 * ����ʱ��: 2016-7-4����04:28:55
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.module.auth.entity;

import com.sharp.core.entity.BaseEntity;

/**
 * ������: UserInfo
 * ���������û���Ϣ
 * ����ʱ��: 2016-7-4 ����04:29:08
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class User extends BaseEntity{
	
	public long lId =-1;
	public String sLoginName = "";
	public String sLoginPassword = "";
	
	public long lStatusID = -1;
	

    @Override
	public long getlId() {
		
		return lId;
	}
	@Override
	public void setlId(long lId) {
		putUsedField("lId", lId);
		this.lId = lId ;
	}
	public String getsLoginName() {
		return sLoginName;
	}
	public void setsLoginName(String sLoginName) {
		putUsedField("sLoginName", sLoginName);
		this.sLoginName = sLoginName;
	}
	public String getsLoginPassword() {
		return sLoginPassword;
	}
	public void setsLoginPassword(String sLoginPassword) {
		putUsedField("sLoginPassword", sLoginPassword);
		this.sLoginPassword = sLoginPassword;
	}
    public long getlStatusID() {
        return lStatusID;
    }
    public void setlStatusID(long lStatusID) {
        putUsedField("lStatusID", lStatusID);
        this.lStatusID = lStatusID;
    }
	

}


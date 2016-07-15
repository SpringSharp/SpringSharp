/**
 * ��������:SpringSharp
 * �ļ�����:ResultInfo.java
 * �����ƣ� com.sharp.core.sysframe.pager.entity
 * ����ʱ��: 2016��7��11������5:33:06
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.entity;

import java.util.ArrayList;
import java.util.List;


public class ResultInfo {
    private long total = 0;// ������
    private List resultList = new ArrayList();// ���صĽ����
    private Class<?> resultClass = null;// ���صĽ�������INFO���CLASS

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }

    public List getResultList()
    {
        return resultList;
    }

    public void setResultList(List resultList)
    {
        this.resultList = resultList;
    }

    public Class<?> getResultClass()
    {
        return resultClass;
    }

    public void setResultClass(Class<?> resultClass)
    {
        this.resultClass = resultClass;
    }
}


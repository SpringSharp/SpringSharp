/**
 * 工程名称:SpringSharp
 * 文件名称:ResultInfo.java
 * 包名称： com.sharp.core.sysframe.pager.entity
 * 创建时间: 2016年7月11日下午5:33:06
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.entity;

import java.util.ArrayList;
import java.util.List;


public class ResultInfo {
    private long total = 0;// 总行数
    private List resultList = new ArrayList();// 返回的结果集
    private Class<?> resultClass = null;// 返回的结果集里的INFO类的CLASS

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


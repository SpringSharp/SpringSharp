/**
 * 工程名称:SpringSharp
 * 文件名称:ResultPagerInfo.java
 * 包名称： com.sharp.core.sysframe.pager.entity
 * 创建时间: 2016年7月11日下午4:13:21
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.entity;

import java.util.List;

/**
 * 类名称: ResultPagerInfo
 * 类描述：分页返回实体类
 * 创建时间: 2016年7月11日 下午4:13:23
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class ResultPagerInfo {
    private long page = 0;// 当前页数
    private long total = 0;// 总行数
    private List<Object> rowList = null;// 数据
    private String usertext = null;// 用户自定义数据

    public long getPage()
    {
        return page;
    }

    public void setPage(long page)
    {
        this.page = page;
    }

    public long getTotal()
    {
        return total;
    }

    public void setTotal(long total)
    {
        this.total = total;
    }


    public List<Object> getRowList() {
        return rowList;
    }

    public void setRowList(List<Object> rowList) {
        this.rowList = rowList;
    }

    public String getUsertext() {
        return usertext;
    }

    public void setUsertext(String usertext) {
        this.usertext = usertext;
    }

}


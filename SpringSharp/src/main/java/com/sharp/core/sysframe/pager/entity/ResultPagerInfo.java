/**
 * ��������:SpringSharp
 * �ļ�����:ResultPagerInfo.java
 * �����ƣ� com.sharp.core.sysframe.pager.entity
 * ����ʱ��: 2016��7��11������4:13:21
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.entity;

import java.util.List;

/**
 * ������: ResultPagerInfo
 * ����������ҳ����ʵ����
 * ����ʱ��: 2016��7��11�� ����4:13:23
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class ResultPagerInfo {
    private long page = 0;// ��ǰҳ��
    private long total = 0;// ������
    private List<Object> rowList = null;// ����
    private String usertext = null;// �û��Զ�������

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


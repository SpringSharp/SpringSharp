/**
 * ��������:SpringSharp
 * �ļ�����:ResultPagerRowInfo.java
 * �����ƣ� com.sharp.core.sysframe.pager.entity
 * ����ʱ��: 2016��7��11������5:31:38
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.entity;

import java.util.List;
public class ResultPagerRowInfo {
    
    private List<?> cell = null;
    private String id = null;
    private boolean validate = true;  //�ж��Ƿ񱻴۸� Ĭ��Ϊtrue
    
    public boolean isValidate() {
        return validate;
    }
    public void setValidate(boolean validate) {
        this.validate = validate;
    }
    public List<?> getCell() {
        return cell;
    }
    public void setCell(List<?> cell) {
        this.cell = cell;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    
}


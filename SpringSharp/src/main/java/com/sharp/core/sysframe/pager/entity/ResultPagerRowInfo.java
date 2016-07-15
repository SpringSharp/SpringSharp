/**
 * 工程名称:SpringSharp
 * 文件名称:ResultPagerRowInfo.java
 * 包名称： com.sharp.core.sysframe.pager.entity
 * 创建时间: 2016年7月11日下午5:31:38
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.entity;

import java.util.List;
public class ResultPagerRowInfo {
    
    private List<?> cell = null;
    private String id = null;
    private boolean validate = true;  //判断是否被篡改 默认为true
    
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


/**
 * 工程名称:SpringSharp
 * 文件名称:FlexiGridInfo.java
 * 包名称： com.sharp.core.sysframe.pager.entity
 * 创建时间: 2016年7月12日下午4:43:39
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.entity;
/**
 * 类名称: FlexiGridInfo
 * 类描述：FlexiGrid表格实体类
 * 创建时间: 2016年7月12日 下午4:43:41
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class FlexiGridInfo {

    String flexigrid_page = "1";
    String flexigrid_rp = "10";
    String flexigrid_sortname = "";
    String flexigrid_sortorder = "";
    String flexigrid_query = "";
    String flexigrid_qtype = "";
    
    public String getFlexigrid_page() {
        return flexigrid_page;
    }
    public void setFlexigrid_page(String flexigrid_page) {
        this.flexigrid_page = flexigrid_page;
    }
    public String getFlexigrid_rp() {
        return flexigrid_rp;
    }
    public void setFlexigrid_rp(String flexigrid_rp) {
        this.flexigrid_rp = flexigrid_rp;
    }
    public String getFlexigrid_sortname() {
        return flexigrid_sortname;
    }
    public void setFlexigrid_sortname(String flexigrid_sortname) {
        this.flexigrid_sortname = flexigrid_sortname;
    }
    public String getFlexigrid_sortorder() {
        return flexigrid_sortorder;
    }
    public void setFlexigrid_sortorder(String flexigrid_sortorder) {
        this.flexigrid_sortorder = flexigrid_sortorder;
    }
    public String getFlexigrid_query() {
        return flexigrid_query;
    }
    public void setFlexigrid_query(String flexigrid_query) {
        this.flexigrid_query = flexigrid_query;
    }
    public String getFlexigrid_qtype() {
        return flexigrid_qtype;
    }
    public void setFlexigrid_qtype(String flexigrid_qtype) {
        this.flexigrid_qtype = flexigrid_qtype;
    }
}


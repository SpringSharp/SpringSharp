/**
 * 工程名称:SpringSharp
 * 文件名称:PagerInfo.java
 * 包名称： com.sharp.core.sysframe.entity
 * 创建时间: 2016年7月11日下午4:09:04
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.entity;

import java.util.List;
import java.util.Map;

/**
 * 类名称: PagerInfo
 * 类描述： 分页操作类
 * 创建时间: 2016年7月11日 下午4:09:13
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class PagerInfo {
    private String sqlString = "";//sql语句。与resultInfo不能共存
    private ResultInfo resultInfo = null;//返回结果集方式调用。与sql不能共存
    private List<Object> depictList = null;//需要进行转换的字段在这里进行配置
    private String usertext = "";//用户自定义的文本
    
    private Class<?> extClazz = null;//对于集合的自定义处理类
    private String extMothodName = null;//对于集合的自定义处理方法
    private Map<Object, Object> params = null;
    
    private Class<?> extTotalClazz = null;//对于集合汇总的自定义处理类
    private String extTotalMothodName = null;//对于集合汇总的自定义处理方法
    private Map<Object, Object> totalParams = null;
    private int rowShowNum = 6;//对于集合汇总的每行显示个数
    
    private List<Object> resultList=null;
    
    public void setExtensionMothod(Class<?> clazz, String methodName,Map<Object, Object> params)
    {
        this.extClazz = clazz;
        this.extMothodName = methodName;
        this.params = params;
    }
    
    public void setExtensionMothod(Class<?> clazz, String methodName)
    {
        setExtensionMothod(clazz,methodName,null);
    }
    
    public void setTotalExtensionMothod(Class<?> clazz, String methodName, Map<Object, Object> params, int lRowShowNum)
    {
        this.extTotalClazz = clazz;
        this.extTotalMothodName = methodName;
        this.totalParams = params;
        this.rowShowNum = lRowShowNum;
    }
    
    public void setTotalExtensionMothod(Class<?> clazz, String methodName, Map<Object, Object> params)
    {
        setTotalExtensionMothod(clazz,methodName,params,rowShowNum);
    }
    
    public void setTotalExtensionMothod(Class<?> clazz, String methodName, int lRowShowNum)
    {
        setTotalExtensionMothod(clazz,methodName,null,lRowShowNum);
    }
    
    public void setTotalExtensionMothod(Class<?> clazz, String methodName)
    {
        setTotalExtensionMothod(clazz,methodName,null);
    }
    
    public String getSqlString() {
        return sqlString;
    }

    public void setSqlString(String sqlString) {
        this.sqlString = sqlString;
    }

    public ResultInfo getResultInfo()
    {
        return resultInfo;
    }

    public void setResultInfo(ResultInfo resultInfo)
    {
        this.resultInfo = resultInfo;
    }

    public String getUsertext() {
        return usertext;
    }

    public void setUsertext(String usertext) {
        this.usertext = usertext;
    }

    public List<Object> getDepictList() {
        return depictList;
    }

    public void setDepictList(List<Object> depictList) {
        this.depictList = depictList;
    }

    public Class<?> getExtClazz() {
        return extClazz;
    }

    public void setExtClazz(Class<?> extClazz) {
        this.extClazz = extClazz;
    }

    public String getExtMothodName() {
        return extMothodName;
    }

    public void setExtMothodName(String extMothodName) {
        this.extMothodName = extMothodName;
    }

    public Map<Object, Object> getParams() {
        return params;
    }

    public Class<?> getExtTotalClazz() {
        return extTotalClazz;
    }

    public void setExtTotalClazz(Class<?> extTotalClazz) {
        this.extTotalClazz = extTotalClazz;
    }

    public String getExtTotalMothodName() {
        return extTotalMothodName;
    }

    public void setExtTotalMothodName(String extTotalMothodName) {
        this.extTotalMothodName = extTotalMothodName;
    }

    public Map<Object, Object> getTotalParams() {
        return totalParams;
    }

    public void setTotalParams(Map<Object, Object> totalParams) {
        this.totalParams = totalParams;
    }

    public List<Object> getResultList() {
        return resultList;
    }

    public void setResultList(List<Object> resultList) {
        this.resultList = resultList;
    }

    public int getRowShowNum() {
        return rowShowNum;
    }

    public void setRowShowNum(int rowShowNum) {
        this.rowShowNum = rowShowNum;
    }

}


/**
 * ��������:SpringSharp
 * �ļ�����:PagerInfo.java
 * �����ƣ� com.sharp.core.sysframe.entity
 * ����ʱ��: 2016��7��11������4:09:04
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.entity;

import java.util.List;
import java.util.Map;

/**
 * ������: PagerInfo
 * �������� ��ҳ������
 * ����ʱ��: 2016��7��11�� ����4:09:13
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class PagerInfo {
    private String sqlString = "";//sql��䡣��resultInfo���ܹ���
    private ResultInfo resultInfo = null;//���ؽ������ʽ���á���sql���ܹ���
    private List<Object> depictList = null;//��Ҫ����ת�����ֶ��������������
    private String usertext = "";//�û��Զ�����ı�
    
    private Class<?> extClazz = null;//���ڼ��ϵ��Զ��崦����
    private String extMothodName = null;//���ڼ��ϵ��Զ��崦����
    private Map<Object, Object> params = null;
    
    private Class<?> extTotalClazz = null;//���ڼ��ϻ��ܵ��Զ��崦����
    private String extTotalMothodName = null;//���ڼ��ϻ��ܵ��Զ��崦����
    private Map<Object, Object> totalParams = null;
    private int rowShowNum = 6;//���ڼ��ϻ��ܵ�ÿ����ʾ����
    
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


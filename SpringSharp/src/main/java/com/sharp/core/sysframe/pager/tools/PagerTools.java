/**
 * 工程名称:SpringSharp
 * 文件名称:PagerTools.java
 * 包名称： com.sharp.core.sysframe.pager.tools
 * 创建时间: 2016年7月11日下午4:15:13
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.tools;

import java.io.Reader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

import com.sharp.core.sysframe.constant.PagerTypeConstant;
import com.sharp.core.sysframe.pager.entity.PagerDepictBaseInfo;
import com.sharp.core.sysframe.pager.entity.PagerInfo;
import com.sharp.core.sysframe.pager.entity.ResultInfo;
import com.sharp.core.sysframe.pager.entity.ResultPagerInfo;
import com.sharp.core.sysframe.pager.entity.ResultPagerRowInfo;
import com.sharp.core.utils.DataFormat;
import com.sharp.core.utils.NumberUtil;

/**
 * 类名称: PagerTools
 * 类描述：分页操作工具类
 * 创建时间: 2016年7月11日 下午4:15:18
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class PagerTools {
    
    /**
     * 根据配置信息将ResultSet数据库结果集转换到JSONList结果集
     * @param resultSet
     * @param pagerInfo
     * @return
     * @throws Exception
     */
    public static List<Object> convertResultSetToJSONListEx(SqlRowSet rowSet, PagerInfo pagerInfo) throws Exception{
        List<Object> resultList = new ArrayList<Object>(); //最终返回结果
        ResultPagerRowInfo rowInfo = null;
        List<Object> cellList = null;
        long rownum = -1;
        SqlRowSetMetaData resultSetMD = null;
        Map<Object,Object> sourceDataTypeMap = null;
        long dataType = -1;
        
        Iterator<?> pagerIter = null;
        PagerDepictBaseInfo baseInfo = null;
        String displayName = null;
        long displayType = -1;
        String cellString = "";
        
        Timestamp rsTimestamp = null;
        double rsDouble = 0.0;
        long rsLong = 0;
        String extensionString = null;
        Class<?>[] argClass = null;
        Method operateMethod = null;
        Object methodObj = null;
        
        
        try
        {
            if(rowSet!=null && pagerInfo != null)
            {
                if(pagerInfo.getDepictList() != null)
                {
                    
                    
                    resultSetMD = rowSet.getMetaData();
                    sourceDataTypeMap = new HashMap<Object,Object>();
                    for(int i=1; i<resultSetMD.getColumnCount(); i++)
                    {
                        sourceDataTypeMap.put(resultSetMD.getColumnName(i).toUpperCase(), String.valueOf(resultSetMD.getColumnType(i)));
                    }
                        
                    while(rowSet.next())
                    {
                        pagerIter = pagerInfo.getDepictList().iterator();
                        cellList = new ArrayList<Object>();
                        
                        while(pagerIter.hasNext())
                        {
                            baseInfo =  (PagerDepictBaseInfo)pagerIter.next();
                            rownum = rowSet.getLong("rownum");
                            
                            displayName = baseInfo.getDisplayName().toUpperCase();
                            if(sourceDataTypeMap.get(displayName) != null)
                            {
                                dataType = Long.parseLong(sourceDataTypeMap.get(displayName).toString());
                            }
                            displayType = baseInfo.getDisplayType();
                            
                            if(baseInfo.isExtension() == true)
                            {
                                extensionString = baseInfo.getExtensionString();
                                cellString = extensionString;
                            }
                            else
                            {
                                //根据数据库列对应的类型取值
                                if(dataType == java.sql.Types.DOUBLE)
                                {
                                    rsDouble = rowSet.getDouble(displayName);
                                    cellString = String.valueOf(rsDouble);
                                }
                                else if(dataType == java.sql.Types.NUMERIC)
                                {
                                    rsDouble = rowSet.getDouble(displayName);
                                    cellString = String.valueOf(rsDouble);
                                }
                                else if(dataType == java.sql.Types.DATE)
                                {
                                    rsTimestamp = rowSet.getTimestamp(displayName);
                                }
                                else if(dataType == java.sql.Types.TIMESTAMP)
                                {
                                    rsTimestamp = rowSet.getTimestamp(displayName);
                                }
                                else if(dataType == java.sql.Types.VARCHAR)
                                {
                                    cellString = rowSet.getString(displayName);
                                }
                                else if(dataType == java.sql.Types.CHAR)
                                {
                                    cellString = rowSet.getString(displayName);
                                }
                                else if(dataType == java.sql.Types.NULL)
                                {
                                    cellString = "";
                                }
                                else
                                {
                                    cellString = "";
                                }
                                
                                //转换为需要显示的类型
                                if(displayType == PagerTypeConstant.STRING)
                                {
                                    if(cellString == null)
                                    {
                                        cellString = "";
                                    }else if(cellString.indexOf(".")>0)
                                    {
                                        try{
                                            Double.valueOf(cellString);
                                            cellString = cellString.substring(0, cellString.indexOf("."));
                                        }catch (Exception e) {
                                        }
                                    }
                                }
                                else if(displayType == PagerTypeConstant.DATE)
                                {
                                    if(rsTimestamp == null)
                                    {
                                        cellString = "";
                                    }
                                    else
                                    {
                                        cellString = DataFormat.formatDate(rsTimestamp, DataFormat.DT_YYYY_MM_DD);
                                    }
                                }
                                else if(displayType == PagerTypeConstant.DATETIME)
                                {
                                    if(rsTimestamp == null)
                                    {
                                        cellString = "";
                                    }
                                    else
                                    {
                                        cellString = DataFormat.formatDate(rsTimestamp, DataFormat.DT_YYYYMMDD_HHMMSS);
                                    }
                                }
                                else if(displayType == PagerTypeConstant.AMOUNT_2)
                                {
                                    if(cellString!=null&&!cellString.equals(""))
                                    {
                                        cellString = NumberUtil.formatAmount(Double.parseDouble(cellString), 2);
                                    }else{
                                        cellString = "0.00";
                                    }
                                }
                                else if(displayType == PagerTypeConstant.AMOUNT_6)
                                {
                                    if(cellString!=null&&!cellString.equals(""))
                                    {
                                        //cellString = sessionMng.m_strCurrencySymbol + DataFormat.formatAmount(Double.parseDouble(cellString), 6);
                                        cellString = NumberUtil.formatAmount(Double.parseDouble(cellString), 6);
                                    }
                                }
                                else if(displayType == PagerTypeConstant.LONG)
                                {
                                    cellString = String.valueOf((long)Double.parseDouble(cellString));
                                }
                                else if(displayType == PagerTypeConstant.DOUBLE)
                                {
                                    //cellString = String.valueOf(rsDouble);
                                }
                                else if(displayType == PagerTypeConstant.FUNCTION)
                                {
                                    argClass = baseInfo.getFnArgClass();
                                    
                                    if(cellString == null||cellString.equals(""))
                                    {
                                        cellString = "";
                                    }
                                    else
                                    {
                                        if(argClass[0].getName().equals(long.class.getName()))
                                        {
                                            rsLong = (long)Double.parseDouble(cellString);
                                            
                                            operateMethod =  baseInfo.getFnClazz().getMethod(baseInfo.getFnMethodName(), argClass);
                                            if(Modifier.isStatic(operateMethod.getModifiers())){
                                                methodObj = operateMethod.invoke(null,  new Object[]{new Long(rsLong)});
                                            }
                                            else{
                                                methodObj = operateMethod.invoke(baseInfo.getFnClazz().newInstance(),  new Object[]{new Long(rsLong)});
                                            }
                                            cellString = String.valueOf(methodObj);
                                        }
                                        else if(argClass[0].getName().equals(String.class.getName()))
                                        {
                                            operateMethod =  baseInfo.getFnClazz().getMethod(baseInfo.getFnMethodName(), argClass);
                                            if(Modifier.isStatic(operateMethod.getModifiers())){
                                                methodObj = operateMethod.invoke(null, new Object[]{new String(cellString)});
                                            }
                                            else{
                                                methodObj = operateMethod.invoke(baseInfo.getFnClazz().newInstance(), new Object[]{new String(cellString)});
                                            }
                                            cellString = String.valueOf(methodObj);
                                        }
                                        else if(argClass[0].getName().equals(Timestamp.class.getName()))
                                        {
                                            operateMethod =  baseInfo.getFnClazz().getMethod(baseInfo.getFnMethodName(), argClass);
                                            if(Modifier.isStatic(operateMethod.getModifiers())){
                                                methodObj = operateMethod.invoke(null, new Object[]{rsTimestamp});
                                            }
                                            else{
                                                methodObj = operateMethod.invoke(baseInfo.getFnClazz().newInstance(), new Object[]{rsTimestamp});
                                            }
                                            cellString = String.valueOf(methodObj);
                                        }
                                    }
                                }
                            }
                            cellList.add(cellString);
                            cellString = "";
                            dataType = -1;
                        }
                        
                        
                        rowInfo = new ResultPagerRowInfo();
                        rowInfo.setCell(cellList);
                        rowInfo.setId(String.valueOf(rownum));
                        resultList.add(rowInfo);
                    }
                }
                else if (pagerInfo.getExtClazz()!=null && pagerInfo.getExtMothodName()!=null)
                {
                    Class<?> clazz = pagerInfo.getExtClazz();
                    String methodName = pagerInfo.getExtMothodName();
                    
                    Method method =null;
                    Method _method[] = clazz.getMethods();
                    
                    for(int i=0;i<_method.length;i++){
                        if(_method[i].getName().equals(methodName)){
                            method = _method[i];
                            break;
                        }
                    }
                    if(Modifier.isStatic(method.getModifiers())){
                        if(pagerInfo.getParams()!=null){
                            Object o = method.invoke(null, new Object[]{rowSet,pagerInfo.getParams()});
                            resultList.add(o);
                        }else{
                            Object o = method.invoke(null, new Object[]{rowSet});
                            resultList.add(o);
                        }
                    }else{
                        if(pagerInfo.getParams()!=null){
                            Object o = method.invoke(clazz.newInstance(), new Object[]{rowSet,pagerInfo.getParams()});
                            resultList.add(o);
                        }else{
                            Object o = method.invoke(clazz.newInstance(), new Object[]{rowSet});
                            resultList.add(o);
                        }
                    }
                }
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new Exception("转换结果集到JSON集合时异常", e);
        }
        
        return resultList;
        
    }
    /**
     * 根据配置信息将ResultSet数据库结果集转换到JSONList结果集
     * @param resultSet
     * @param pagerInfo
     * @return
     * @throws Exception
     */
    public static List<Object> convertResultSetToJSONList(SqlRowSet rowSet, PagerInfo pagerInfo) throws Exception
    {
        return convertResultSetToJSONListEx(rowSet,pagerInfo);
    }
    
    /**
     * 根据配置信息将ResultInfo数据库结果集转换到JSONList结果集
     * @param pagerInfo
     * @return
     * @throws Exception
     */
    public static List<Object> convertResultInfoToJSONList(PagerInfo pagerInfo,long startNum) throws Exception
    {
        ResultInfo sourceInfo = pagerInfo.getResultInfo();
        Iterator<Object> sourceIter = null;
        Object sourceObj = null;
        Field[] sourceFields = null;
        Field[] sourceFieldsParent = null; //父类属性
        Field currField = null;
        Object currFieldValue = null;
        Map<Object,Object> sourceMap = null;
        List<Object> resultList = new ArrayList<Object>(); //最终返回结果
        ResultPagerRowInfo rowInfo = null;
        List<Object> cellList = null;
        long rownum = startNum;
        
        Iterator<Object> pagerIter = null;
        PagerDepictBaseInfo baseInfo = null;
        String displayName = null;
        long displayType = -1;
        String cellString = "";
        
        Timestamp rsTimestamp = null;
        double rsDouble = 0.0;
        long rsLong = 0;
        
        String extensionString = null;
        Class<?>[] argClass = null;
        Method operateMethod = null;
        Object methodObj = null;
        
        
        try
        {
            
            if(sourceInfo != null && sourceInfo.getResultList() != null && sourceInfo.getResultList().size() > 0 && pagerInfo != null && pagerInfo.getDepictList() != null)
            {
                sourceIter = sourceInfo.getResultList().iterator();
                
                while(sourceIter.hasNext())
                {
                    sourceObj = sourceIter.next();
                    sourceFields = sourceObj.getClass().getDeclaredFields();
                    sourceFieldsParent = sourceObj.getClass().getSuperclass().getDeclaredFields();
                    
                    sourceMap = new HashMap<Object,Object>();
                    rownum=rownum+1;
                    for(int i=0; i<sourceFields.length; i++)
                    {
                        currField = sourceFields[i];
                        currField.setAccessible(true);
                        currFieldValue = currField.get(sourceObj);
                        
                        sourceMap.put(currField.getName().toUpperCase(), currFieldValue);
                        
                    }
                   for(int i=0; i<sourceFieldsParent.length; i++)
                    {
                        currField = sourceFieldsParent[i];
                        currField.setAccessible(true);
                        currFieldValue = currField.get(sourceObj);
                        sourceMap.put(currField.getName().toUpperCase(), currFieldValue);
                    }
                    pagerIter = pagerInfo.getDepictList().iterator();
                    cellList = new ArrayList<Object>();
                    
                    while(pagerIter.hasNext())
                    {
                        baseInfo =  (PagerDepictBaseInfo)pagerIter.next();                  
                        
                        displayName = baseInfo.getDisplayName().toUpperCase();
                        displayType = baseInfo.getDisplayType();
                        
                        if(baseInfo.isExtension() == true)
                        {
                            extensionString = baseInfo.getExtensionString();
                            cellString = extensionString;
                        }
                        else
                        {
                            currFieldValue = sourceMap.get(displayName);
                            
                            if(displayType == PagerTypeConstant.STRING)
                            {
                                if(currFieldValue != null){
                                    cellString = (String)currFieldValue;
                                }
                                else{
                                    cellString = "";
                                }
                                
                            }
                            else if(displayType == PagerTypeConstant.DATE)
                            {
                                if(currFieldValue != null){
                                    rsTimestamp = (Timestamp)currFieldValue;
                                    cellString = DataFormat.formatDate(rsTimestamp, DataFormat.DT_YYYY_MM_DD);
                                }
                                else{
                                    cellString = "";
                                }
                            }
                            else if(displayType == PagerTypeConstant.DATETIME)
                            {
                                if(currFieldValue != null){
                                    rsTimestamp = (Timestamp)currFieldValue;
                                    cellString = DataFormat.formatDate(rsTimestamp, DataFormat.DT_YYYYMMDD_HHMMSS);
                                }
                                else{
                                    cellString = "";
                                }
                            }
                            else if(displayType == PagerTypeConstant.AMOUNT_2)
                            {
                                if(currFieldValue != null){
                                    rsDouble = ((Double)currFieldValue).doubleValue();
                                    cellString = NumberUtil.formatAmount(rsDouble, 2);
                                }
                                else{
                                    cellString = "";
                                }
                            }
                            else if(displayType == PagerTypeConstant.AMOUNT_6)
                            {
                                if(currFieldValue != null){
                                    rsDouble = ((Double)currFieldValue).doubleValue();
                                    cellString = NumberUtil.formatAmount(rsDouble, 6);
                                }
                                else{
                                    cellString = "";
                                }
                            }
                            else if(displayType == PagerTypeConstant.LONG)
                            {
                                if(currFieldValue != null){
                                    rsLong = ((Long)currFieldValue).longValue();
                                    cellString = String.valueOf(rsLong);
                                }
                                else{
                                    cellString = "";
                                }
                            }
                            else if(displayType == PagerTypeConstant.DOUBLE)
                            {
                                if(currFieldValue != null){
                                    rsDouble = ((Double)currFieldValue).doubleValue();
                                    cellString = String.valueOf(rsDouble);
                                }
                                else{
                                    cellString = "";
                                }
                            }
                            else if(displayType == PagerTypeConstant.ARRAY)
                            {
                                if(currFieldValue != null){
                                    cellString = convertArrayToString(currFieldValue, ",");
                                }
                                else{
                                    cellString = "";
                                }
                            }
                            else if(displayType == PagerTypeConstant.FUNCTION)
                            {
                                argClass = baseInfo.getFnArgClass();
                                
                                if(currFieldValue != null)
                                {
                                    if(argClass[0].getName().equals(long.class.getName()))
                                    {
                                        rsLong = ((Long)currFieldValue).longValue();
                                        
                                        operateMethod =  baseInfo.getFnClazz().getMethod(baseInfo.getFnMethodName(), argClass);
                                        if(Modifier.isStatic(operateMethod.getModifiers())){
                                            methodObj = operateMethod.invoke(null, new Object[]{new Long(rsLong)});
                                        }
                                        else{
                                            methodObj = operateMethod.invoke(baseInfo.getFnClazz().newInstance(), new Object[]{new Long(rsLong)});
                                        }
                                        cellString = String.valueOf(methodObj);
                                    }
                                    else if(argClass[0].getName().equals(String.class.getName()))
                                    {
                                        cellString = (String)currFieldValue;
                                        
                                        operateMethod =  baseInfo.getFnClazz().getMethod(baseInfo.getFnMethodName(), argClass);
                                        if(Modifier.isStatic(operateMethod.getModifiers())){
                                            methodObj = operateMethod.invoke(null, new Object[]{new String(cellString)});
                                        }
                                        else{
                                            methodObj = operateMethod.invoke(baseInfo.getFnClazz().newInstance(), new Object[]{new String(cellString)});
                                        }
                                        cellString = String.valueOf(methodObj);
                                    }
                                }
                                else{
                                    cellString = "";
                                }
                            }
                        }
                        
                        cellList.add(cellString);
                        cellString = "";
                    }
                    
                    rowInfo = new ResultPagerRowInfo();
                    rowInfo.setCell(cellList);
                    rowInfo.setId(String.valueOf(rownum));
                    resultList.add(rowInfo);
                }           
                
            
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new Exception("转换结果集到JSON集合时异常", e);
        }
        
        return resultList;
    }
    
    public static String convertArrayToString(Object arrayObj, String split)
    {
        StringBuffer bufferString = new StringBuffer();
        
        if(arrayObj != null)
        {
            if(long[].class.equals(arrayObj.getClass()))
            {
                long[] arrayLong = (long[])arrayObj;
                
                for(int i=0; i<arrayLong.length; i++)
                {
                    bufferString.append(arrayLong[i] + split);
                }
            }
            else if(double[].class.equals(arrayObj.getClass()))
            {
                double[] arrayDouble = (double[])arrayObj;
                
                for(int i=0; i<arrayDouble.length; i++)
                {
                    bufferString.append(arrayDouble[i] + split);
                }
            }
            else if(String[].class.equals(arrayObj.getClass()))
            {
                String[] arrayString = (String[])arrayObj;
                
                for(int i=0; i<arrayString.length; i++)
                {
                    bufferString.append(arrayString[i] + split);
                }
            }
        }
        
        if(!bufferString.toString().equals(""))
        {
            bufferString = new StringBuffer(bufferString.toString().substring(0, bufferString.toString().length() - split.length()));
        }
        
        return bufferString.toString();
    }
    /**
     * CLOB转换成字符串基本实现
     */ 
    public static String convertClobToString(Clob clob) throws Exception
    {
        Reader reader = null;
        char[] ch = null;
        
        try
        {
            reader = (Reader)clob.getCharacterStream();
            ch = new char[(int)clob.length()]; 
            reader.read(ch);
            
            reader.close();
            reader = null;
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new Exception("CLOB转换成字符串时异常");
        } 
        finally
        {
            if(reader != null)
            {
                try {
                    reader.close();
                    reader = null;
                } catch (Exception e) {
                    e.printStackTrace();
                    throw new Exception("CLOB转换成字符串时异常");
                }
            }
        }
        return new String(ch);
    }
    
    public static String getJSONString(ResultPagerInfo resultPagerinfo)throws Exception
    {
        String strJSON = "";
        try
        {
            if(resultPagerinfo!=null)
            {
                JSONObject jsonObject = JSONObject.fromObject(resultPagerinfo);
                strJSON = jsonObject.toString();
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            throw new Exception("从结果集转换到JSON字符串失败!");
        }
        return strJSON;
    }
    
    public static List<Object> returnCellList(List<Object> cellList,Object _object)
    {
        String cellString = _object == null?"":String.valueOf(_object);
        cellList.add(cellString);
        return cellList;
    }
    public static List<Object> convertListMapToJSONList(PagerInfo pagerInfo,long startNum,String pfFlag) throws Exception{
        List<Object> list= pagerInfo.getResultList();
        Iterator<Object> it = list.iterator();
        int n = 0;
        while(it.hasNext()){
            n++;
            Object o = it.next();
            Map<String, String> content = BeanUtils.describe(o);
            convertMapToResultPagerRowInfo(pagerInfo,content,n);
        }
        return null;
    }
    private static ResultPagerRowInfo convertMapToResultPagerRowInfo(PagerInfo pagerInfo,Map<String,String> content,int id) throws Exception {
        ResultPagerRowInfo info = new ResultPagerRowInfo();
        info.setId(id+"");
        info.setCell(getDisplayDaTa(pagerInfo,content));
        return info;
    }
    private static List<Object> getDisplayDaTa(PagerInfo pagerInfo,Map<String,String> content) throws Exception {
        Iterator<Object> pagerIter = pagerInfo.getDepictList().iterator();
        List<Object> cellList = new ArrayList<Object>();
        PagerDepictBaseInfo baseInfo;
        String columnName ="";
        int type= -1;
        String cellString ;
        
        while(pagerIter.hasNext()){
            
            baseInfo =  (PagerDepictBaseInfo)pagerIter.next();
            
            columnName = baseInfo.getDisplayName();
            type = Long.valueOf(baseInfo.getDisplayType()+"").intValue();
            
            cellString = content.get(columnName)==null?"":content.get(columnName)+"";
            switch (type) {
                case (int) PagerTypeConstant.STRING:
                    if(cellString.indexOf(".")>0)
                    {
                        try{
                            Double.valueOf(cellString);
                            cellString = cellString.substring(0, cellString.indexOf("."));
                        }catch (Exception e) {
                        }
                    }       
                    break;
                case (int) PagerTypeConstant.AMOUNT_2:
                    if(!cellString.equals(""))
                    {
                        cellString = NumberUtil.formatAmount(Double.parseDouble(cellString), 2);
                    }
                    break;
                case (int) PagerTypeConstant.AMOUNT_6:
                    if(!cellString.equals(""))
                    {
                        cellString = NumberUtil.formatAmount(Double.parseDouble(cellString), 6);
                    }
                    break;
                case (int) PagerTypeConstant.LONG:
                    break;
                case (int) PagerTypeConstant.DOUBLE:
                        //保留1位小数
                        cellString = NumberUtil.formatAmount(Double.parseDouble(cellString), 1);
                    break;
                case (int) PagerTypeConstant.FUNCTION:
                        Class<?> []argClass = baseInfo.getFnArgClass();
                        long rsLong;
                        Method operateMethod;
                        Object methodObj ;
                if(argClass[0].getName().equals(long.class.getName()))
                {
                    rsLong = (long)Double.parseDouble(cellString);
                    
                    operateMethod =  baseInfo.getFnClazz().getMethod(baseInfo.getFnMethodName(), argClass);
                    if(Modifier.isStatic(operateMethod.getModifiers())){
                        methodObj = operateMethod.invoke(null,  new Object[]{new Long(rsLong)});
                    }
                    else{
                        methodObj = operateMethod.invoke(baseInfo.getFnClazz().newInstance(),  new Object[]{new Long(rsLong)});
                    }
                    cellString = String.valueOf(methodObj);
                }
                 if(argClass[0].getName().equals(String.class.getName()))
                {
                    operateMethod =  baseInfo.getFnClazz().getMethod(baseInfo.getFnMethodName(), argClass);
                    if(Modifier.isStatic(operateMethod.getModifiers())){
                        methodObj = operateMethod.invoke(null, new Object[]{new String(cellString)});
                    }
                    else{
                        methodObj = operateMethod.invoke(baseInfo.getFnClazz().newInstance(), new Object[]{new String(cellString)});
                    }
                    cellString = String.valueOf(methodObj);
                }
            
                    break;
            default:
                if(cellString.indexOf(".")>0)
                {
                    try{
                        Double.valueOf(cellString);
                        cellString = cellString.substring(0, cellString.indexOf("."));
                    }catch (Exception e) {
                    }
                }       
                break;
            }
        }
        return cellList;
    }
}


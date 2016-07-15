/**
 * 工程名称:SpringSharp
 * 文件名称:FlexiGridDao.java
 * 包名称： com.sharp.core.sysframe.dao
 * 创建时间: 2016年7月11日下午4:10:10
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.sharp.core.dao.BaseDao;
import com.sharp.core.sysframe.constant.PagerConstant;
import com.sharp.core.sysframe.pager.entity.PagerInfo;
import com.sharp.core.sysframe.pager.entity.ResultPagerInfo;
import com.sharp.core.sysframe.pager.tools.PagerTools;
import com.sharp.core.utils.NumberUtil;

public class FlexiGridDao extends BaseDao{

   
    /**
     * 方法名称：getResultList
     * 方法描述：查询结果列表带分页
     * 创建人： 邢凌霄
     * 创建时间：2016年7月11日 下午6:54:24
     * @param sql
     * @param map
     * @return
     * @throws Exception
     * @since JDK 1.5
     */
    public List<Object> getResultList(Map<String,String[]> map,PagerInfo pagerInfo) throws Exception
    {
        
        long page = Long.valueOf(map.get(PagerConstant.PAGE)[0]).longValue();
        long rp = Long.valueOf(map.get(PagerConstant.RP)[0]).longValue();
        String sortName = map.get(PagerConstant.SORT_NAME)[0];
        String sortOrder =map.get(PagerConstant.SORT_ORDER)[0];
        long startNum = (page-1)* rp;// 计算查询开始数据下标
        long finishNum = startNum + rp;   
        String sql = pagerInfo.getSqlString();
        
        StringBuffer strSQL = null;
        List<Object> returnList = null;
        SqlRowSet rowSet = null;
        
        //查询总记录
        strSQL = new StringBuffer();
        strSQL.append("select * from (");
        strSQL.append("  select t.*, rownum as rownum from (");
        strSQL.append("    select * from (" + sql + ")");
        if(sortName != null && !sortName.equals("")){
            strSQL.append("    order by "+ sortName + " " + sortOrder);
        }
        strSQL.append("  ) t where 1 = 1 " );
        strSQL.append(") where rownum > " + startNum + " and rownum <= " + finishNum);
        
        rowSet = this.jdbcTemplate.queryForRowSet(strSQL.toString());
        
        returnList = PagerTools.convertResultSetToJSONListEx(rowSet,pagerInfo);
          
        return returnList;
    }

    /**
     * 方法名称：getAllResultList
     * 方法描述：查询所有不带分页
     * 创建人： 邢凌霄
     * 创建时间：2016年7月11日 下午6:54:58
     * @param sql
     * @param map
     * @return
     * @throws Exception
     * @since JDK 1.5
     */
    public List<Object> getAllResultList(Map<String,String[]> map,PagerInfo pagerInfo) throws Exception
    {
        String sortName = (String) map.get(PagerConstant.SORT_NAME)[0];
        String sortOrder = (String) map.get(PagerConstant.SORT_ORDER)[0];
        String sql = pagerInfo.getSqlString();
        
        StringBuffer strSQL = null;
        List<Object> returnList = null;
        SqlRowSet rowSet = null;
   
        //查询总记录
        strSQL = new StringBuffer();
        strSQL.append("  select t.*, rownum as rownum from (");
        strSQL.append("    select * from (" + sql + ")");
        if(sortName != null && !sortName.equals("")){
            strSQL.append("    order by "+ sortName + " " + sortOrder);
        }
        strSQL.append("  ) t where 1 = 1 ");
        rowSet = this.jdbcTemplate.queryForRowSet(strSQL.toString());
        returnList = PagerTools.convertResultSetToJSONListEx(rowSet,pagerInfo);
        return returnList;
    }
    
    public ResultPagerInfo  getResultPagerInfo(Map<String,String[]> map,PagerInfo pagerInfo,List<Object> rowList) throws Exception{
        ResultPagerInfo resultPagerInfo = new ResultPagerInfo();
        long page = Long.valueOf(map.get(PagerConstant.PAGE)[0]).longValue();
        resultPagerInfo.setPage(page);
        resultPagerInfo.setTotal(this.getRowNum(pagerInfo.getSqlString()));
        resultPagerInfo.setUsertext(this.getUserText(pagerInfo, rowList));
        resultPagerInfo.setRowList(rowList);
        return resultPagerInfo;
    }
    
    
    
    /**
     * 方法名称：getUserText
     * 方法描述：获取合计信息，抽象出公用方法
     * 创建人： 邢凌霄
     * 创建时间：2016-6-6 下午10:35:06
     * @param pagerInfo
     * @return
     * @throws Exception
     * @since JDK 1.5
     */
    public  String getUserText(PagerInfo pagerInfo,List<Object> rowList) throws Exception {
        StringBuffer strSQL;
        List<Object> returnList = null;
        String strUsertext = null;//存放  pagerInfo中usertext的内容
        SqlRowSet rowSet = null;
        String sql = pagerInfo.getSqlString();
        try {
        //所有结果的合计统计
        
            //合计
            String[] singleTexts=null;//存放pagerInfo中usertext的内容按“;”分割后的结果集
            
            String displayName = "";//存放汇总的字段
            String m_sbWhere = "";//汇总的查询条件
            boolean formatAmount = true;//是否需要对金额格式化
                
            if( pagerInfo.getUsertext()!=null && !pagerInfo.getUsertext().equals("")){
                strUsertext = pagerInfo.getUsertext();
                if(strUsertext.indexOf("{")>0&&strUsertext.indexOf("}")>strUsertext.indexOf("{")){

                    singleTexts=strUsertext.split(";");
                    int singleTextNum=singleTexts.length; //存放pagerInfo中usertext的内容按“;”分割后的结果的数目;
                    
                    StringBuffer textBuffer= new StringBuffer(); 
                    
                    for(int i=0;i<singleTextNum;i++){
                        
                        displayName = singleTexts[i].substring(singleTexts[i].indexOf("{") + 1, singleTexts[i].indexOf("}"));
                        
                        m_sbWhere = "";
                        if(singleTexts[i].indexOf("[")>0&&singleTexts[i].indexOf("]")>singleTexts[i].indexOf("[")){
                            m_sbWhere = singleTexts[i].substring(singleTexts[i].indexOf("[") + 1, singleTexts[i].indexOf("]"));
                        }
                        
                        formatAmount = true;
                        if(singleTexts[i].indexOf("#")>0){
                            if(singleTexts[i].substring(singleTexts[i].indexOf("#") + 1, singleTexts[i].length()).equalsIgnoreCase("N")){
                                formatAmount = false;
                            }
                        }
                        double dataTotal = 0.00;
                        strSQL = new StringBuffer();
                        strSQL.append("select sum("+ displayName+") allSum_"+ i +" from ("+ sql +")" + m_sbWhere);
                        rowSet = this.jdbcTemplate.queryForRowSet(strSQL.toString());
                        
                        if (rowSet.next())
                        {
                            //用于存放汇总的结果
                            dataTotal = rowSet.getDouble("allSum_"+ i);
                            
                        }
                    
                        if(formatAmount){
                            singleTexts[i] = "<b>"+singleTexts[i].substring(0, singleTexts[i].indexOf("{"))+"</b>："+NumberUtil.formatAmount(dataTotal,2);
                        }else{
                            singleTexts[i] = "<b>"+singleTexts[i].substring(0, singleTexts[i].indexOf("{"))+"</b>："+dataTotal+"";
                        }
                                                    
                        if(i>1&&(i)%6==0){
                            textBuffer.append("<br>&nbsp;&nbsp;");
                        }else{
                            textBuffer.append("&nbsp;&nbsp;");
                        }
                        textBuffer.append(singleTexts[i]);
                    }
                    strUsertext = textBuffer.toString();
                }
                
            }
            /**
             * 对于自定义汇总的处理方式 add by xiangzhou 20120806
             */
            else if(pagerInfo.getExtTotalClazz()!=null && pagerInfo.getExtTotalMothodName()!=null){
                
                Class<?> clazz = pagerInfo.getExtTotalClazz();
                String methodName = pagerInfo.getExtTotalMothodName();
                
                Method method = null;
                Method _method[] = clazz.getMethods();
                
                for(int i=0;i<_method.length;i++){
                    if(_method[i].getName().equals(methodName)){
                        method = _method[i];
                        break;
                    }
                }
            
                if(Modifier.isStatic(method.getModifiers())){
                    if(pagerInfo.getTotalParams()!=null){
                        Object o =  method.invoke(null, new Object[]{rowList, pagerInfo.getTotalParams()});
                        returnList = new ArrayList<Object>();
                        returnList.add(o);
                    }else{
                        Object o = method.invoke(null, new Object[]{rowList});
                        returnList = new ArrayList<Object>();
                        returnList.add(o);
                    }
                }else{
                    if(pagerInfo.getTotalParams()!=null){
                        Object o = method.invoke(clazz.newInstance(), new Object[]{rowList, pagerInfo.getTotalParams()});
                        returnList = new ArrayList<Object>();
                        returnList.add(o);
                    }else{
                        Object o  = method.invoke(clazz.newInstance(), new Object[]{rowList});
                        returnList = new ArrayList<Object>();
                        returnList.add(o);
                    }
                }
                
                StringBuffer textBuffer= new StringBuffer(); 
                
                for(int i=0;i<returnList.size();i++){
                    
                    strUsertext = returnList.get(i).toString();
                    strUsertext = "<b>"+strUsertext.substring(0, strUsertext.indexOf("{"))+"</b>："+strUsertext.substring(strUsertext.indexOf("{")+1, strUsertext.indexOf("}"));
                    if(i>1&&(i)%pagerInfo.getRowShowNum()==0){
                        textBuffer.append("<br>&nbsp;&nbsp;");
                    }else{
                        textBuffer.append("&nbsp;&nbsp;");
                    }
                    textBuffer.append(strUsertext);
                }
                strUsertext = textBuffer.toString();
                
            }
            
        }
        catch (Exception e) {
            if( e instanceof InvocationTargetException) {
                
                InvocationTargetException e1 = (InvocationTargetException)e;
                throw new Exception(e1.getTargetException().getMessage());
            }else{
                e.printStackTrace();
                throw e;
            }
        }
        return strUsertext;
    }

    
    /**
     * 获取记录总条数
     * @param sql
     * @return
     * @throws Exception
     */
    public long getRowNum(String sql) throws Exception
    {
        long lRowNum = 0;
        StringBuffer strSQL = null;

        strSQL = new StringBuffer();
        strSQL.append(" select count(*) countNum from ("+ sql +")");
         
        lRowNum = this.jdbcTemplate.queryForLong(strSQL.toString());
        return lRowNum;
    }
    
    
}


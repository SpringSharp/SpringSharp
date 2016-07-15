/**
 * ��������:SpringSharp
 * �ļ�����:FlexiGridDao.java
 * �����ƣ� com.sharp.core.sysframe.dao
 * ����ʱ��: 2016��7��11������4:10:10
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
     * �������ƣ�getResultList
     * ������������ѯ����б����ҳ
     * �����ˣ� ������
     * ����ʱ�䣺2016��7��11�� ����6:54:24
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
        long startNum = (page-1)* rp;// �����ѯ��ʼ�����±�
        long finishNum = startNum + rp;   
        String sql = pagerInfo.getSqlString();
        
        StringBuffer strSQL = null;
        List<Object> returnList = null;
        SqlRowSet rowSet = null;
        
        //��ѯ�ܼ�¼
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
     * �������ƣ�getAllResultList
     * ������������ѯ���в�����ҳ
     * �����ˣ� ������
     * ����ʱ�䣺2016��7��11�� ����6:54:58
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
   
        //��ѯ�ܼ�¼
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
     * �������ƣ�getUserText
     * ������������ȡ�ϼ���Ϣ����������÷���
     * �����ˣ� ������
     * ����ʱ�䣺2016-6-6 ����10:35:06
     * @param pagerInfo
     * @return
     * @throws Exception
     * @since JDK 1.5
     */
    public  String getUserText(PagerInfo pagerInfo,List<Object> rowList) throws Exception {
        StringBuffer strSQL;
        List<Object> returnList = null;
        String strUsertext = null;//���  pagerInfo��usertext������
        SqlRowSet rowSet = null;
        String sql = pagerInfo.getSqlString();
        try {
        //���н���ĺϼ�ͳ��
        
            //�ϼ�
            String[] singleTexts=null;//���pagerInfo��usertext�����ݰ���;���ָ��Ľ����
            
            String displayName = "";//��Ż��ܵ��ֶ�
            String m_sbWhere = "";//���ܵĲ�ѯ����
            boolean formatAmount = true;//�Ƿ���Ҫ�Խ���ʽ��
                
            if( pagerInfo.getUsertext()!=null && !pagerInfo.getUsertext().equals("")){
                strUsertext = pagerInfo.getUsertext();
                if(strUsertext.indexOf("{")>0&&strUsertext.indexOf("}")>strUsertext.indexOf("{")){

                    singleTexts=strUsertext.split(";");
                    int singleTextNum=singleTexts.length; //���pagerInfo��usertext�����ݰ���;���ָ��Ľ������Ŀ;
                    
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
                            //���ڴ�Ż��ܵĽ��
                            dataTotal = rowSet.getDouble("allSum_"+ i);
                            
                        }
                    
                        if(formatAmount){
                            singleTexts[i] = "<b>"+singleTexts[i].substring(0, singleTexts[i].indexOf("{"))+"</b>��"+NumberUtil.formatAmount(dataTotal,2);
                        }else{
                            singleTexts[i] = "<b>"+singleTexts[i].substring(0, singleTexts[i].indexOf("{"))+"</b>��"+dataTotal+"";
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
             * �����Զ�����ܵĴ���ʽ add by xiangzhou 20120806
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
                    strUsertext = "<b>"+strUsertext.substring(0, strUsertext.indexOf("{"))+"</b>��"+strUsertext.substring(strUsertext.indexOf("{")+1, strUsertext.indexOf("}"));
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
     * ��ȡ��¼������
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


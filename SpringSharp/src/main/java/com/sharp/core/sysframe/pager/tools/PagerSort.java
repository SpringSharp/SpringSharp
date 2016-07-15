/**
 * 工程名称:SpringSharp
 * 文件名称:PagerSort.java
 * 包名称： com.sharp.core.sysframe.pager.tools
 * 创建时间: 2016年7月11日下午4:14:50
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.sharp.core.sysframe.constant.PagerConstant;
import com.sharp.core.sysframe.pager.entity.PagerInfo;
import com.sharp.core.sysframe.pager.entity.ResultInfo;
import com.sharp.core.utils.NumberUtil;

/**
 * 类名称: PagerSort
 * 类描述：分页排序
 * 创建时间: 2016年7月11日 下午4:15:34
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class PagerSort {
    
    
    /**
     * 对PagerInfo中的结果进行排序
     * @param sortName
     * @param sortOrder
     * @param firstNum
     * @param lastNum
     * @param pagerInfo
     * @throws Exception
     */
    public static void sortAllPagerInfo(Map<String,String[]> map, PagerInfo pagerInfo) throws Exception
    {
        String sortName = map.get(PagerConstant.SORT_NAME)[0];
        String sortOrder =map.get(PagerConstant.SORT_ORDER)[0];
        
        ResultInfo sourceInfo = pagerInfo.getResultInfo();
        PagerComparator pagerComparator = null;
        
        try
        {
            if(sourceInfo != null && sourceInfo.getResultList() != null && sourceInfo.getResultList().size() > 0)
            {
                pagerComparator = new PagerComparator(sortName);
                Collections.sort(sourceInfo.getResultList(), pagerComparator);
                
                if(sortOrder.equals("desc"))
                {
                    Collections.reverse(sourceInfo.getResultList());
                }

            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new Exception("对PagerInfo中的结果进行排序时异常", e);
        }
    }
    /**
     * 对PagerInfo中的结果进行排序
     * @param sortName
     * @param sortOrder
     * @param firstNum
     * @param lastNum
     * @param pagerInfo
     * @throws Exception
     */
    public static void sortPagerInfo(Map<String,String[]> map, PagerInfo pagerInfo) throws Exception
    {
        
        long page = Long.valueOf(map.get(PagerConstant.PAGE)[0]).longValue();
        long rp = Long.valueOf(map.get(PagerConstant.RP)[0]).longValue();
        String sortName = map.get(PagerConstant.SORT_NAME)[0];
        String sortOrder =map.get(PagerConstant.SORT_ORDER)[0];
        long startNum = (page-1)* rp;// 计算查询开始数据下标
        long finishNum = startNum + rp;   
        ResultInfo sourceInfo = pagerInfo.getResultInfo();
        PagerComparator pagerComparator = null;
        List<Object> subList = null;
        int fromIndex = (int) startNum;
        int endIndex = 0;
        
        try
        {
            if(sourceInfo != null && sourceInfo.getResultList() != null && sourceInfo.getResultList().size() > 0)
            {
                pagerComparator = new PagerComparator(sortName);
                Collections.sort(sourceInfo.getResultList(), pagerComparator);
                
                if(sortOrder.equals("desc"))
                {
                    Collections.reverse(sourceInfo.getResultList());
                }
                
                if(finishNum < sourceInfo.getResultList().size())
                {
                    endIndex = (int)finishNum;
                }
                else
                {
                    endIndex = sourceInfo.getResultList().size();
                }
                
                //所有结果的合计统计
                
                //合计
                String strUsertext = null;//存放  pagerInfo中usertext的内容
                String[] singleTexts=null;//存放pagerInfo中usertext的内容按“;”分割后的结果集
                    
                if( pagerInfo.getUsertext()!=null && !pagerInfo.getUsertext().equals("")){
                    strUsertext = pagerInfo.getUsertext();
                    if(strUsertext.indexOf("{")>0&&strUsertext.indexOf("}")>strUsertext.indexOf("{")){
    
                        singleTexts=strUsertext.split(";");
                        int singleTextNum=singleTexts.length; //存放pagerInfo中usertext的内容按“;”分割后的结果的数目;
                        
                        StringBuffer textBuffer= new StringBuffer(); 
                        
                        for(int i=0;i<singleTextNum;i++){
                            
                            double dataTotal = 0.00;
                            
                            singleTexts[i] = "<b>"+singleTexts[i].substring(0, singleTexts[i].indexOf("{"))+"</b><em>"+NumberUtil.formatAmount(dataTotal, 2)+"</em>";
                            if(i>1&&(i)%3==0){
                                textBuffer.append("<br>&nbsp;&nbsp;");
                            }else{
                                textBuffer.append("&nbsp;&nbsp;");
                            }
                            textBuffer.append(singleTexts[i]);
                        }

                        pagerInfo.setUsertext(textBuffer.toString());
                    }
                }
            
                subList  = sourceInfo.getResultList().subList((int)fromIndex, endIndex);
                sourceInfo.setResultList(subList);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new Exception("对PagerInfo中的结果进行排序时异常", e);
        }
    }
    
    /**
     * 对一个List中的结果进行排序
     * @param sortName
     * @param sortOrder
     * @param fromIndex
     * @param toIndex
     * @param list
     * @throws Exception
     */
    public static void sortList(String sortName, String sortOrder, long fromIndex, long toIndex, List<Object> list) throws Exception
    {
        PagerComparator pagerComparator = null;
        List<Object> subList = null;
        
        int endIndex = 0;
        
        try
        {
            if(list != null && list.size() > 0)
            {
                pagerComparator = new PagerComparator(sortName);
                Collections.sort(list, pagerComparator);
                
                if(sortOrder.equals("desc"))
                {
                    Collections.reverse(list);
                }
                
                if(toIndex < list.size())
                {
                    endIndex = (int)toIndex;
                }
                else
                {
                    endIndex = list.size();
                }
                
                subList  = list.subList((int)fromIndex, endIndex);
                list = new ArrayList<Object>(subList);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new Exception("对PagerInfo中的结果进行排序时异常", e);
        }
    }

}


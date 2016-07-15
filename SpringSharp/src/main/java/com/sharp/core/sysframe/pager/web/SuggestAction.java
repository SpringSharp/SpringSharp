/**
 * 工程名称:SpringSharp
 * 文件名称:SuggestAction.java
 * 包名称： com.sharp.core.sysframe.web
 * 创建时间: 2016年7月11日下午4:07:37
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.sharp.core.sysframe.constant.PagerTypeConstant;
import com.sharp.core.sysframe.pager.entity.PagerDepictBaseInfo;
import com.sharp.core.sysframe.pager.entity.PagerInfo;

public class SuggestAction {
    public PagerInfo doQuery(Map<String,String[]> map) throws Exception{
        PagerInfo pagerInfo = new PagerInfo();

        String colmodel = (String) map.get("colModel")[0];
        String colmodeltype = (String) map.get("colModelType")[0];
        String colmodellink = (String) map.get("colModelLink")[0];
        String elementNames = (String) map.get("elementNames")[0];
        String elements = (String) map.get("elements")[0];
        String elementstype = (String) map.get("elementsType")[0];
        String form = (String) map.get("form")[0];
        String name = (String) map.get("name")[0];
        String sql = (String) map.get("sql")[0];
        String query = (String) map.get("query")[0];
        String qtype = (String) map.get("qtype")[0];
        String nextfocus = (String) map.get("nextFocus")[0];
        String callback = (String) map.get("callback")[0];

        String clazzs = (String) map.get("clazzs")[0];
        String methods = (String) map.get("methods")[0];
        String paramTypes = (String) map.get("paramTypes")[0];
        String paramValues = (String) map.get("paramValues")[0];
        //拼装SQL语句
        if ((query != null && !query.equals(""))
                && (qtype != null && !qtype.equals(""))) {
            sql = "select * from (" + sql + ") where 1=1";
            sql += " and " + qtype + " like '%" + query + "%'";
        }

        // 置入SQL语句
        pagerInfo.setSqlString(sql);

        // 生成页面脚本代码
        String[] elementNamesList = elementNames.split(",");
        StringBuffer elBuffer = new StringBuffer();
        elBuffer.append("[");
        for (int i = 0; i < elementNamesList.length; i++) {
            elBuffer.append("{name:'" + elementNamesList[i] + "',value:'"+PagerTypeConstant.LOGOTYPE+"'},");
        }
        elBuffer = new StringBuffer(elBuffer.toString().substring(0,
                elBuffer.toString().length() - 1)
                + "]");

        // 窗口类型数组
        String[] strColmodeltype = colmodeltype.split(",");
        long[] lColmodeltype = new long[strColmodeltype.length];
        for (int j = 0; j < strColmodeltype.length; j++) {
            lColmodeltype[j] = Long.parseLong(strColmodeltype[j]);
        }


        // 生成页面配置信息
        List<Object> depictList = new ArrayList<Object>();
        PagerDepictBaseInfo baseInfo = null;
        String[] paramValues_ = paramValues.split(",");
        String[] clazzs_ = clazzs.split(",");
        String[] methods_ = methods.split(",");
        String[] paramTypes_ = paramTypes.split(",");
        String[] colmodels = colmodel.split(",");
        for (int i = 0; i < colmodels.length; i++) {
            if ((colmodellink == null || colmodellink.equals("") || colmodellink
                    .equalsIgnoreCase("null"))
                    || (colmodellink != null && !colmodellink.equals("") && colmodellink
                            .indexOf(colmodels[i]) >= 0)) {// 带链接的字段
                baseInfo = new PagerDepictBaseInfo();
                baseInfo.setDisplayName(colmodels[i]);
                baseInfo.setDisplayType(lColmodeltype[i]);
                baseInfo.setExtension(true);
                baseInfo.setExtensionName(elements.concat(",").concat(colmodels[i]).split(","));

                // 类型数组
                String[] strElementstypes = elementstype.concat(",").concat(new Long(lColmodeltype[i]).toString()).split(",");
                long[] lElementstypes = new long[strElementstypes.length];
                for (int j = 0; j < strElementstypes.length; j++) {
                    lElementstypes[j] = Long.parseLong(strElementstypes[j]);
                }
                baseInfo.setExtensionType(lElementstypes);
                if(PagerTypeConstant.FUNCTION==lColmodeltype[i])
                    baseInfo.setExtensionMothod(Class.forName(clazzs_[i]), methods_[i], new Class[]{this.getTypeClass(paramTypes_[i])});

                baseInfo.setExtensionString("<a href=\"javascript:"
                        + "$.suggest.fillForm({" + "form:'" + form + "',name:'"
                        + name + "',el:" + elBuffer.toString()
                        + ",nextFocus : '" + nextfocus + "',callback:'"
                        + callback + "'})\">"+PagerTypeConstant.LOGOTYPE+"</a>");
                depictList.add(baseInfo);
            } else {
                baseInfo = new PagerDepictBaseInfo();
                baseInfo.setDisplayName(colmodels[i]);
                if(lColmodeltype[i]==PagerTypeConstant.FUNCTION){
                    // 类型数组
                    String[] strElementstypes = elementstype.concat(",").concat(new Long(lColmodeltype[i]).toString()).split(",");
                    long[] lElementstypes = new long[strElementstypes.length];
                    for (int j = 0; j < strElementstypes.length; j++) {
                        lElementstypes[j] = Long.parseLong(strElementstypes[j]);
                    }
                    baseInfo.setExtensionType(lElementstypes);
                    baseInfo.setDisplayName(paramValues_[i]);
                    baseInfo.setDisplayType(PagerTypeConstant.FUNCTION);
                    baseInfo.setExtensionMothod(Class.forName(clazzs_[i]), methods_[i], new Class[]{this.getTypeClass(paramTypes_[i])});
                }else
                    baseInfo.setDisplayType(lColmodeltype[i]);
                depictList.add(baseInfo);
            } 
        }
        
        // 置入配置信息
        pagerInfo.setDepictList(depictList);
        return pagerInfo;
    }
    private Class<?> getTypeClass(String type) throws ClassNotFoundException {
        Class<?> clazz;
        try {
            clazz = Class.forName("java.lang."+type);
        } catch (ClassNotFoundException e) {
            if(type.equals("int")){
                return int.class;
            }else if(type.equals("double")){
                return double.class;
            }else if(type.equals("long")){
                return long.class;
            }else if(type.equals("short")){
                return short.class;
            }else{
                throw e;
            }
        }
        return clazz;
    }
}


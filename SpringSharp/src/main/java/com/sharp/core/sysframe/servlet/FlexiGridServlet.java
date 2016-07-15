/**
 * 工程名称:SpringSharp
 * 文件名称:AjaxServlet.java
 * 包名称： com.sharp.core.servlet
 * 创建时间: 2016年7月11日下午3:52:30
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.apache.log4j.Logger;

import com.sharp.core.sysframe.constant.PagerConstant;
import com.sharp.core.sysframe.pager.dao.FlexiGridDao;
import com.sharp.core.sysframe.pager.entity.PagerInfo;
import com.sharp.core.sysframe.pager.entity.ResultPagerInfo;
import com.sharp.core.sysframe.pager.tools.PagerSort;
import com.sharp.core.sysframe.pager.tools.PagerTools;
import com.sharp.core.sysframe.pager.web.SuggestAction;

/**
 * 类名称: AjaxServlet
 * 类描述：捕捉FlexiGrid请求Servlet
 * 创建时间: 2016年7月11日 下午3:53:43
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class FlexiGridServlet extends HttpServlet{

    /**
     * serialVersionUID:流水号
     * @since JDK 1.5
     */
    private static final long serialVersionUID = 6336189851794756016L;
    

    private Logger logger = Logger.getLogger(FlexiGridServlet.class);
    
    static final private String strClassMethod = "strClassMethod";
    static final private String strAutoJson = "strAutoJson";
    static final private String strParseXml = "strParseXml";
    static final private String strUsePager = "strUsePager";
    static final private String strIsEncode = "strIsEncode";
    static final private String strIsPrint = "strIsPrint";
    static final private String strIsExcel = "strIsExcel";
    static final private String isDraftDetil = "isDraftDetil";
    static final private String isExportDetail = "isExportDetail";
    static final private String istag = "istag";
    static final private String isBackExportDetail = "isBackExportDetail";
    static final private String isdraftcode = "isdraftcode";
    static final private String isdisplayDate = "isdisplayDate";
    
    String autoJson = null;
    String parseXml = null;
    String usePager = null;
    String isEncode = null;
    String isPrint = null;
    String isExcel = null;
    String DraftDetil = null;
    String ExportDetail =null;
    String tag =null;
    String backExprot =null;
    String draftcode =null;
    String displayDate =null;
    
    /**
     * Creates a new instance of AjaxServlet.
     *
     */
    public FlexiGridServlet()
    {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy()
    {
        super.destroy(); 
    }

    /**
     * Servlet的doGet方法，获取get请求
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * Servlet的doPost方法，获取post请求
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String[] classMethods = null;
        
        // 将request里的所有内容转化为map
        Map<Object,Object> queryMap = new HashMap<Object,Object>();
        Map<Object,Object> initMap = new HashMap<Object,Object>();
        
        Map<String, String[]> parameterMap = null;
        String _name = null;
        Object _objValue = null;
        String[] _strValue = null;
        
        try
        {
            classMethods = request.getParameterValues(strClassMethod);// 类方法路径
            autoJson = request.getParameter(strAutoJson);
            parseXml = request.getParameter(strParseXml);
            usePager = request.getParameter(strUsePager);
            isEncode = request.getParameter(strIsEncode);
            isPrint = request.getParameter(strIsPrint);
            isExcel = request.getParameter(strIsExcel);
            DraftDetil = request.getParameter(isDraftDetil);
            ExportDetail=request.getParameter(isExportDetail);
            
            tag=request.getParameter(istag);
            backExprot =request.getParameter(isBackExportDetail);
            draftcode=request.getParameter(isdraftcode);
            displayDate=request.getParameter(isdisplayDate);
        }
        catch(Exception e)
        {
            logger.debug("接收到传入的字串符解析错误");
            e.printStackTrace();
            return;
        }
        
        
        try
        {
            parameterMap = request.getParameterMap();
            Iterator<String> iter = parameterMap.keySet().iterator();
            
            while (iter.hasNext())
            {
                _name = (String) iter.next();
                _objValue = parameterMap.get(_name);
                
                if(_objValue.getClass().getName().equals(String[].class.getName()))
                {
                    _strValue = (String[])_objValue;
                    
                    if(_strValue.length > 1)
                    {
                        if(isEncode != null && isEncode.equalsIgnoreCase("true"))
                        {
                            for(int i = 0; i < _strValue.length; i ++)
                            {
                                _strValue[i] = URLDecoder.decode(_strValue[i], "UTF-8");
                            }
                        }
                        queryMap.put(_name.toLowerCase(), _strValue);
                    }
                    else
                    {
                        if(isEncode != null && isEncode.equalsIgnoreCase("true"))
                        {
                            queryMap.put(_name.toLowerCase(), URLDecoder.decode(_strValue[0], "UTF-8"));
                        }
                        else
                        {
                            queryMap.put(_name.toLowerCase(), _strValue[0]);
                        }
                    }
                }
                else
                {
                    logger.debug("接收到传入的非正常数据类型");
                    logger.debug(_objValue.getClass().getName());
                }
            }
        }
        catch(Exception e)
        {
            logger.debug("接收到传入的字串符解析错误");
            e.printStackTrace();
            return;
        }

        //如果存在提交处理函数
        if(classMethods!=null && classMethods.length>0)
        {
            //执行提交处理函数
            try {
                executeClassMethods(request, response, queryMap, initMap, classMethods);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                this.responseWriter(response,"error:"+e.getMessage());
            }
        }
    }
    
    /**
     * 执行提交、成功、失败处理方法
     * 该处理方法将request需要设置的值已经置入
     * @param request
     * @param queryMap
     * @param classMethods
     * @return
     * @throws Exception 
     */
    /**
     * 方法名称：executeClassMethods
     * 方法描述：(这里用一句话描述这个方法的作用)
     * 创建人： 邢凌霄
     * 创建时间：2016年7月12日 下午2:07:13
     * @param request
     * @param response
     * @param queryMap
     * @param initMap
     * @param classMethods
     * @throws Exception
     * @since JDK 1.5
     */
    private void executeClassMethods(HttpServletRequest request, HttpServletResponse response, Map<Object,Object> queryMap, Map<Object,Object> initMap, String[] classMethods) throws Exception
    {
        String methodName = null;
        Class<?> actionClazz = null;
        Object actionObj = null;
        Class<?>[] classList = null;
        Method execMethod = null;
        
        Object returnObj = null;
        
        if(classMethods[0].equals("")){
            
            try {
                queryData(request, response);//放大镜
            } catch (ServletException e) {
                e.printStackTrace();
            }
        
        }else{
                
            for(int i=0; i<classMethods.length; i++)
            {
                
                try {
                    methodName = classMethods[i].substring(classMethods[i].lastIndexOf(".") + 1, classMethods[i].length());//取得方法名
                    actionClazz = Class.forName(classMethods[i].substring(0, classMethods[i].lastIndexOf(".")));//得到被调用类的Class对象
                    actionObj = actionClazz.newInstance();
                }
                catch(Exception e)
                {
                    logger.debug("接收到传入的字串符解析错误");
                    logger.debug("执行方法：" + classMethods[i]);
                    e.printStackTrace();
                    throw e;
                }           
                try {
                    
                    classList = new Class[]{Map.class};
                    execMethod = actionClazz.getMethod(methodName, classList); //得到被调用方法的method对象
                    returnObj = execMethod.invoke(actionObj, new Object[]{queryMap}); //调用执行方法，得到结果
                    
                    /**
                     * 直接将LIST、INFO类等转化为JSON对象
                     */
                    if (autoJson != null && autoJson.equalsIgnoreCase("true"))
                    {
                        //使用json返回串
                        if(returnObj instanceof List)
                        {
                            this.responseWriter(response, JSONArray.fromObject(returnObj).toString());
                        }
                        
                        //使用json返回串
                        if(returnObj instanceof Map)
                        {
                            this.responseWriter(response, JSONArray.fromObject(returnObj).toString());
                        }
                        
                        //使用json返回串
                        if(returnObj instanceof String)
                        {
                            this.responseWriter(response, (String)returnObj);
                        }
                    } 
                    
                    /**
                     * 将XML解析成为JSON对象
                     */
                    else if (parseXml != null &&  parseXml.equalsIgnoreCase("true"))
                    {
                        //将XML解析成为JSON对象
                        if(returnObj instanceof String)
                        {
                            XMLSerializer serializer = new XMLSerializer();
                            JSON json = serializer.read((String)returnObj);
                            this.responseWriter(response, json.toString());
                        }
                    } 
                    
                    /**
                     * 进行分页查询
                     */
                    else if (returnObj instanceof PagerInfo)
                    {
                        ResultPagerInfo resultPagerInfo = new ResultPagerInfo();
                        PagerInfo pagerInfo = (PagerInfo)returnObj;
                        List<Object> rowList = null;  //查询列表信息
                        Map<String, String[]> paramMap = new HashMap<String, String[]>();
                        paramMap = request.getParameterMap();
                        
                        //是否分页
                        if(usePager != null && usePager.equalsIgnoreCase("true"))
                        {
        
                            //返回的是SQL语句
                            if(pagerInfo.getSqlString() != null && !pagerInfo.getSqlString().equals(""))
                            {
                                FlexiGridDao fgDao = null;
                                
                                try
                                {
                                    fgDao = new FlexiGridDao();
                            
                                    if("all".equals(request.getParameter("pageChoose"))){
                                        rowList = fgDao.getAllResultList(paramMap,pagerInfo);
    
                                    }else{
                                        rowList = fgDao.getResultList(paramMap,pagerInfo);
                                    }
                                    resultPagerInfo = fgDao.getResultPagerInfo(paramMap,pagerInfo,rowList);
                                }
                                catch(Exception e)
                                {
                                    logger.debug("处理分页时产生异常");
                                    throw e;
                                }
        
                            }
                            //返回的是结果集
                            else if(pagerInfo.getResultInfo() != null)
                            {
                                //对List结果进行排序
                                if("all".equals(request.getParameter("pageChoose"))){
                                    PagerSort.sortAllPagerInfo(paramMap, pagerInfo);    
                                }else{
                                    PagerSort.sortPagerInfo(paramMap, pagerInfo);  
                                }
                                rowList = PagerTools.convertResultInfoToJSONList(pagerInfo,0);
                                resultPagerInfo.setPage(Long.valueOf(paramMap.get(PagerConstant.PAGE)[0]).longValue());
                                resultPagerInfo.setTotal(pagerInfo.getResultInfo().getTotal());
                                resultPagerInfo.setRowList(rowList);
                                resultPagerInfo.setUsertext(pagerInfo.getUsertext());
                            }
                            
                        
                        }else{
                            
                            if(pagerInfo.getResultList()!=null){
                                PagerSort.sortAllPagerInfo(paramMap,pagerInfo);    
                                resultPagerInfo.setRowList(pagerInfo.getResultList());
                                resultPagerInfo.setUsertext(pagerInfo.getUsertext());
                            }
                            //返回的是SQL语句
                            else if(pagerInfo.getSqlString()!= null && !pagerInfo.getSqlString().equals(""))
                            {
                                FlexiGridDao fgDao = null;
                                
                                try
                                {
                                    fgDao = new FlexiGridDao();
                                
                                     rowList = fgDao.getAllResultList(paramMap, pagerInfo);
                                     resultPagerInfo = fgDao.getResultPagerInfo(paramMap,pagerInfo,rowList);
                                }
                                catch(Exception e)
                                {
                                    logger.debug("处理分页时产生异常");
                                    e.printStackTrace();
                                    return;
                                }
        
                            }
                            //返回的是结果集
                            else if(pagerInfo.getResultInfo() == null)
                            {

                                PagerSort.sortAllPagerInfo(paramMap, pagerInfo);    
                                rowList = PagerTools.convertResultInfoToJSONList(pagerInfo,0);
                                resultPagerInfo.setTotal(1);
                                resultPagerInfo.setRowList(rowList);
                                resultPagerInfo.setUsertext(pagerInfo.getUsertext());
                            }
                            
                        }
                        
                         //使用json返回串
                        JSONObject jsonObject = JSONObject.fromObject(resultPagerInfo);
                        this.responseWriter(response, jsonObject.toString());
                    }else{
                        String strMessage = "";//sessionMng.getActionMessages().toString();
    
                        if(strMessage != null && !strMessage.equals(""))
                        {
                            this.responseWriter(response, "$.Message.setData('','" + strMessage + "');");
                        }
                        else if(returnObj != null && returnObj instanceof String)
                        {
                            this.responseWriter(response, (String)returnObj);
                        }
                    }
                } 
                catch (InvocationTargetException e)
                {
                    
                    e.printStackTrace();
                    this.responseWriter(response,e.getTargetException().getMessage());
                } 
                catch (Exception e)
                {
                    logger.debug("处理AJAX请求时产生异常");
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }
    
    /**
     * 通用的response writer方法
     * @param response
     * @param strInfo
     */
    public void responseWriter(HttpServletResponse response, String strInfo)
    {
        Writer writer = null;
        try
        {
            response.setContentType("text/html; charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control","no-cache, must-revalidate");
            
            writer = response.getWriter();
            logger.debug("AJAX请求：" + strInfo);
            
            writer.write(strInfo);
            writer.flush();
            writer.close();
            writer = null;
        }
        catch(Exception e)
        {
            logger.debug("response向客户端返回消息时异常");
            logger.debug("消息：" + strInfo);
            e.printStackTrace();
        }
        finally
        {
            if(writer != null)
            {
                try {
                    writer.flush();
                    writer.close();
                    writer = null;
                } catch (IOException e) {
                    logger.debug("response向客户端返回消息时异常");
                    logger.debug("消息：" + strInfo);
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Initialization of the servlet. <br>
     * 
     * @throws ServletException
     *             if an error occurs
     */
    public void init() throws ServletException
    {
        super.init();
    }
    
    public void queryData(HttpServletRequest request, HttpServletResponse response) throws ServletException
    {
        try
        {
            List<Object> rowList = new ArrayList<Object>();
            FlexiGridDao fgDao = new FlexiGridDao();
            SuggestAction suggestAction = new SuggestAction();
            Map<String,String[]> queryMap = this.getData(request);
            PagerInfo pagerInfo = suggestAction.doQuery(queryMap);    
            
            if(pagerInfo.getSqlString() != null && !pagerInfo.getSqlString().equals(""))
            {
                rowList = fgDao.getResultList(queryMap,pagerInfo);
            }else
            {
                throw new Exception("获取查询语句失败!");
            }
            
            long page = Integer.parseInt((String)queryMap.get("page")[0]);
            
            ResultPagerInfo resultPagerInfo = new ResultPagerInfo();
            resultPagerInfo.setPage(page);
            resultPagerInfo.setTotal(fgDao.getRowNum(queryMap.get("sql").toString()));
            resultPagerInfo.setRowList(rowList);       
            String strJSON = PagerTools.getJSONString(resultPagerInfo);
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out =response.getWriter();
            out.print(strJSON);
            
        }catch(Exception e)
        {
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
    
    public Map<String,String[]> getData(HttpServletRequest request) throws ServletException
    {
        Map<String,String[]> queryMap = new HashMap<String,String[]>(); 
        try
        {
            queryMap = request.getParameterMap();

        }catch(Exception e)
        {
            e.printStackTrace();
            throw new ServletException("获取数据失败!");
        }
        return queryMap;
    }
    

    
    
    
    
    
    
    

}


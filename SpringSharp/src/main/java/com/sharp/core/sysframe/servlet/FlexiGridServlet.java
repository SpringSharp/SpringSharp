/**
 * ��������:SpringSharp
 * �ļ�����:AjaxServlet.java
 * �����ƣ� com.sharp.core.servlet
 * ����ʱ��: 2016��7��11������3:52:30
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
 * ������: AjaxServlet
 * ����������׽FlexiGrid����Servlet
 * ����ʱ��: 2016��7��11�� ����3:53:43
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class FlexiGridServlet extends HttpServlet{

    /**
     * serialVersionUID:��ˮ��
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
     * Servlet��doGet��������ȡget����
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }

    /**
     * Servlet��doPost��������ȡpost����
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        String[] classMethods = null;
        
        // ��request�����������ת��Ϊmap
        Map<Object,Object> queryMap = new HashMap<Object,Object>();
        Map<Object,Object> initMap = new HashMap<Object,Object>();
        
        Map<String, String[]> parameterMap = null;
        String _name = null;
        Object _objValue = null;
        String[] _strValue = null;
        
        try
        {
            classMethods = request.getParameterValues(strClassMethod);// �෽��·��
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
            logger.debug("���յ�������ִ�����������");
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
                    logger.debug("���յ�����ķ�������������");
                    logger.debug(_objValue.getClass().getName());
                }
            }
        }
        catch(Exception e)
        {
            logger.debug("���յ�������ִ�����������");
            e.printStackTrace();
            return;
        }

        //��������ύ������
        if(classMethods!=null && classMethods.length>0)
        {
            //ִ���ύ������
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
     * ִ���ύ���ɹ���ʧ�ܴ�����
     * �ô�������request��Ҫ���õ�ֵ�Ѿ�����
     * @param request
     * @param queryMap
     * @param classMethods
     * @return
     * @throws Exception 
     */
    /**
     * �������ƣ�executeClassMethods
     * ����������(������һ�仰�����������������)
     * �����ˣ� ������
     * ����ʱ�䣺2016��7��12�� ����2:07:13
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
                queryData(request, response);//�Ŵ�
            } catch (ServletException e) {
                e.printStackTrace();
            }
        
        }else{
                
            for(int i=0; i<classMethods.length; i++)
            {
                
                try {
                    methodName = classMethods[i].substring(classMethods[i].lastIndexOf(".") + 1, classMethods[i].length());//ȡ�÷�����
                    actionClazz = Class.forName(classMethods[i].substring(0, classMethods[i].lastIndexOf(".")));//�õ����������Class����
                    actionObj = actionClazz.newInstance();
                }
                catch(Exception e)
                {
                    logger.debug("���յ�������ִ�����������");
                    logger.debug("ִ�з�����" + classMethods[i]);
                    e.printStackTrace();
                    throw e;
                }           
                try {
                    
                    classList = new Class[]{Map.class};
                    execMethod = actionClazz.getMethod(methodName, classList); //�õ������÷�����method����
                    returnObj = execMethod.invoke(actionObj, new Object[]{queryMap}); //����ִ�з������õ����
                    
                    /**
                     * ֱ�ӽ�LIST��INFO���ת��ΪJSON����
                     */
                    if (autoJson != null && autoJson.equalsIgnoreCase("true"))
                    {
                        //ʹ��json���ش�
                        if(returnObj instanceof List)
                        {
                            this.responseWriter(response, JSONArray.fromObject(returnObj).toString());
                        }
                        
                        //ʹ��json���ش�
                        if(returnObj instanceof Map)
                        {
                            this.responseWriter(response, JSONArray.fromObject(returnObj).toString());
                        }
                        
                        //ʹ��json���ش�
                        if(returnObj instanceof String)
                        {
                            this.responseWriter(response, (String)returnObj);
                        }
                    } 
                    
                    /**
                     * ��XML������ΪJSON����
                     */
                    else if (parseXml != null &&  parseXml.equalsIgnoreCase("true"))
                    {
                        //��XML������ΪJSON����
                        if(returnObj instanceof String)
                        {
                            XMLSerializer serializer = new XMLSerializer();
                            JSON json = serializer.read((String)returnObj);
                            this.responseWriter(response, json.toString());
                        }
                    } 
                    
                    /**
                     * ���з�ҳ��ѯ
                     */
                    else if (returnObj instanceof PagerInfo)
                    {
                        ResultPagerInfo resultPagerInfo = new ResultPagerInfo();
                        PagerInfo pagerInfo = (PagerInfo)returnObj;
                        List<Object> rowList = null;  //��ѯ�б���Ϣ
                        Map<String, String[]> paramMap = new HashMap<String, String[]>();
                        paramMap = request.getParameterMap();
                        
                        //�Ƿ��ҳ
                        if(usePager != null && usePager.equalsIgnoreCase("true"))
                        {
        
                            //���ص���SQL���
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
                                    logger.debug("�����ҳʱ�����쳣");
                                    throw e;
                                }
        
                            }
                            //���ص��ǽ����
                            else if(pagerInfo.getResultInfo() != null)
                            {
                                //��List�����������
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
                            //���ص���SQL���
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
                                    logger.debug("�����ҳʱ�����쳣");
                                    e.printStackTrace();
                                    return;
                                }
        
                            }
                            //���ص��ǽ����
                            else if(pagerInfo.getResultInfo() == null)
                            {

                                PagerSort.sortAllPagerInfo(paramMap, pagerInfo);    
                                rowList = PagerTools.convertResultInfoToJSONList(pagerInfo,0);
                                resultPagerInfo.setTotal(1);
                                resultPagerInfo.setRowList(rowList);
                                resultPagerInfo.setUsertext(pagerInfo.getUsertext());
                            }
                            
                        }
                        
                         //ʹ��json���ش�
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
                    logger.debug("����AJAX����ʱ�����쳣");
                    e.printStackTrace();
                    throw e;
                }
            }
        }
    }
    
    /**
     * ͨ�õ�response writer����
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
            logger.debug("AJAX����" + strInfo);
            
            writer.write(strInfo);
            writer.flush();
            writer.close();
            writer = null;
        }
        catch(Exception e)
        {
            logger.debug("response��ͻ��˷�����Ϣʱ�쳣");
            logger.debug("��Ϣ��" + strInfo);
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
                    logger.debug("response��ͻ��˷�����Ϣʱ�쳣");
                    logger.debug("��Ϣ��" + strInfo);
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
                throw new Exception("��ȡ��ѯ���ʧ��!");
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
            throw new ServletException("��ȡ����ʧ��!");
        }
        return queryMap;
    }
    

    
    
    
    
    
    
    

}


<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@ page import="com.sharp.module.auth.web.UserAction" %>
<%@ page import="com.sharp.module.auth.entity.User" %>
<%@page import="com.sharp.core.style.Html"%>
<jsp:useBean id="sessionMng" scope="session" class="com.sharp.core.utils.SessionMng"></jsp:useBean>
<%
    String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'c001.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">

  </head>
<%
    Html.validateRequest(out,request,response);
	Html.showHomeHead(out,"测试页面");
	long lId = -1;
	UserAction action = new UserAction();
	User user = new User();
	user.convertRequestToDataEntity(request);
	//System.out.println(user.toString());
	try{
	}catch(Exception e){
		e.printStackTrace();
	}finally{
		if(lId>0){
			sessionMng.getActionMessages().addMessage("保存成功");
		}else{
			sessionMng.getActionMessages().addMessage("保存失败");
		}
	}
	
	String sNextPageURL = "../view/v001-user-input.jsp";
RequestDispatcher rd = request.getRequestDispatcher(sNextPageURL);
rd.forward( request,response );
%>




</html>

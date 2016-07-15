<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@page import="com.sharp.core.style.Html"%>
<%@page import="com.sharp.core.sysframe.pager.entity.FlexiGridInfo"%>
<jsp:include page="/page/common/ShowMessage.jsp"/>
<jsp:useBean id="sessionMng" scope="session" class="com.sharp.core.utils.SessionMng"></jsp:useBean>
<%
    String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Html.showHomeHead(out,"测试");
FlexiGridInfo flexiGridInfo = new FlexiGridInfo();
%>
  
  <form name = "form" id = "form"  method = "post">
	   <TABLE >
		<TBODY>
			<tr>
				<TD>&nbsp;</TD>
				<TD>
					<TABLE  id="flexlist"></TABLE>
				</TD>
				<TD >&nbsp;</TD>
			</tr>
		</TBODY>
		</TABLE>
   </form>
<script >
//列表初始化
	$(document).ready(function() {
		$("#flexlist").flexigridenc({
		colModel : [
			{display: '主键',  name : 'lId', width : 100, sortable : true, align: 'center'},
			{display: '登录名',  name : 'sLoginName', width :100, sortable : true, align: 'center'},
			{display: '密码',  name : 'sLoginPassword', width :100, sortable : true, align: 'center'}
			
		],//列参数
		classMethod : 'com.sharp.module.auth.web.UserAction.queryUser',//要调用的方法
		page : <%=flexiGridInfo.getFlexigrid_page()%>,
		rp : <%=flexiGridInfo.getFlexigrid_rp()%>,
		sortname : '<%=flexiGridInfo.getFlexigrid_sortname().equals("") == true ? "lId" : flexiGridInfo.getFlexigrid_sortname()%>',// 预设指定哪列进行排序
		sortorder : '<%=flexiGridInfo.getFlexigrid_sortorder().equals("") == true ? "asc" : flexiGridInfo.getFlexigrid_sortorder()%>',// 预设排序的方式
		userFunc : getFormData
	});
});

//填充
function getFormData() 
{
	$.addFormData("form","flexlist");
	
	return true;
	
}
//查询
function doQuery()
{	
	$.gridReload("flexlist");
}
</script>
<%@ page language="java" import="java.util.*" pageEncoding="GBK"%>
<%@page import="com.sharp.core.style.Html"%>
<%@page import="com.sharp.core.sysframe.pager.entity.FlexiGridInfo"%>
<jsp:include page="/page/common/ShowMessage.jsp"/>
<jsp:useBean id="sessionMng" scope="session" class="com.sharp.core.utils.SessionMng"></jsp:useBean>
<%
    String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
Html.showHomeHead(out,"����");
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
//�б��ʼ��
	$(document).ready(function() {
		$("#flexlist").flexigridenc({
		colModel : [
			{display: '����',  name : 'lId', width : 100, sortable : true, align: 'center'},
			{display: '��¼��',  name : 'sLoginName', width :100, sortable : true, align: 'center'},
			{display: '����',  name : 'sLoginPassword', width :100, sortable : true, align: 'center'}
			
		],//�в���
		classMethod : 'com.sharp.module.auth.web.UserAction.queryUser',//Ҫ���õķ���
		page : <%=flexiGridInfo.getFlexigrid_page()%>,
		rp : <%=flexiGridInfo.getFlexigrid_rp()%>,
		sortname : '<%=flexiGridInfo.getFlexigrid_sortname().equals("") == true ? "lId" : flexiGridInfo.getFlexigrid_sortname()%>',// Ԥ��ָ�����н�������
		sortorder : '<%=flexiGridInfo.getFlexigrid_sortorder().equals("") == true ? "asc" : flexiGridInfo.getFlexigrid_sortorder()%>',// Ԥ������ķ�ʽ
		userFunc : getFormData
	});
});

//���
function getFormData() 
{
	$.addFormData("form","flexlist");
	
	return true;
	
}
//��ѯ
function doQuery()
{	
	$.gridReload("flexlist");
}
</script>
package com.sharp.core.style;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspWriter;

import com.sharp.core.entity.HtmlInfo;
/**
 * ������: HTMLSTYLE
 * ��������html��ʽ
 * ����ʱ��: 2016-7-6 ����11:30:43
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class Html
{
	/**
	 * �������ƣ�showHomeEnd
	 * ������������ʾһ����Ϣ(ҳ��ͷ��)
	 * �����ˣ� ������
	 * ����ʱ�䣺2016-7-6 ����11:39:35
	 */
    public static void showHomeHead(JspWriter out,String strTitle)
    {

        String strProject = "SpringSharp";
        HtmlInfo html = new HtmlInfo();
        html.setJspWriter(out);
        html.setTitle(strTitle);
        html.setShowMenu(1);
        html.setProjectName(strProject);
        html.setTitle(strProject);
        displyHtmlHeader(html);
    }
	/**
	 * �������ƣ�showHomeEnd
	 * ������������ʾһ����Ϣ(ҳ��β��)
	 * �����ˣ� ������
	 * ����ʱ�䣺2016-7-6 ����11:39:35
	 */
	public static void showHomeEnd(JspWriter out) throws IOException
	{
		  HtmlInfo html = new HtmlInfo();
	      html.setJspWriter(out);
	      displyHtmlFooter(html);
	}
    /**
     * �������ƣ�displyHtmlHeader
     * ������������ʾһ����Ϣ(ҳ��ͷ��)
     * �����ˣ� ������
     * ����ʱ�䣺2016-7-6 ����11:39:40
     * @param html
     * @since JDK 1.5
     */
    private static void displyHtmlHeader(HtmlInfo html)
    {
    	try {
			html.getJspWriter().println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">");
	
	        html.getJspWriter().println("<html>");
	        html.getJspWriter().println("<head>");
	        html.getJspWriter().println("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\">");
	        html.getJspWriter().println("<title>" + html.getTitle() + "</title>\n");

	        html.getJspWriter().println("<script language=\"javascript\" src=\"/SpringSharp/webdocs/jquery/jquery1.9.1.js\"></script>\n");
	        html.getJspWriter().println("<script language=\"javascript\" src=\"/SpringSharp/webdocs/jquery/jquery-browser.js\"></script>\n");
	        html.getJspWriter().println("<script language=\"javascript\" src=\"/SpringSharp/webdocs/js/util.js\"></script>\n");
	        html.getJspWriter().println("<script language=\"javascript\" src=\"/SpringSharp/webdocs/flexigrid/flexigrid.js\"></script>\n");
	        html.getJspWriter().println("<script language=\"javascript\" src=\"/SpringSharp/webdocs/flexigrid/flexigridEncapsulation.js\"></script>\n");
	        
	        html.getJspWriter().println("<link rel=\"stylesheet\" href=\"/SpringSharp/webdocs/css/flexigrid.css\" type=\"text/css\">\n");
	        
	        html.getJspWriter().println("</head>\n");
	        html.getJspWriter().println("<table >");
	        html.getJspWriter().println("<tbody>");
	        html.getJspWriter().println("<tr>");
	        html.getJspWriter().println("<td>");
	} catch (IOException e) {
			
			e.printStackTrace();
			
		}
    }
    
    private static void displyHtmlFooter(HtmlInfo html)
    {
    	try {
	        html.getJspWriter().println("</td>");
	        html.getJspWriter().println("</tr>");
	        html.getJspWriter().println("</tbody>");
	        html.getJspWriter().println("</table>");
	        html.getJspWriter().println("</body>");
	        html.getJspWriter().println("</html>");
	} catch (IOException e) {
			
			e.printStackTrace();
			
		}
    }
    
    public static boolean validateRequest(JspWriter out, HttpServletRequest request, HttpServletResponse response) throws Exception
    {
        boolean bResult = true;
        try
        {
        	Enumeration<?> en = request.getParameterNames ( ) ;  
        	while (en.hasMoreElements ( ))  
        	{  
        	    String strName = (String) en.nextElement ( ) ;  
        	    String strValue = request.getParameter ( strName ) ;  
        	  
        	    if (request.getAttribute ( strName ) == null)  
        	    {  
        	        request.setAttribute ( strName , strValue ) ;  
        	    }  
        	}  
        }
        catch (Exception e)
        {
        
        }
        return bResult;
    }

    
    
}

package com.sharp.core.entity;

import javax.servlet.jsp.JspWriter;


/**
 * 类名称: HtmlHeaderInfo
 * 类描述：页面展示头实体类
 * 创建时间: 2016-7-6 上午11:29:05
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class HtmlInfo
{
    private String ProjectName = "";
    private String StyleName = "";
    private JspWriter jspWriter = null;
    private String RemindURL = "";
    private String Status = "";
    private String Title = "";
    private String SubTitle = "";
    private long ShowMenu = 1;
    private String year = "";
    
    public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	/**
     * @return Returns the jspWriter.
     */
    public JspWriter getJspWriter()
    {
        return jspWriter;
    }
    /**
     * @param jspWriter The jspWriter to set.
     */
    public void setJspWriter(JspWriter jspWriter)
    {
        this.jspWriter = jspWriter;
    }
    /**
     * @return Returns the projectName.
     */
    public String getProjectName()
    {
        return ProjectName;
    }
    /**
     * @param projectName The projectName to set.
     */
    public void setProjectName(String projectName)
    {
        ProjectName = projectName;
    }
    /**
     * @return Returns the remindURL.
     */
    public String getRemindURL()
    {
        return RemindURL;
    }
    /**
     * @param remindURL The remindURL to set.
     */
    public void setRemindURL(String remindURL)
    {
        RemindURL = remindURL;
    }
    /**
     * @return Returns the showMenu.
     */
    public long isShowMenu()
    {
        return ShowMenu;
    }
    /**
     * @param showMenu The showMenu to set.
     */
    public void setShowMenu(long showMenu)
    {
        ShowMenu = showMenu;
    }
    /**
     * @return Returns the status.
     */
    public String getStatus()
    {
        return Status;
    }
    /**
     * @param status The status to set.
     */
    public void setStatus(String status)
    {
        Status = status;
    }
    /**
     * @return Returns the styleName.
     */
    public String getStyleName()
    {
        return StyleName;
    }
    /**
     * @param styleName The styleName to set.
     */
    public void setStyleName(String styleName)
    {
        StyleName = styleName;
    }
    /**
     * @return Returns the title.
     */
    public String getTitle()
    {
        return Title;
    }
    /**
     * @param title The title to set.
     */
    public void setTitle(String title)
    {
        Title = title;
    }
	/**
	 * @return Returns the subTitle.
	 */
	public String getSubTitle() {
		return SubTitle;
	}
	/**
	 * @param subTitle The subTitle to set.
	 */
	public void setSubTitle(String subTitle) {
		SubTitle = subTitle;
	}
	/**
	 * @return Returns the showMenu.
	 */
	public long getShowMenu() {
		return ShowMenu;
	}
}

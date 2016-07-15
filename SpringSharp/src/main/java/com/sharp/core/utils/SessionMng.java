
package com.sharp.core.utils;
import java.io.Serializable;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import com.sharp.core.action.ActionMessages;

/**
 * 类名称: SessionMng
 * 类描述：登录加载数据
 * 创建时间: 2016-7-4 下午06:02:45
 * 创建人： 邢凌霄
 * 版本： 1.0
 * @since JDK 1.5
 */
public class SessionMng extends Object implements Serializable ,HttpSessionBindingListener
{
	private static final long serialVersionUID = 1L;
	private ActionMessages	m_actionMessages	= new ActionMessages () ;

	/**
	 * Returns the actionMessages.
	 * 
	 * @return ActionMessages
	 */
	public ActionMessages getActionMessages ( )
	{
		return m_actionMessages ;
	}

	public void valueBound(HttpSessionBindingEvent event) {
		
		
	}

	public void valueUnbound(HttpSessionBindingEvent event) {
		
		
	}
	
}
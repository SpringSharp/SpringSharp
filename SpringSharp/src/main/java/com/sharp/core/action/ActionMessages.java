package com.sharp.core.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * ������: ActionMessages
 * �����������ڱ�����ʾ��Ϣ
 * ����ʱ��: 2016-7-4 ����05:51:36
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class ActionMessages implements Serializable
{
	/**
	 * serialVersionUID:���к�
	 * @since JDK 1.5
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * �洢������ʾ��Ϣ
	 */
	private Collection<String> m_collMessages = new ArrayList<String>();
	
	/**
	 * ActionExceptionĬ�Ϲ��췽����
	 * @see java.lang.Object#Object()
	 */
	public ActionMessages()
	{
		super();
	}

	/**
	 * ��ActionException�쳣����һ������
	 * @param actionException
	 */
	public ActionMessages(Exception exception)
	{
			this.m_collMessages.add(exception.getMessage());
	}
	/**
	 * ����һ��ָ������ϢActionMessages��
	 * @param message ��Ϣ��
	 */
	public ActionMessages(String message)
	{
		this.m_collMessages.add(message);
	}

	/**
	 * ��ǰ���������һ����Ϣ
	 * @param messageKey
	 */
	public void addMessage(String message)
	{
		this.m_collMessages.add(message);
	}

	/**
	 * ��һ���쳣�����е���Ϣ��ӵ���ǰ������
	 * @param actionException
	 */
	public void addMessage(Exception exception)
	{
		if (exception != null)
		{
				this.m_collMessages.add(exception.getMessage());
		}
	}

	/**
	 * ���������Ϣ
	 * @return Enumeration
	 */
	public Collection<String> getMessages()
	{
		return this.m_collMessages;
	}

	/**
	 * �жϵ�ǰActionError�������Ƿ��д�����Ϣ��
	 * @return boolean
	 */
	public boolean isEmpty()
	{
		return this.m_collMessages.isEmpty();
	}

	/**
	 * ��յ�ǰ�����е���Ϣ
	 */
	public void clear()
	{
		this.m_collMessages.clear();
	}

	public String toString()
	{
		StringBuffer sb = new StringBuffer(128);

		Iterator<String> itTemp = this.m_collMessages.iterator();

		while (itTemp.hasNext())
		{
			sb.append((String) itTemp.next());
		}

		return sb.toString();
	}

}

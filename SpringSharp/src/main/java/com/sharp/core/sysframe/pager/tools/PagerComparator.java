/**
 * ��������:SpringSharp
 * �ļ�����:PagerComparator.java
 * �����ƣ� com.sharp.core.sysframe.pager.tools
 * ����ʱ��: 2016��7��12������11:19:53
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.sysframe.pager.tools;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.Date;

/**
 * ������: PagerComparator
 * ��������ͨ�����򷽷�
 *         �ȽϺ���ǿ�ж�ĳЩ���� collection ������������
 *         ���Խ� Comparator ���ݸ� sort �������� Collections.sort�����Ӷ�����������˳����ʵ�־�ȷ���ơ�
 *         ������ʹ�� Comparator ������ĳЩ���ݽṹ���� TreeSet �� TreeMap����˳��
 * ����ʱ��: 2016��7��12�� ����11:20:10
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class PagerComparator implements Comparator<Object> {
    
    String sortName = null;
    
    public PagerComparator(String sortName)
    {
        this.sortName = sortName;
    }

    public int compare(Object o1, Object o2) {
        
        Field field1 = null;
        Field field2 = null;
        String fieldTypeName = null;
        
        int flag = 0;
        
        try
        {
            field1 = o1.getClass().getDeclaredField(sortName);
            field1.setAccessible(true);
            
            field2 = o2.getClass().getDeclaredField(sortName);
            field2.setAccessible(true);
            
            fieldTypeName = field1.getType().getName();
            
            if(fieldTypeName.equals(double.class.getName()))
            {
                flag = Double.compare(field1.getDouble(o1), field2.getDouble(o2));
            }
            else if(fieldTypeName.equals(long.class.getName()))
            {
                flag = Double.compare(field1.getLong(o1), field2.getLong(o2));
            }
            else if(fieldTypeName.equals(Timestamp.class.getName()))
            {
                Timestamp fidldValue1 = (Timestamp) field1.get(o1);
                Timestamp fidldValue2 = (Timestamp) field2.get(o2);
                
                flag = fidldValue1.compareTo(fidldValue2);
            }
            else if(fieldTypeName.equals(Date.class.getName()))
            {
                Date fidldValue1 = (Date) field1.get(o1);
                Date fidldValue2 = (Date) field2.get(o2);
                
                flag = fidldValue1.compareTo(fidldValue2);
            }
            else if(fieldTypeName.equals(String.class.getName()))
            {
                String fidldValue1 = (String) field1.get(o1);
                String fidldValue2 = (String) field2.get(o2);
                
                flag = fidldValue1.compareTo(fidldValue2);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        return flag;
    }

}


/**
 * ��������:SpringSharp
 * �ļ�����:ReflectionUtil.java
 * �����ƣ� com.sharp.core.utils
 * ����ʱ��: 2016��7��8������10:39:39
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.utils;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
/**
 * ������: ReflectionUtil
 * �����������乤����
 * ����ʱ��: 2016��7��8�� ����10:40:00
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */

public class ReflectionUtil {
    /**
     * 
     * @title: setField
     * @description: ����ĳ����Ա������ֵ
     * @param owner
     * @param fieldName
     * @param value
     * @throws Exception
     * @return: void
     */
    public static void setField(Object owner, String fieldName, Object value) throws Exception {
        Class<?> ownerClass = owner.getClass();
        Field field = ownerClass.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(owner, value);
    }
      
    /**
     * 
     * @title: setFieldAll
     * @description: �������ø����field��ֵ
     * @param owner
     * @param fieldName
     * @param value
     * @throws Exception
     * @return: void
     */
    public static void setFieldAll(Object owner, String fieldName, Object value) throws Exception {
        Class<?> ownerClass = owner.getClass();
        Field field = null;
        for (Class<?> clazz = ownerClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                field = clazz.getDeclaredField(fieldName);
                System.out.println(field + " find : in " + clazz.getName());
                break;
            }
            catch (Exception e) {
                System.out.println(fieldName + " not find in " + clazz.getName());
            }
        }
        field.setAccessible(true);
        field.set(owner, value);
    }
      
    /**
     * �õ�ĳ������Ĺ�������
     * 
     * @param owner
     *            , fieldName
     * @return �����Զ���
     * @throws Exception
     * 
     */
    public static Object getField(Object owner, String fieldName) throws Exception {
        Class<?> ownerClass = owner.getClass();
          
        Field field = ownerClass.getField(fieldName);
          
        Object property = field.get(owner);
          
        return property;
    }
      
    /**
     * �õ�ĳ��ľ�̬��������
     * 
     * @param className
     *            ����
     * @param fieldName
     *            ������
     * @return �����Զ���
     * @throws Exception
     */
    public static Object getStaticField(String className, String fieldName) throws Exception {
        Class<?> ownerClass = Class.forName(className);
          
        Field field = ownerClass.getField(fieldName);
          
        Object property = field.get(ownerClass);
          
        return property;
    }
      
    /**
     * ִ��ĳ���󷽷�
     * 
     * @param owner
     *            ����
     * @param methodName
     *            ������
     * @param args
     *            ����
     * @return ��������ֵ
     * @throws Exception
     */
    public static Object invokeMethod(Object owner, String methodName, Object... args) throws Exception {
          
        Class<?> ownerClass = owner.getClass();
          
        Class<?>[] argsClass = new Class[args.length];
          
        for (int i = 0, j = args.length; i < j; i++) {
            if (args[i].getClass() == Integer.class) { //һ��ĺ������� int ������Integer
                argsClass[i] = int.class;
            }
            else if (args[i].getClass() == Float.class) { //һ��ĺ������� int ������Integer
                argsClass[i] = float.class;
            }
            else if (args[i].getClass() == Double.class) { //һ��ĺ������� int ������Integer
                argsClass[i] = double.class;
            }
            else {
                argsClass[i] = args[i].getClass();
            }
        }
          
        Method method = ownerClass.getDeclaredMethod(methodName, argsClass);
        method.setAccessible(true);
        return method.invoke(owner, args);
    }
      
    /**
     * 
     * @title: invokeMethodAll
     * @description: �������еĺ���, ������������к���
     * @param owner
     * @param methodName
     * @param args
     * @return
     * @throws Exception
     * @return: Object
     */
    public static Object invokeMethodAll(Object owner, String methodName, Object... args) throws Exception {
          
        Class<?> ownerClass = owner.getClass();
          
        Class<?>[] argsClass = new Class[args.length];
          
        for (int i = 0, j = args.length; i < j; i++) {
            if (args[i].getClass() == Integer.class) { //һ��ĺ������� int ������Integer
                argsClass[i] = int.class;
            }
            else if (args[i].getClass() == Float.class) { //һ��ĺ������� int ������Integer
                argsClass[i] = float.class;
            }
            else if (args[i].getClass() == Double.class) { //һ��ĺ������� int ������Integer
                argsClass[i] = double.class;
            }
            else {
                argsClass[i] = args[i].getClass();
            }
        }
        Method method = null;
          
        for (Class<?> clazz = ownerClass; clazz != Object.class; clazz = clazz.getSuperclass()) {
            try {
                method = clazz.getDeclaredMethod(methodName, argsClass);
                System.out.println(method + " find : in " + clazz.getName());
                return method;
            }
            catch (Exception e) {
                //e.printStackTrace();
                System.out.println(methodName + " not find in " + clazz.getName());
            }
        }
        method.setAccessible(true);
        return method.invoke(owner, args);
    }
      
    /**
     * ִ��ĳ��ľ�̬����
     * 
     * @param className
     *            ����
     * @param methodName
     *            ������
     * @param args
     *            ��������
     * @return ִ�з������صĽ��
     * @throws Exception
     */
    public static Object invokeStaticMethod(String className, String methodName, Object... args) throws Exception {
        Class<?> ownerClass = Class.forName(className);
          
        Class<?>[] argsClass = new Class[args.length];
          
        for (int i = 0, j = args.length; i < j; i++) {
            argsClass[i] = args[i].getClass();
        }
          
        Method method = ownerClass.getMethod(methodName, argsClass);
        method.setAccessible(true);
        return method.invoke(null, args);
    }
      
    /**
     * �½�ʵ��
     * 
     * @param className
     *            ����
     * @param args
     *            ���캯���Ĳ��� ����޹��������args ��дΪ null
     * @return �½���ʵ��
     * @throws Exception
     */
    public static Object newInstance(String className, Object[] args) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        return newInstance(className, args, null);
          
    }
      
    /**
     * �½�ʵ��
     * 
     * @param className
     *            ����
     * @param args
     *            ���캯���Ĳ��� ����޹��������args ��дΪ null
     * @return �½���ʵ��
     * @throws Exception
     */
    public static Object newInstance(String className, Object[] args, Class<?>[] argsType) throws NoSuchMethodException, SecurityException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Class<?> newoneClass = Class.forName(className);
          
        if (args == null) {
            return newoneClass.newInstance();
              
        }
        else {
            Constructor<?> cons;
            if (argsType == null) {
                Class<?>[] argsClass = new Class[args.length];
                  
                for (int i = 0, j = args.length; i < j; i++) {
                    argsClass[i] = args[i].getClass();
                }
                  
                cons = newoneClass.getConstructor(argsClass);
            }
            else {
                cons = newoneClass.getConstructor(argsType);
            }
            return cons.newInstance(args);
        }
          
    }
      
    /**
     * �ǲ���ĳ�����ʵ��
     * 
     * @param obj
     *            ʵ��
     * @param cls
     *            ��
     * @return ��� obj �Ǵ����ʵ�����򷵻� true
     */
    public static boolean isInstance(Object obj, Class<?> cls) {
        return cls.isInstance(obj);
    }
      
    /**
     * �õ������е�ĳ��Ԫ��
     * 
     * @param array
     *            ����
     * @param index
     *            ����
     * @return ����ָ��������������������ֵ
     */
    public static Object getItemInArray(Object array, int index) {
        return Array.get(array, index);
    }
      
    /**
     * 
     * @title: GetClassListByPackage
     * @description: ��ȡ���µ�����Class
     * @param pPackage
     * @return
     * @return: Class<?>
     */
    public static Class<?> getClassListByPackage(String pPackage) {
        Package _Package = Package.getPackage(pPackage);
        Class<?> _List = _Package.getClass();
          
        return _List;
    }
}


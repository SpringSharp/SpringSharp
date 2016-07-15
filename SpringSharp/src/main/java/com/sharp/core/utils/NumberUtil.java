/**
 * ��������:springejb
 * �ļ�����:DoubleUtils.java
 * �����ƣ� com.core.utils
 * ����ʱ��: 2016-7-4����04:01:24
 * Copyright (c) 2016, iSoftStone All Rights Reserved.
 *
*/
package com.sharp.core.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * ������: DoubleUtils
 * ��������double���͹�����
 * ����ʱ��: 2016-7-4 ����04:01:28
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class NumberUtil {
	
	/**
	 * �ص�double����С�����֣���������������
	 * */
	public static double cutNumberAfterDecimal(double beforeDouble){
		Double before = new Double(beforeDouble);
		double afterDouble = (double)before.longValue();
		return afterDouble;
	}	
	
	/**
	 * ��ʽ���б�Ľ��
	 * 
	 * @param dAmount
	 *            ��� @return(��������)
	 */
	public static String roundAmount ( double dAmount )
	{
		//������������
		BigDecimal bigDecimal = new BigDecimal ( dAmount ) ;
		double dBigDecimal = bigDecimal.doubleValue ( ) ;
		String sBigDecimal = formatAmount ( dBigDecimal , 0 ) ;
		if ("0".equals ( sBigDecimal ))
		{
			return "&nbsp;&nbsp;" ;
		}
		int iLength = 0 ;
		String sReturn = "" ;
		for (int i = sBigDecimal.length ( ) - 1; i >= 0; i--)
		{
			if (iLength == 3)
			{
				sReturn = "," + sReturn ;
				iLength = 0 ;
			}
			iLength++ ;
			char cData = sBigDecimal.charAt ( i ) ;
			sReturn = cData + sReturn ;
		}
		return sReturn ;
	}
	/**
	 * ��ʽ��������Ӻ�׺,����:��3.6��ʽ����3.600000,С�������λ
	 * 
	 * @param double
	 *            ��Ҫ��ʽ��������
	 * @param char
	 *            ��Ӻ�׺���ַ�
	 * @param int
	 *            С�����λ��
	 * @return String ���ظ�ʽ������ַ���
	 */
	public static String formatTax ( double taxRate , char suffix , int decimal )
	{
		String strReturnValue = "" + taxRate ; //����ֵ
		int intDotPosition = -1 ; //С����λ��
		int intNumberLen = -1 ; //���ֳ���		
		String strSuffix = "" ; //��׺
		
		for (int n = 0; n < decimal; n++)
		{ //��ʼ����׺
			strSuffix += suffix ;
		}
		intDotPosition = strReturnValue.indexOf ( "." ) ;
		if (intDotPosition < 0)
		{ //�����Ҫ��ʽ��������û��С��
			strReturnValue += "." ;
			intNumberLen = strReturnValue.length ( ) + decimal ;
		} else
		{
			intNumberLen = intDotPosition + decimal + 1 ; //ȡ�ø�ʽ�������ֵĳ���
		}
		strReturnValue += strSuffix ; //��Ӻ�׺
		strReturnValue = strReturnValue.substring ( 0 , intNumberLen ) ;
		return strReturnValue ;
	}
	/**
	 * @param dfValue
	 *            the double value to be format
	 * @param nFmt
	 *            format is requested.
	 * @return String
	 */
	/**
	 * @deprecated this format fucntion is unsafe and uncompatible.
	 */
	public static double formatDouble ( double dValue , int nScale )
	{
		return new Double ( format ( dValue , nScale ) ).doubleValue ( ) ;
	}
	public static double formatDouble ( double dValue )
	{
		return formatDouble ( dValue , 2 ) ;
	}
	
	/**
	 * ��ʽ���ַ���Ϊ�ٷ���
	 * Create Date: 2007-06-15
	 * @author xkli3
	 * @param strData
	 * @param lScale
	 * @return String ��ʽ����ַ���
	 */
	public static String formatStringToPercent(String strData, int lScale )
	{
		double dData = 0;
		if(strData == null || strData.length() == 0)
		{
			return "";
		}
		else
		{
			dData = Double.parseDouble(strData) * 100;
			if(dData == 0)
			    return "";
			else
				 return format(dData, lScale);
		}
			
	}
	/**
	 * ��ʽ���б�Ľ��
	 * 
	 * @param dAmount
	 *            ���
	 * @param dAmount
	 *            lTypeID -��0ת����2-����0ת��
	 * @return ���ظ�ʽ���Ľ��
	 */
	public static String formatListAmount ( double dAmount , int lTypeID )
	{
		String strData = formatDisabledAmount ( dAmount , lTypeID ) ;
		if (strData == null || strData.length ( ) <= 0)
		{
			strData = "&nbsp;" ;
		}
		return strData ;
	}
	
	/**
	 * ��ʽ���б�Ľ��
	 * 
	 * @param dAmount
	 *            ���
	 * ��dAmountֵΪ0��Ϊ�յ��������ҳ����ʾֵ:0.00
	 *            
	 * @return ���ظ�ʽ���Ľ��
	 */
	public static String formatListAmount( double dAmount,boolean condition) {
		String strData = formatDisabledAmount ( dAmount ) ;
		if (strData == null || strData.length ( ) <= 0) {
			strData = "0.00" ;
		}
		return strData ;
	}
	
	/**
	 * ��ʽ���б�Ľ��
	 * 
	 * @param dAmount
	 *            ���
	 * @return ���ظ�ʽ���Ľ��
	 */
	public static String formatListAmount ( double dAmount )
	{
		String strData = formatDisabledAmount ( dAmount ) ;
		if (strData == null || strData.length ( ) <= 0)
		{
			strData = "&nbsp;" ;
		}
		return strData ;
	}
	
	/**
	 * ��ʽ���б�Ľ��
	 * 
	 * @param dAmount
	 *            ���
	 * @param nDigit
	 *            С������λ��
	 * @return ���ظ�ʽ���Ľ��
	 */
	public static String formatDisabledAmount ( int nDigit , double dAmount )
	{
		return formatDisabledAmount ( dAmount , 1 , nDigit ) ;
	}
	/**
	 * ��ʽ���б�Ľ��
	 * 
	 * @param dAmount
	 *            ���
	 * @return ���ظ�ʽ���Ľ��
	 */
	public static String formatDisabledAmount ( double dAmount )
	{
		return formatDisabledAmount ( dAmount , 1 ) ;
	}
	
	/**
	 * ��ʽ���б�Ľ��
	 * 
	 * @param dAmount
	 *            ���
	 * @param nType
	 *            1-��0ת����2-����0ת��
	 * @param nDigit
	 *            С������λ��
	 * @return
	 */
	public static String formatDisabledAmount ( double dAmount , int nType ,
			int nDigit )
	{
		String strData = "" ;
		if (nType == 1)
		{
			strData = formatAmount ( dAmount , nDigit ) ;
		} else
		{
			strData = formatAmountUseZero ( dAmount , nDigit ) ;
		}
		//if (dAmount < 0)
		//	strData = formatAmount(java.lang.Math.abs(dAmount), nDigit);
		if (strData.equals ( "" ))
		{
			return strData ;
		} else
		{
			System.out.print ( "=======strData=" + strData ) ;
			//��С����ǰ�ͺ�����ݷֱ�ȡ����
			int nPoint ;
			nPoint = strData.indexOf ( "." ) ;
			String strFront = new String ( strData ) , strEnd = "" ;
			if (nPoint != -1)
			{
				strFront = strData.substring ( 0 , nPoint ) ;
				strEnd = strData.substring ( nPoint + 1 , strData.length ( ) ) ;
			}
			String strTemp = "" ;
			//С����ǰ������ݼ�","
			strTemp = new String ( strFront ) ;
			strFront = "" ;
			int nNum , i ;
			nNum = 0 ;
			for (i = strTemp.length ( ) - 1; i >= 0; i--)
			{
				if (nNum == 3)
				{
					strFront = "," + strFront ;
					nNum = 0 ;
				}
				nNum++ ;
				char cData ;
				cData = strTemp.charAt ( i ) ;
				strFront = cData + strFront ;
			}
			//�����߽�С��������ֵ������С������λ��
			if (strEnd.length ( ) >= nDigit)
			{
				strEnd = strEnd.substring ( 0 , nDigit ) ;
			} else
			{
				strEnd = strEnd + formatInt ( 0 , nDigit - strEnd.length ( ) ) ;
			}
			if (nDigit != 0) {
				strData = strFront + "." + strEnd ;
			} else {
				strData = strFront;
			}
		}
		System.out.print("=======strData="+strData);
		return strData ;
	}
	
	
	/**
	 * ��ʽ�����
	 * 
	 * @param dValue
	 *            ���
	 */
	public static String formatAmount ( double dValue )
	{
		//
		if (dValue == 0)
			return "" ;
		else if (dValue > 0)
			return format ( dValue , 2 ) ;
		else
			return "-" + format ( java.lang.Math.abs ( dValue ) , 2 ) ;
	}
	/**
	 * ��ʽ��������0
	 * 
	 * @param dValue
	 * @return
	 */
	public static String formatAmountUseZero ( double dValue )
	{
		if (dValue == 0)
			return "0.00" ;
		else if (dValue > 0)
			return format ( dValue , 2 ) ;
		else
			return "-" + format ( java.lang.Math.abs ( dValue ) , 2 ) ;
	}
	/**
	 * �����ʽ������","ȥ��
	 * 
	 * @param strData
	 *            ����
	 */
	public static String reverseFormatAmount ( String strData )
	{
		int i ;
		String strTemp ;
		//ȥ�����е�","
		strTemp = new String ( strData ) ;
		strData = "" ;
		for (i = 0; i < strTemp.length ( ); i++)
		{
			char cData ;
			cData = strTemp.charAt ( i ) ;
			if (cData != ',')
			{
				strData = strData + cData ;
			}
		}
		return strData ;
	}
	/**
	 * ��ʽ���б�Ľ��
	 * 
	 * @param dAmount
	 *            ���
	 * @return ���ظ�ʽ������С����û�ж��ŷָ���Ľ��
	 */
	public static String formatAmountNotDot ( double dAmount )
	{
		String strData = formatAmount ( dAmount ) ;
		if (strData.equals ( "" ))
		{
			return strData ;
		} else
		{
			//��С����ǰ�ͺ�����ݷֱ�ȡ����
			int nPoint ;
			nPoint = strData.indexOf ( "." ) ;
			String strFront = new String ( strData ) , strEnd = "" ;
			if (nPoint != -1)
			{
				strFront = strData.substring ( 0 , nPoint ) ;
				strEnd = strData.substring ( nPoint + 1 , strData.length ( ) ) ;
			}
			//�����߽�С��������ֵ��������λ
			if (strEnd.length ( ) > 2)
			{
				strEnd = strEnd.substring ( 0 , 2 ) ;
			} else
			{
				if (strEnd.length ( ) == 1)
				{
					strEnd = strEnd + "0" ;
				} else
				{
					if (strEnd.length ( ) == 0)
					{
						strEnd = "00" ;
					}
				}
			}
			strData = strFront + strEnd ;
			for (int ii = 18; ii < strData.length ( ); ii--)
			{
				strData = "&nbsp;" + strData ;
			}
		}
		return strData ;
	}
	/**
	 * ��ʽ���б�Ľ��
	 * 
	 * @param dAmount
	 *            ���
	 * @param nType
	 *            1-��0ת����2-����0ת��
	 * @return
	 */
	public static String formatDisabledAmount ( double dAmount , int nType )
	{
		String strData = "" ;
		if (nType == 1)
		{
			strData = formatAmount ( dAmount ) ;
		} else
		{
			strData = formatAmountUseZero ( dAmount ) ;
		}
		if (dAmount < 0)
			strData = formatAmount ( java.lang.Math.abs ( dAmount ) ) ;
		if (strData.equals ( "" ))
		{
			return strData ;
		} else
		{
			//��С����ǰ�ͺ�����ݷֱ�ȡ����
			int nPoint ;
			nPoint = strData.indexOf ( "." ) ;
			String strFront = new String ( strData ) , strEnd = "" ;
			if (nPoint != -1)
			{
				strFront = strData.substring ( 0 , nPoint ) ;
				strEnd = strData.substring ( nPoint + 1 , strData.length ( ) ) ;
			}
			String strTemp = "" ;
			//С����ǰ������ݼ�","
			strTemp = new String ( strFront ) ;
			strFront = "" ;
			int nNum , i ;
			nNum = 0 ;
			for (i = strTemp.length ( ) - 1; i >= 0; i--)
			{
				if (nNum == 3)
				{
					strFront = "," + strFront ;
					nNum = 0 ;
				}
				nNum++ ;
				char cData ;
				cData = strTemp.charAt ( i ) ;
				strFront = cData + strFront ;
			}
			//�����߽�С��������ֵ��������λ
			if (strEnd.length ( ) > 2)
			{
				strEnd = strEnd.substring ( 0 , 2 ) ;
			} else
			{
				if (strEnd.length ( ) == 1)
				{
					strEnd = strEnd + "0" ;
				} else
				{
					if (strEnd.length ( ) == 0)
					{
						strEnd = "00" ;
					}
				}
			}
			strData = strFront + "." + strEnd ;
		}
		if (dAmount < 0 && !strData.equals ( "0.00" ))
			strData = "-" + strData ;
		return strData ;
	}
	/**
	 * ��ʽ�����
	 * 
	 * @param dValue
	 *            ���
	 * @param nDigit
	 *            С������λ��
	 */
	public static String formatAmount ( double dValue , int nDigit )
	{
		//
		if (dValue == 0)
			return "" ;
		else if (dValue > 0)
			return format ( dValue , nDigit ) ;
		else
			return "-" + format ( java.lang.Math.abs ( dValue ) , nDigit ) ;
	}
	/**
	 * ��ʽ��������0
	 * 
	 * @param dValue
	 * @param nDigit
	 *            С������λ��
	 * @return
	 */
	public static String formatAmountUseZero ( double dValue , int nDigit )
	{
		if (dValue == 0)
		{
			return "0." + formatInt ( 0 , nDigit ) ;
		} else if (dValue > 0)
		{
			return format ( dValue , nDigit ) ;
		} else
		{
			return "-" + format ( java.lang.Math.abs ( dValue ) , nDigit ) ;
		}
	}
	/**
	 * ��ʽ�����֣����磺��5ת��Ϊ4λ�ַ�����õ�0005
	 * 
	 * @param dValue
	 *            ����ʽ������ֵ
	 * @param nWidth
	 *            ��Ҫת����λ��
	 * @return
	 */
	public static String formatInt ( long lValue , int nWidth )
	{
		String strReturn = "" + lValue ;
		int initLength = strReturn.length ( ) ;
		for (int i = nWidth; i > initLength; i--)
		{
			strReturn = "0" + strReturn ;
		}
		return strReturn ;
	}
	public static String format ( double dValue , int lScale )
	{
		//		////��������װ��Ϊ�����������������
		boolean bFlag = false ;
		if (dValue < 0)
		{
			bFlag = true ;
			dValue = -dValue ;
		}
		long lPrecision = 10000 ; //����ֵ��Ϊ�˱���double�ͳ���ƫ�����У��λ
		long l45Value = lPrecision / 2 - 1 ; //����������ж�ֵ
		long lLength = 1 ; //�˵�����
		for (int i = 0; i < lScale; i++)
		{
			lLength = lLength * 10 ; //���籣��2λ������100
		}
		long lValue = (long) dValue ; //ֵ����������
		long lValue1 = (long) ((dValue - lValue) * lLength) ; //���Ա���λ��
		long lValue2 = (long) ((dValue - lValue) * lLength * lPrecision) ; //
		long lLastValue = lValue2 - (lValue2 / lPrecision) * lPrecision ;
		if (lLastValue >= l45Value)
		{
			lValue1++ ;
		}
		dValue = lValue + (double) lValue1 / lLength ; //����������ֵ
		if (bFlag)
		{
			dValue = -dValue ;
		}
		java.math.BigDecimal bd = new java.math.BigDecimal ( dValue ) ;
		bd = bd.setScale ( lScale , java.math.BigDecimal.ROUND_HALF_UP ) ;
		return bd.toString ( ) ;
	}
	public static String format ( float fValue , int lScale )
	{
		java.math.BigDecimal bd , bdup , bddown ;
		bd = new java.math.BigDecimal ( fValue ) ;
		bdup = bd.setScale ( lScale , java.math.BigDecimal.ROUND_UP ) ;
		bddown = bd.setScale ( lScale , java.math.BigDecimal.ROUND_DOWN ) ;
		if ((bdup.doubleValue ( ) - bd.doubleValue ( )) <= (bd.doubleValue ( ) - bddown
				.doubleValue ( )))
		{
			return bdup.toString ( ) ;
		} else
		{
			return bddown.toString ( ) ;
		}
	}
	/**
	 * ������ʽ�����ַ�����ת��Ϊ��ֵ�����磺12,345.00ת��Ϊ12345
	 * 
	 * @param text
	 *            ����ʽ������ֵ
	 * @return
	 */
	public static double parseNumber ( String text )
	{
		int index = text.indexOf ( "," ) ;
		String sbNumber = "" ;
		while (index != -1)
		{
			sbNumber += text.substring ( 0 , index ) ;
			text = text.substring ( index + 1 , text.length ( ) ) ;
			index = text.indexOf ( "," ) ;
		}
		sbNumber += text ;
		System.out.println ( sbNumber ) ;
		return Double.parseDouble ( sbNumber ) ;
	}
	/**
	 * �õ����������
	 * 
	 * @param nLen���������
	 */
	public static String randomNumberPassword ( int nLen )
	{
		long lNum = 1 ;
		for (int i = 0; i < nLen - 1; i++)
		{
			lNum = lNum * 10 ;
		}
		return String
				.valueOf ( (long) ((lNum * 10 - lNum + 1) * Math.random ( ))
						+ lNum ) ;
	}
	
	public static String format ( java.math.BigDecimal bValue , int lScale )
	{
		if (bValue != null)
		{
			bValue = bValue.setScale ( lScale , java.math.BigDecimal.ROUND_HALF_UP ) ;
			return bValue.toString() ;
		}
		else
		{
			return "";
		}
	}
	public static double BigToDouble ( java.math.BigDecimal bValue , int lScale )
	{
		if (bValue != null)
		{
			bValue = bValue.setScale ( lScale , java.math.BigDecimal.ROUND_HALF_UP ) ;
			return bValue.doubleValue() ;
		}
		else
		{
			return 0;
		}
	}
	/**
	 * ��ʽ�����֣����磺12345ת��Ϊ12,345
	 * 
	 * @param dValue
	 *            ����ʽ������ֵ 
	 * @param iScale
	 *            С�������λ��,���㲹0
	 * @return
	 */
	public static String formatNumber ( double dValue , int iScale )
	{
		DecimalFormat df = null ;
		StringBuffer sPattern = new StringBuffer ( ",##0" ) ;
		if (iScale > 0)
		{
			sPattern.append ( "." ) ;
			for (int i = 0; i < iScale; i++)
			{
				sPattern.append ( "0" ) ;
			}
		}
		df = new DecimalFormat ( sPattern.toString ( ) ) ;
		return df.format ( dValue ) ;
	}
	public static String formatNumber ( long lValue )
	{
		return formatNumber ( (double) lValue , 0 ) ;
	}
	public static long parseLong ( String text )
	{
		int index = text.indexOf ( "," ) ;
		String sbNumber = "" ;
		while (index != -1)
		{
			sbNumber += text.substring ( 0 , index ) ;
			text = text.substring ( index + 1 , text.length ( ) ) ;
			index = text.indexOf ( "," ) ;
		}
		sbNumber += text ;
		System.out.println ( sbNumber ) ;
		return Long.parseLong ( sbNumber ) ;
	}
	/**
	 * ��ʽ���б������
	 * 
	 * @param lCount
	 *            ����
	 * @return ���ظ�ʽ��������
	 */
	public static String formatListLong ( long lCount )
	{
		String strTemp = "" ;
		if (lCount >= 0)
		{
			strTemp = String.valueOf ( lCount ) ;
		}
		String strFront = "" ;
		int nNum , i ;
		nNum = 0 ;
		for (i = strTemp.length ( ) - 1; i >= 0; i--)
		{
			if (nNum == 3)
			{
				strFront = "," + strFront ;
				nNum = 0 ;
			}
			nNum++ ;
			char cData ;
			cData = strTemp.charAt ( i ) ;
			strFront = cData + strFront ;
		}
		return strFront ;
	}
	
	//��ȷ�ĳ���
	public static BigDecimal div(double v1, double v2, int scale)
	{
		if (scale < 0)
		{
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
	}
}


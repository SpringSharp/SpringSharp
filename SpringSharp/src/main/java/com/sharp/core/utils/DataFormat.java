package com.sharp.core.utils ;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Vector;
/**
 * ������: DataFormat
 * �����������ݸ�ʽ��
 * ����ʱ��: 2016-7-4 ����03:22:57
 * �����ˣ� ������
 * �汾�� 1.0
 * @since JDK 1.5
 */
public class DataFormat {

    /**
     * ���ڸ�ʽ����
     */
    public static final int DT_YYYY_MM_DD = 1; //yyyy-MM-dd
    public static final int DT_YYYYMMDDHHMMSS = 2; //yyyyMMddHHmmss
    public static final int DT_YYYYMMDD_T_HHMMSS = 3; //yyyy-MM-dd'T'HH:mm:ss
    public static final int DT_YYYY = 4; //yyyy
    public static final int DT_YYYYMMDD_HHMMSS_SSSS  = 5; //yyyy-MM-dd HH:mm:ss.S
    public static final int DT_YYYYMMDD = 6; //yyyyMMdd
    public static final int DT_HH_MM_SS = 7; //hh:mm:ss
    public static final int DT_YYYYMMDD_HHMMSS = 8; //yyyy-MM-dd HH:mm:ss
    public static final int DT_HHMMSS = 9; //HHmmss
    
    /**
     * ���ڸ�ʽ������Ӧ���ַ���
     * @param type
     * @return
     */
    public static String getDateConvertString(int type)
    {
        String strFormatString = "";
        switch(type)
        {
            case DataFormat.DT_YYYY_MM_DD:
                strFormatString = "yyyy-MM-dd";
                break;
            case DataFormat.DT_YYYYMMDDHHMMSS:
                strFormatString = "yyyyMMddHHmmss";
                break;
            case DataFormat.DT_YYYYMMDD_T_HHMMSS:
                strFormatString = "yyyy-MM-dd'T'HH:mm:ss";
                break;
            case DataFormat.DT_YYYY:
                strFormatString = "yyyy";
                break;
            case DataFormat.DT_YYYYMMDD_HHMMSS_SSSS:
                strFormatString = "yyyy-MM-dd HH:mm:ss.S";
                break;
            case DataFormat.DT_YYYYMMDD:
                strFormatString = "yyyyMMdd";
                break;
            case DataFormat.DT_HH_MM_SS:
                strFormatString = "HH:mm:ss";
                break;
            case DataFormat.DT_YYYYMMDD_HHMMSS:
                strFormatString = "yyyy-MM-dd HH:mm:ss";
                break;
            case DataFormat.DT_HHMMSS:
                strFormatString = "HHmmss";
                break;
            default:
                strFormatString = "yyyy-MM-dd";
                break;
        }
        return strFormatString;
    }
    
    public static String formatDate(Date date, int type)
    {
        if(date == null){
            return "";
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(DataFormat.getDateConvertString(type));
        return dateFormat.format(date);
    }
    
    public static String formatDate(Timestamp date, int type){
        if(date == null){
            return "";
        }
        
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.applyPattern(DataFormat.getDateConvertString(type));
        return dateFormat.format(date);
    }
    
	/**
	 * ��ʽ���ַ����������ַ�����nullת��Ϊ"&nbsp;"
	 * 
	 * @param strData
	 *            ��Ҫ��ʽ�����ַ���
	 * @return String
	 */
	public static String formatEmptyString ( String strData )
	{
		if (strData == null || strData.length ( ) == 0)
		{
			return "&nbsp;" ;
		} else
		{
			return strData ;
		}
	}
	/**
	 * ��ʽ���ַ����������ַ�����nullת��Ϊ""
	 * 
	 * @param strData
	 *            ��Ҫ��ʽ�����ַ���
	 * @return String
	 */
	public static String formatNullString ( String strData )
	{
		if (strData == null || strData.length ( ) == 0)
		{
			return "" ;
		} else
		{
			return strData ;
		}
	}
	
	/**
	 * //����������ֱ�Ϊ�й���д����
	 * 
	 * @param ts
	 * @return
	 */
	static public String getChineseNumberString ( long lMonth )
	{
		if (lMonth < 10000)
			return getChineseNumBelowTenThousand ( String.valueOf ( lMonth ) ) ;
		else
			return "" ;
	}
	static public String getChineseNumBelowTenThousand ( String strNum )
	{
		//Log.print ( "����strNum=" + strNum ) ;
		String strTmp = "" ;
		if (strNum.length ( ) >= 1)
			strTmp = getChineseNumSingle ( strNum.substring ( 
					strNum.length ( ) - 1 , strNum.length ( ) ) ) ;
		if (strNum.length ( ) >= 2)
			strTmp = strTmp
					+ "ʮ"
					+ getChineseNumSingle ( strNum.substring ( 
							strNum.length ( ) - 2 , strNum.length ( ) - 1 ) ) ;
		if (strNum.length ( ) >= 3)
			strTmp = strTmp
					+ "��"
					+ getChineseNumSingle ( strNum.substring ( 
							strNum.length ( ) - 3 , strNum.length ( ) - 2 ) ) ;
		if (strNum.length ( ) >= 4)
			strTmp = strTmp
					+ "ǧ"
					+ getChineseNumSingle ( strNum.substring ( 
							strNum.length ( ) - 4 , strNum.length ( ) - 3 ) ) ;
		if (strTmp.equals ( "" ))
			strTmp = "��" ;
		//Log.print("����strNum=" + strTmp);
		return strTmp ;
	}
	static public String getChineseNumSingle ( String strSingleNum )
	{
		String strTmpTmp = "" ;
		//Log.print("strSingleNum=" + strSingleNum);
		switch ((int) Long.parseLong ( strSingleNum ))
		{
			case 0 :
				strTmpTmp = "" ;
				break ;
			case 1 :
				strTmpTmp = "һ" ;
				break ;
			case 2 :
				strTmpTmp = "��" ;
				break ;
			case 3 :
				strTmpTmp = "��" ;
				break ;
			case 4 :
				strTmpTmp = "��" ;
				break ;
			case 5 :
				strTmpTmp = "��" ;
				break ;
			case 6 :
				strTmpTmp = "��" ;
				break ;
			case 7 :
				strTmpTmp = "��" ;
				break ;
			case 8 :
				strTmpTmp = "��" ;
				break ;
			case 9 :
				strTmpTmp = "��" ;
				break ;
		}
		return strTmpTmp ;
	}


	//the following const is to define double format
	public static final int	FMT_NUM_NORMAL		= 1 ;
	public static final int	FMT_NUM_NODELIMA	= 2 ;
	public static final int	FMT_NUM_COMMADELIMA	= 3 ;
	/**
	 * get file name from full file path.
	 * 
	 * @param strFilePath
	 *            file path to be processed.
	 * @return file name.
	 */
	public static String getFileName ( String strFilePath )
	{
		int nIndex = 0 ;
		if ((nIndex = strFilePath.lastIndexOf ( '\\' )) == -1)
			nIndex = strFilePath.lastIndexOf ( '/' ) ;
		return strFilePath.substring ( nIndex + 1 , strFilePath.length ( ) ) ;
	}
	/**
	 * �ж��Ƿ��ǺϷ��� email ��ַ (������,'@')
	 * 
	 * @param strEMail
	 * @return
	 */
	public static boolean isEmail ( String strEMail )
	{
		int nDelima = strEMail.indexOf ( "@" ) ;
		int nDot = strEMail.lastIndexOf ( "." ) ;
		if (nDelima != -1 && nDot > nDelima)
		{
			return true ;
		}
		return false ;
	}
	/**
	 * filterHTML: ��ʾҳ��ʱ,�뽫�����ַ�����: " => &quot; , > => &gt; , < => &lt; '\n' =>
	 * <br>, '\r' =><br>
	 * 
	 * @param strHTML
	 *            input html string.
	 * @return String ���ظ�ʽ������ַ�����
	 */
	public static String filterHTML ( String strHTML )
	{
		if (strHTML == null)
			return "" ;
		StringBuffer sbResult = new StringBuffer ( ) ;
		int nLen = strHTML.length ( ) ;
		char chCur ;
		for (int i = 0; i < nLen; i++)
		{
			chCur = strHTML.charAt ( i ) ;
			switch (chCur)
			{
				case '\"' :
					sbResult.append ( "&quot;" ) ;
					break ;
				case '>' :
					sbResult.append ( "&gt;" ) ;
					break ;
				case '<' :
					sbResult.append ( "&lt;" ) ;
					break ;
				case '\r' :
				case '\n' :
					sbResult.append ( "<br>" ) ;
					break ;
				default :
					sbResult.append ( chCur ) ;
					break ;
			}
		}
		return sbResult.toString ( ) ;
	}
	/**
	 *  
	 */
	public static String normalizeString ( String strValue )
	{
		return ((strValue == null) ? "" : strValue.trim ( )) ;
	}
	public static String transFormat ( String str )
			throws java.io.UnsupportedEncodingException
	{
		return new String ( str.getBytes ( "ISO8859_1" ) , "GBK" ) ;
	}
	public static String subAsciiString ( String strSRC , int nLen )
	{
		if (strSRC == null)
		{
			return "" ;
		} else
		{
			byte[] byTemp = strSRC.getBytes ( ) ;
			if (nLen < byTemp.length)
			{
				boolean bDouble = false ;
				int i ;
				for (i = 0; i < nLen; i++)
				{
					if (bDouble)
					{
						bDouble = (false) ;
					} else
					{
						bDouble = (((short) byTemp[i] & 0xff) > 0x80) ;
					}
				}
				if (bDouble)
					i-- ;
				return new String ( byTemp , 0 , i ) ;
			}
			return strSRC ;
		}
	}
	/**
	 * convert a string encode with 'iso-8859-1', which is used to transfer in
	 * network wiht http, to 'GBK' encoding.
	 * 
	 * @param strGBK
	 *            a string encoded with 'GBK'
	 * @return return a new string encoded with 'iso-8859-1'.
	 */
	public static String toAscii ( String strGBK )
	{
		try
		{
			return new String ( DataFormat.normalizeString ( strGBK ).getBytes ( 
					"GBK" ) , "iso-8859-1" ) ;
		} catch (UnsupportedEncodingException uee)
		{
			uee.printStackTrace ( ) ;
		}
		return null ;
	}
	//������Ч����ֵ
	public static String				W_STRING			= "" ;
	public static long					W_ID				= -1 ;
	public static double				W_AMOUNT			= 0 ;
	public static float					W_RATE				= 0 ;
	public static java.sql.Timestamp	W_DATE				= null ;
	//���������Ϣ��
	public static String				m_strBeChangedTxt[]	=
																{"<", "[b]",
			"[/b]", "[br]", "[I]", "[/I]", "[P]", "[/P]", "[SUP]", "[/SUP]",
			"[SUB]", "[/SUB]", "[BR]", "[A", "[/A]", "[STRIKE]", "[/STRIKE]",
			"[UL]", "[LI]", "[/UL]", "[OL]", "[LI]", "[/OL]", "[FONT",
			"[/FONT]"											} ;
	public static String				m_strChangeTxt[]	=
																{"&lt;", "<b>",
			"</b>", "<br>", "<I>", "</I>", "<P>", "</P>", "<SUP>", "</SUP>",
			"<SUB>", "</SUB>", "<BR>", "<A", "</A>", "<STRIKE>", "</STRIKE>",
			"<UL>", "<LI>", "</UL>", "<OL>", "<LI>", "</OL>", "<FONT",
			"</FONT>"											} ;
	/**
	 * set dest array with special char
	 * 
	 * @param dest
	 * @param ch
	 */
	public static void memSet ( char dest[] , char ch )
	{
		for (int i = 0; i < dest.length; i++)
		{
			dest[i] = ch ;
		}
	}
	/**
	 * copy src array into dest array
	 * 
	 * @param dest
	 *            to where
	 * @param from
	 *            where
	 * @return the byte copied, the length is minmum of dest length and src
	 *         length.
	 */
	public static int memCopy ( char dest[] , char src[] )
	{
		int nLen = Math.min ( dest.length , src.length ) ;
		for (int i = 0; i < nLen; i++)
		{
			dest[i] = src[i] ;
		}
		return nLen ;
	}
	public static String txtStore ( String strText )
	{
		//convert a text when it will store text to db
		//parameter: cText, the text which will be converted.
		if (strText == null)
		{
			strText = "" ;
		} else
		{
			for (int i = 0; i < m_strBeChangedTxt.length; i++)
			{
				String strBeChangedTxt = m_strBeChangedTxt[i] ;
				String strChangeTxt = m_strChangeTxt[i] ;
				int nLength = strBeChangedTxt.length ( ) ;
				while (strText.indexOf ( strBeChangedTxt ) >= 0)
				{
					int nPlace = strText.indexOf ( strBeChangedTxt ) ;
					strText = strText.substring ( 0 , nPlace )
							+ strChangeTxt
							+ strText.substring ( nPlace + nLength , strText
									.length ( ) ) ;
				}
			}
		}
		return strText ;
	}
	/**
	 * �������ַ�����_��%��'ת��
	 * 
	 * @param strIn
	 *            �����ַ���
	 * @param cTransferChar
	 *            ת���������ʹ��'\'��'/'
	 * @param lType
	 *            ת������ 0 ����LIKE��� 1 ���ڷ�LIKE���
	 * @return ת�����ַ���
	 */
	public static String transString ( String strIn , char cTransferChar ,
			long lType )
	{
		String strReturn = "" ;
		for (int nIndex = 0; nIndex < strIn.length ( ); nIndex++)
		{
			char cChar = strIn.charAt ( nIndex ) ;
			switch (cChar)
			{
				case '\'' :
					strReturn += "'" ;
					break ;
				case '%' :
					if (lType == 0)
					{
						strReturn += new Character ( cTransferChar ) ;
						break ;
					}
				case '_' :
					if (lType == 0)
					{
						strReturn += new Character ( cTransferChar ) ;
						break ;
					}
			}
			if (cChar == cTransferChar && lType == 0)
			{
				strReturn += new Character ( cTransferChar ) ;
			}
			strReturn += new Character ( cChar ) ;
		}
		return strReturn ;
	}
	/**
	 * �õ�������ַ�
	 * 
	 * @return ���������
	 */
	public static String getRnd ( )
	{
		return String.valueOf ( (long) ((10000000000l - 1000000000l + 1) * Math
				.random ( )) + 1000000000l ) ;
	}
	/**
	 * ��һ����","�ֿ��Ĵ��ֽ�Ϊһ��Vector������
	 * 
	 * @param strParam
	 *            ��Ҫ��ֵĲ���
	 * @return ����һ��Vector��������Long��
	 */
	public static Vector<Long> changeStringGroup ( String strParam )
	{
		Vector<Long> vcReturn = new Vector<Long> ( ) ;
		int nIndex ; //","������λ��
		nIndex = strParam.indexOf ( "," ) ;
		String strData = "" ; //��������ִ�
		while (nIndex > 0)
		{
			strData = strParam.substring ( 0 , nIndex ) ;
			vcReturn.add ( new Long ( strData ) ) ;
			strParam = strParam.substring ( nIndex + 1 , strParam.length ( ) ) ;
			nIndex = strParam.indexOf ( "," ) ;
		}
		if (strParam != "")
		{
			vcReturn.add ( new Long ( strParam ) ) ;
		}
		return vcReturn ;
	}
	//��������
	public static final String	m_strEncodeKeys	= "qwertyuiop[]asdfghjkl;;'zxcvbnm,./QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?`1234567890-=\\~!@#$%^&*()_+|" ;
	public static String randomPassword ( int nLen )
	{
		if (nLen <= 0)
			nLen = 8 ;
		char charyKeys[] = new char[nLen] ;
		char charyEncodeKeys[] = m_strEncodeKeys.toCharArray ( ) ;
		java.util.Random rndSequence = new java.util.Random ( ) ;
		int nSequence ;
		int nKeyLen = m_strEncodeKeys.length ( ) ;
		for (int i = 0; i < nLen; i++)
		{
			nSequence = Math.abs ( rndSequence.nextInt ( ) ) % nKeyLen ;
			charyKeys[i] = charyEncodeKeys[nSequence] ;
		}
		return charyKeys.toString ( ) ;
	}
		
	
	/**
	 * ��ʽ���ַ���
	 * 
	 * @param strData
	 *            �ַ�������
	 */
	public static String formatString ( String strData )
	{
		if (strData == null || strData.length ( ) == 0)
		{
			return "" ;
		} else
		{
			return strData ;
		}
	}


	/*
	 * ������ת���ɹ̶����ȵ��ַ��������λ����������ǰ�油�㣡 ���磺transCode(123,6); ���Ϊ��000123
	 */
	public static String transCode ( long ID , int length )
	{
		String returnString = "" ;
		if (ID >= 0)
		{
			returnString = String.valueOf ( ID ) ;
			for (int i = returnString.length ( ); i < length; i++)
			{
				returnString = "0" + returnString ;
			}
		}
		return returnString ;
	}

	/**
	 * ����ת��Ϊ��ĳһ���ŷֿ����ַ���
	 * 
	 * @param nothing
	 *            @return: such as 17,18,19,20,21,22,23
	 */
	public static String getStringWithTagByLongArray ( long[] args , String tag )
	{
		String str = "" ;
		for (int i = 0; i < args.length; i++)
			str = str + args[i] + tag ;
		return str.substring ( 0 , str.length ( ) - 1 ) ;
	}
	/**
	 * ����ת��Ϊ��','�ֿ����ַ���
	 * 
	 * @param nothing
	 *            @return: such as 17,18,19,20,21,22,23
	 */
	public static String getStringWithTagByLongArray ( long[] args )
	{
		return getStringWithTagByLongArray ( args , "," ) ;
	}
	/**
	 * �������JDK1.4 String::split�Ĺ��� @param1 splitedStr ��Ҫ���ָ��String @param2
	 * splitConditon �ָ�������ַ�
	 */
	public static String[] splitString ( String splitedStr ,
			String splitConditon )
	{
		int start = 0 ;
		int end = 0 ;
		ArrayList<String> list = new ArrayList<String> ( ) ;
		String tmp = null ;
		while (true)
		{
			end = splitedStr.indexOf ( splitConditon , start ) ;
			if (end == -1)
			{
				tmp = splitedStr.substring ( start , splitedStr.length ( ) ) ;
				list.add ( tmp ) ;
				break ;
			}
			tmp = splitedStr.substring ( start , end ) ;
			list.add ( tmp ) ;
			start = end + splitConditon.length ( ) ;
		}
		String[] res = new String[list.size ( )] ;
		for (int i = 0; i < list.size ( ); i++)
		{
			res[i] = (String) list.get ( i ) ;
		}
		return res ;
	}

	


	

	/**
	 * ��ʽ���ַ���������length�ģ�ǰ�油0
	 * added by wangzhen  <br>
	 * 2012-08-15  <br>
	 * @param str  <br>
	 * @param length  <br>
	 * @return
	 */
	public static String formatPreZeroString (String str,int length) {
		String sReturn = "";
		if(str != null) {
			if(str.length() <= length) {
				int zero = length - str.length();
				for(int i = 0; i < zero; i ++) {
					sReturn += "0";
				}
				sReturn += str;
			} else {
				sReturn = str;
			}
		}
		return sReturn;
	}

	/**
	 * �ַ���ת��������
	 * @author lxxing
	 * @date Mar 12, 2015 11:36:36 AM
	 * 
	 * */
	public static String codeToString(String str)
	{
		String s=str;
		try
		{
			byte temp[]=s.getBytes("iso-8859-1");
			s=new String(temp);
			return s;
		}
		catch(Exception e)
		{
			return s;
		}
	}
	
	/**
	 * �������ƣ�fillSeparator
	 * ������������sElementԪ��Ϊ������������ΪlPositionλ�ô�����Separator�ָ���
	 * ������sElement = 123456789��lPosition = 8��   Separator = "-";�����12345678-9
	 * �����ˣ� ������
	 * ����ʱ�䣺2016-6-15 ����10:06:35
	 * @param sElement
	 * @param lPosition
	 * @param Separator
	 * @return
	 * @since JDK 1.5
	 */
	public static String fillSeparator(String  sElement,int lPosition,String Separator){
		String sReturn = "";
		if(lPosition>=0&&sElement.length()>lPosition){
			if(sElement.indexOf(Separator)!=lPosition){
				String sHead = sElement.substring(0, lPosition);
				System.out.println("ͷ����"+sHead);
				String sEnd = sElement.substring(lPosition,sElement.length());
				System.out.println("β����"+sEnd);
				sReturn = sHead+Separator+sEnd;
			}else{
				sReturn = sElement;
			}
		}else{
			System.out.println("����lPosition����ֵ��������");
		}
		return sReturn;
	}
	
	public static void main(String[] args) {
		String str = "123456789";
		System.out.println(str.length());
	}
	
}
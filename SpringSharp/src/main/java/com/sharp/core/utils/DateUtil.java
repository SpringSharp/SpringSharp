package com.sharp.core.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * ClassName:DateUtils <br/>
 * Reason: ����ʱ�乤����. <br/>
 * Date: 2014-9-15 11:45:26<br/>
 * �������и��๦����׷�ӵ���ʽ��¼
 * @author ������
 * @version 1.0
 * @since JDK 1.5
 * @see
 */
public class DateUtil {
    
    public final static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    
    private static final SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat dateFormatSlash = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat timeFormat = new SimpleDateFormat( "HH:mm:ss");
    
    private static final Calendar calendar = Calendar.getInstance();
    
    /**
     * ��õ�ǰ����ʱ��
     * 
     * ����ʱ���ʽyyyy-MM-dd HH:mm:ss
     * 
     * @return
     */
    public static String currentDatetime() {
        return datetimeFormat.format(now());
    }
    /**
     * ��ʽ������ʱ��
     * <p>
     * ����ʱ���ʽyyyy-MM-dd HH:mm:ss
     * 
     * ��DateTime����ת����String����
     * @return
     */
    public static String formatDatetime(Date date) {
        return datetimeFormat.format(date);
    }
    
    /**
     * ��ʽ������ʱ��
     * <p>
     * ����ʱ���ʽyyyy-MM-dd HH:mm:ss
     * ��String����ת����DateTime����
     * @return
     * @throws ParseException 
     */
    public static Date formatDatetime(String dateTime) throws ParseException {
        return datetimeFormat.parse(dateTime);
    }
    
    /**
     * ��ʽ������ʱ��
     * 
     * @param date
     * @param pattern
     *  ��ʽ��ģʽ�����{@link SimpleDateFormat}������
     *  <code>SimpleDateFormat(String pattern)</code>
     * @return
     */
    public static String formatDatetime(Date date, String pattern) {
        SimpleDateFormat customFormat = (SimpleDateFormat) datetimeFormat.clone();
        customFormat.applyPattern(pattern);
        return customFormat.format(date);
    }
    /**
     * ��õ�ǰ����
     * <p>
     * ���ڸ�ʽyyyy-MM-dd
     * 
     * @return
     */
    public static String currentDate() {
        return dateFormat.format(now());
    }

    /**
     * ��ʽ������
     * <p>
     * ���ڸ�ʽyyyy-MM-dd
     * ��Dateת����String
     * @return
     */
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
    
    /**
     * ��ʽ������
     * <p>
     * ���ڸ�ʽyyyy-MM-dd
     * ��Stringת����Date
     * @return
     * @throws ParseException 
     */
    public static Date formatStringToDate(String date) throws ParseException {
        return dateFormat.parse(date);
    }
    
    /**
     * ��ʽ������
     * <p>
     * ת��ǰString���͸�ʽΪyyyy-MM-dd
     * ת����Date���͸�ʽΪyyyy/MM/dd
     * ��Stringת����Date
     * @return
     * @throws ParseException 
     */
    public static Date formatDate(String date) throws ParseException {
        return (Date)dateFormatSlash.parseObject(date);
    }
    /**
     * ��õ�ǰʱ��
     * <p>
     * ʱ���ʽHH:mm:ss
     * 
     * @return
     */
    public static String currentTime() {
        return timeFormat.format(now());
    }
    
    /**
     * ��ʽ��ʱ��
     * <p>
     * ʱ���ʽHH:mm:ss
     * 
     * @return
     */
    public static String formatTime(Date date) {
        return timeFormat.format(date);
    }
    
    /**
     * ��õ�ǰʱ���<code>java.util.Date</code>����
     * 
     * @return
     */
    public static Date now() {
        return new Date();
    }

    public static Calendar calendar() {
        Calendar cal = GregorianCalendar.getInstance(Locale.CHINESE);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        return cal;
    }

    /**
     * ��õ�ǰʱ��ĺ�����
     * <p>
     * ���{@link System#currentTimeMillis()}
     * 
     * @return
     */
    public static long millis() {
        return System.currentTimeMillis();
    }

    /**
     * 
     * ��õ�ǰChinese�·�
     * 
     * @return
     */
    public static int month() {
        return calendar().get(Calendar.MONTH) + 1;
    }

    /**
     * ����·��еĵڼ���
     * 
     * @return
     */
    public static int dayOfMonth() {
        return calendar().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * ���������ڵĵڼ���
     * 
     * @return
     */
    public static int dayOfWeek() {
        return calendar().get(Calendar.DAY_OF_WEEK);
    }

    /**
     * ���������еĵڼ���
     * 
     * @return
     */
    public static int dayOfYear() {
        return calendar().get(Calendar.DAY_OF_YEAR);
    }

    /**
     *�ж�ԭ�����Ƿ���Ŀ������֮ǰ
     * 
     * @param src
     * @param dst
     * @return
     */
    public static boolean isBefore(Date src, Date dst) {
        return src.before(dst);
    }

    /**
     *�ж�ԭ�����Ƿ���Ŀ������֮��
     * 
     * @param src
     * @param dst
     * @return
     */
    public static boolean isAfter(Date src, Date dst) {
        return src.after(dst);
    }

    /**
     *�ж��������Ƿ���ͬ
     * 
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isEqual(Date date1, Date date2) {
        return date1.compareTo(date2) == 0;
    }

    /**
     * �ж�ĳ�������Ƿ���ĳ�����ڷ�Χ
     * 
     * @param beginDate
     *            ���ڷ�Χ��ʼ
     * @param endDate
     *            ���ڷ�Χ����
     * @param src
     *            ��Ҫ�жϵ�����
     * @return
     */
    public static boolean between(Date beginDate, Date endDate, Date src) {
        return beginDate.before(src) && endDate.after(src);
    }
    /**
     * ��õ�ǰ�µĵ�һ��
     * <p>
     * HH:mm:ss SSΪ��
     * ���ڸ�ʽyyyy-MM-dd HH:mm:ss SS
     * 
     * @return
     */
    public static Date firstDayOfMonthDateTime(Date dtDate) {
    	if(dtDate==null){
    		return null;
    	}
        Calendar cal = calendar();
        cal.setTime(dtDate);
        cal.set(Calendar.DAY_OF_MONTH, 1); // M����1
        cal.set(Calendar.HOUR_OF_DAY, 0);// H����
        cal.set(Calendar.MINUTE, 0);// m����
        cal.set(Calendar.SECOND, 0);// s����
        cal.set(Calendar.MILLISECOND, 0);// S����
        return cal.getTime();
    }
    
    /**
     * ��õ�ǰ�µĵ�һ��
     * <p>
     * ���ڸ�ʽyyyy-MM-dd
     * 
     * @return  String 
     * @throws ParseException 
     */
    public static String firstDayOfMonthDateString(Date dtDate) throws ParseException {
    	if(dtDate==null){
    		return null;
    	}
        Calendar cal = calendar();
        cal.setTime(dtDate);
        cal.set(Calendar.DAY_OF_MONTH, 1); // M����1
        return dateFormat.format(cal.getTime());
    }
    /**
     * ��õ�ǰ�µĵ�һ��
     * <p>
     * ���ڸ�ʽyyyy-MM-dd
     * 
     * @return  Date
     * @throws ParseException 
     * @throws ParseException 
     */
    public static Date firstDayOfMonthDate(Date dtDate) throws ParseException{
    	if(dtDate==null){
    		return null;
    	}
    	Calendar cal = calendar();
    	cal.setTime(dtDate);
    	cal.set(Calendar.DAY_OF_MONTH, 1); // M����1
    	return dateFormat.parse(dateFormat.format(cal.getTime()));
    }
    /**
     * ��õ�ǰ�µ����һ��
     * <p>
     * HH:mm:ssΪ0������Ϊ999
     * ���ڸ�ʽyyyy-MM-dd HH:mm:ss SS
     * @return
     */
    public static Date lastDayOfMonthDateTime(Date dtDate) {
    	if(dtDate==null){
    		return null;
    	}
    	Calendar cal = calendar();
    	cal.setTime(dtDate);
    	cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// �·�+1
    	cal.set(Calendar.DAY_OF_MONTH, 1); // M����1
    	cal.setTime(before(cal.getTime(),1));
        return cal.getTime();
    }
    /**
     * ��õ�ǰ�µ����һ��
     * <p>
     * ���ڸ�ʽyyyy-MM-dd 
     * @return  String
     * @throws ParseException 
     */
    public static String lastDayOfMonthDateString(Date dtDate) throws ParseException {
    	if(dtDate==null){
    		return null;
    	}
    	Calendar cal = calendar();
    	cal.setTime(dtDate);
    	cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// �·�+1
    	cal.set(Calendar.DAY_OF_MONTH, 1); // M����1
    	cal.setTime(before(cal.getTime(),1));
    	return dateFormat.format(cal.getTime());
    }
    /**
     * ��õ�ǰ�µ����һ��
     * <p>
     * ���ڸ�ʽyyyy-MM-dd 
     * @return Date
     * @throws ParseException 
     */
    public static Date lastDayOfMonthDate(Date dtDate) throws ParseException {
    	if(dtDate==null){
    		return null;
    	}
    	Calendar cal = calendar();
    	cal.setTime(dtDate);
    	cal.set(Calendar.DAY_OF_MONTH, 0); // M������
    	cal.set(Calendar.HOUR_OF_DAY, 0);// H����
    	cal.set(Calendar.MINUTE, 0);// m����
    	cal.set(Calendar.SECOND, 0);// s����
    	cal.set(Calendar.MILLISECOND, 0);// S����
    	cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);// �·�+1
    	cal.set(Calendar.MILLISECOND, -1);// ����-1
    	cal.setTime(before(cal.getTime(),1));
    	return dateFormat.parse(dateFormat.format(cal.getTime()));
    }
    
    /**
     * �����һ���µĵ�һ��
     * <p>
     * ���ڸ�ʽyyyy-MM-dd
     * 
     * @return
     * @throws ParseException 
     */
    public static Date firstDayOfPriorMonth() throws ParseException {
        Calendar cal = calendar();
        cal.add(Calendar.MONTH, -1);
        cal.set(Calendar.DAY_OF_MONTH, 1); // M����1
        return dateFormat.parse(datetimeFormat.format(cal.getTime()));
    }

    private static Date weekDay(int week) {
        Calendar cal = calendar();
        cal.set(Calendar.DAY_OF_WEEK, week);
        return cal.getTime();
    }

    /**
     * �����������
     * <p>
     * ע��������������{@link #calendar()}������ÿ�����ڵĵ�һ��ΪMonday��US��ÿ���ڵ�һ��Ϊsunday
     * 
     * @return
     */
    public static Date friday() {
        return weekDay(Calendar.FRIDAY);
    }

    /**
     * �����������
     * <p>
     * ע��������������{@link #calendar()}������ÿ�����ڵĵ�һ��ΪMonday��US��ÿ���ڵ�һ��Ϊsunday
     * 
     * @return
     */
    public static Date saturday() {
        return weekDay(Calendar.SATURDAY);
    }

    /**
     * �����������
     * <p>
     * ע��������������{@link #calendar()}������ÿ�����ڵĵ�һ��ΪMonday��US��ÿ���ڵ�һ��Ϊsunday
     * 
     * @return
     */
    public static Date sunday() {
        return weekDay(Calendar.SUNDAY);
    }

    /**
     * ���ַ�������ʱ��ת����java.util.Date����
     * <p>
     * ����ʱ���ʽyyyy-MM-dd HH:mm:ss
     * 
     * @param datetime
     * @return
     */
    public static Date parseDatetime(String datetime) throws ParseException {
        return datetimeFormat.parse(datetime);
    }

    /**
     * ���ַ�������ת����java.util.Date����
     *<p>
     * ����ʱ���ʽyyyy-MM-dd
     * 
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        return dateFormat.parse(date);
    }

    /**
     * ���ַ�������ת����java.util.Date����
     *<p>
     * ʱ���ʽ HH:mm:ss
     * 
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date parseTime(String time) throws ParseException {
        return timeFormat.parse(time);
    }

    /**
     * �����Զ���pattern���ַ�������ת����java.util.Date����
     * 
     * @param datetime
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date parseDatetime(String datetime, String pattern)throws java.text.ParseException {
        SimpleDateFormat format = (SimpleDateFormat) datetimeFormat.clone();
        format.applyPattern(pattern);
        return format.parse(datetime);
    }



   
	/**
	 * ʹ�÷��䴦������ʱ����׼ʱ���ʽ��ת��String����Ҫ�������ʽ����һ��
	 * @param dateStr
	 * @return
	 */
    public static Date timeZoneStrToDate(String dateStr){
		try{
			if(dateStr==null) return null;
			SimpleDateFormat  sdf = new SimpleDateFormat ("EEE MMM dd HH:mm:ss 'CST' yyyy", Locale.US);
			Date date = sdf.parse(dateStr);
			
			return date;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
    

    /**
     * ������������ַ��������ĵ����ڼ� 1:������ 7������һ
     * @param dayOfWeek
     * @return
     */ 
	public static String getChineseWeekName(int dayOfWeek){
    	switch(dayOfWeek){  
    		case   1:   
  	  			return   "������"; 
			case   2:   
				return   "����һ";   
	  	  	case   3:   
	  	  		return   "���ڶ�";   
	  	  	case   4:   
	  	  		return   "������";   
	  	  	case   5:   
	  	  		return   "������";   
	  	  	case   6:   
	  	  		return   "������";   
	  	  	case   7:   
	  	  		return   "������";
    	}   
	  	return   "";   
    }
	
	
    /**
     * ���㴫�����ڵĺ�һ��
     * <p>
     * ����ʱ���ʽyyyy-MM-dd
     * @param String 
     * @return
     * @throws ParseException 
     */
    public static String afterDate(String dateString) throws ParseException {
        
        calendar.setTime(DateUtil.formatStringToDate(dateString));
        int dayDate = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, dayDate+1);
        
        return formatDate(calendar.getTime());
    }
    
    /**
     * ���㴫�����ڵ�ǰһ��
     * <p>
     * ����ʱ���ʽyyyy-MM-dd
     * @param String 
     * @return
     * @throws ParseException 
     */
    public static String beforeDate(String dateString) throws ParseException {
        
        calendar.setTime(DateUtil.formatStringToDate(dateString));
        int dayDate = calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE, dayDate-1);
        
        return formatDate(calendar.getTime());
    }
    
    /** ��ø�����ָ������֮ǰ������
     * @param dtDate
     * @param lDays
     * @return ��������
     */
    public static Date before(Date dtDate, long lDays)
    {
        long lCurrentDate = 0;
        lCurrentDate = dtDate.getTime() - lDays * 24 * 60 * 60 * 1000;
        Date dtBefore = new Date(lCurrentDate);
        return dtBefore;
    }
    /** ��ø�����ָ������֮�������
     * @param dtDate
     * @param lDays
     * @return ��������
     */
    public static Date after(Date dtDate, long lDays)
    {
        long lCurrentDate = 0;
        lCurrentDate = dtDate.getTime() + lDays * 24 * 60 * 60 * 1000;
        Date dtAfter = new Date(lCurrentDate);
        return dtAfter;
    }
    /**
     * ��StringתTimestamp
     * */
     public static Timestamp formatStringToTimestamp(String datetimeString){
     	Timestamp timestamp = null;
     	datetimeFormat.setLenient(false);
     	  try {
     		  timestamp = new Timestamp(datetimeFormat.parse(datetimeString).getTime());
     	  } catch (Exception e) {
     	   e.printStackTrace();
     	  }
     	return timestamp;
     }
     /**
      * ��DateתTimestamp
      * */
      public static Timestamp formatDateToTimestamp(Date date){
      	Timestamp timestamp = null;
      	  try {
      		timestamp = new Timestamp(date.getTime());
      	  } catch (Exception e) {
      	   e.printStackTrace();
      	  }
      	return timestamp;
      }
     /**
      * ��TimestampתDate
      * ����ʱ���ʽyyyy-MM-dd HH:mm:ss
      * */
     public static Date formatTimestampToDateTime (Timestamp ts){
     	Date date = null;
     	String strTemp = ts.toString();
     	 try {
 			date = datetimeFormat.parse(strTemp);
 		} catch (ParseException e) {
 			e.printStackTrace();
 		}   
     	return date;
     }
     /**
      * ��TimestampתDate
      * ����ʱ���ʽyyyy-MM-dd
      * */
     public static Date formatTimestampToDate (Timestamp ts){
    	 Date date = null;
    	 String strTemp = ts.toString();
    	 try {
    		 date = dateFormat.parse(strTemp);
    	 } catch (ParseException e) {
    		 e.printStackTrace();
    	 }   
    	 return date;
     }
 	/**
 	 * �������ƣ�getNextMonthForFix
 	 * ��������������ȥ��ʼ���ں��·ݲ������ó���ʼ���ڼ����·ݲ���֮�������
 	 * �����ˣ� ������
 	 * ����ʱ�䣺2016-6-30 ����04:00:46
 	 * @param dtDate
 	 * @param nMonth
 	 * @return
 	 * @since JDK 1.5
 	 */
 	static public Date getNextMonthForFix (Date dtDate,int nMonth )
 	{
  	    Calendar c = Calendar.getInstance();
  	    c.setTime(dtDate);
  	    c.add(Calendar.MONTH, nMonth);
  	    dtDate = c.getTime();
  	   return dtDate;
 	}
	/*
	 * �õ�һ�����ڵ��·ݣ���1-12��ʾ
	 */
	public static String getRealMonthString ( java.sql.Timestamp ts )
	{
		if (null == ts)
			return "" ;
		java.util.Calendar calendar = Calendar.getInstance ( ) ;
		calendar.setTime ( ts ) ;
		return String.valueOf ( calendar.get ( Calendar.MONTH ) + 1 ) ;
	}
	/*
	 * �õ�һ�����ڵ����
	 */
	public static String getYearString ( java.sql.Timestamp ts )
	{
		if (null == ts)
			return "" ;
		java.util.Calendar calendar = Calendar.getInstance ( ) ;
		calendar.setTime ( ts ) ;
		return String.valueOf ( calendar.get ( Calendar.YEAR ) ) ;
	}
	/*
	 * �õ�һ�����ڵ�����
	 */
	public static String getDayString ( java.sql.Timestamp ts )
	{
		if (null == ts)
			return "" ;
		java.util.Calendar calendar = Calendar.getInstance ( ) ;
		calendar.setTime ( ts ) ;
		return String.valueOf ( calendar.get ( Calendar.DAY_OF_MONTH ) ) ;
	}
	static public java.sql.Timestamp getDateTime ( String sDt )
	{
		try
		{
			return java.sql.Timestamp.valueOf ( sDt ) ; //sDt
														// format:yyyy-mm-dd
														// hh:mm:ss.fffffffff
		} catch (IllegalArgumentException iae)
		{
			sDt = sDt + " 00:00:00" ;
			try
			{
				return java.sql.Timestamp.valueOf ( sDt ) ;
			} catch (Exception e)
			{
				return null ;
			}
		}
	}
     public static void main(String[] args) {
     	String str = "2016-2-29";
     	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
     	try {
     	    Date myDate = formatter.parse(str);
     	    Calendar c = Calendar.getInstance();
     	    c.setTime(myDate);
     	    c.add(Calendar.MONTH, 12);
     	    myDate = c.getTime();
     	    System.out.println(DateUtil.formatDateToTimestamp(myDate));
     	} catch (ParseException e1) {
     	    e1.printStackTrace();
     	}
 	}
}

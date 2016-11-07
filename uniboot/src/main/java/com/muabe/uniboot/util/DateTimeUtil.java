package com.muabe.uniboot.util;

import java.text.ParseException;
import java.util.Date;


/**
 *  날짜,시간 관련 연산을 수행하는 Utility 클래스
 * @version V3.0 (2005-04-14)
 */
public class DateTimeUtil {

	/**
	 * Don't let anyone instantiate this class
	 */
	private DateTimeUtil() {
	}

	/**
	 * check date string validation with the default format "yyyyMMdd".
	 * @param s date string you want to check with default format "yyyyMMdd".
	 * @return date java.util.Date
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 format 에 맞지 않는 경우.
	 **/
	public static Date check(String s)
			throws ParseException {
		return check(s, "yyyyMMdd");
	}

	/**
	 *날짜를 표현하는 형식을 변경하여 변경된 문자열을 리턴한다.
	 * @param s 날짜를 나타내는 문자열
	 * @param format 소스(s) 날짜의 형식을 설명하는 문자열 ,예) "yyyy-MM-dd"
	 * @param toformat 변경될 날짜의 형식을 설명하는 문자열 ,예) "yyyy-MM-dd"
	 *  @return toformat형태로 변경된 날짜를 표시하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 format 에 맞지 않는 경우.
	 */
	public static String changeFormat(String s, String format, String toformat)
			throws ParseException {
		Date date = check(s, format);
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(toformat, java.util.Locale.KOREA);
		String dateString = formatter.format(date);
		return dateString;

	}

	/**
	 *날짜를 표현하는 형식을 변경하여 변경된 문자열을 리턴한다.
	 * @param date 날짜를 나타내는 Date객체
	 * @param toformat 변경될 날짜의 형식을 설명하는 문자열 ,예) "yyyy-MM-dd"
	 *  @return toformat형태로 변경된 날짜를 표시하는 문자열
	 */
	public static String changeFormat(Date date,  String toformat)
	{

		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(toformat, java.util.Locale.KOREA);
		String dateString = formatter.format(date);
		return dateString;

	}

	/**
	 * check date string validation with an user defined format.
	 * @param s date string you want to check.
	 * @param format string representation of the date format. For example, "yyyy-MM-dd".
	 * @return date java.util.Date
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 format 에 맞지 않는 경우 발생
	 */
	public static Date check(String s, String format)
			throws ParseException {
		if (s == null)
			throw new ParseException(
					"date string to check is null",
					0);
		if (format == null)
			throw new ParseException(
					"format string to check date is null",
					0);

		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		Date date = null;
		try {
			date = formatter.parse(s);
		} catch (ParseException e) {
			/*
			throw new java.text.ParseException(
				e.getMessage() + " with format \"" + format + "\"",
				e.getErrorOffset()
			);
			*/
			throw new ParseException(
					" wrong date:\"" + s + "\" with format \"" + format + "\"",
					0);
		}

		if (!formatter.format(date).equals(s))
			throw new ParseException(
					"Out of bound date:\""
							+ s
							+ "\" with format \""
							+ format
							+ "\"",
					0);
		return date;
	}

	/**
	 * check date string validation with the default format "HH:mm:ss".
	 * @param s date string you want to check with default format "HH:mm:ss"
	 * @return <tt>true</tt> 날짜 형식이 맞고, 존재하는 날짜일 때.
	 *                 <tt>false</tt> 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
	 */
	public static boolean isValid(String s) {
		return isValid(s, "HH:mm:ss");
	}

	/**
	 * "HH:mm" 또는 "HH/mm" 형태의 <code>java.util.Date</code> 객체를 리턴한다.
	 * @param s "HH:mm" 또는 "HH/mm" 형태의 현재 시각(몇시 몇분)을 나타내는 문자열
	 * @return 인자로 전달된 시각에 해당하는 <code>java.util.Date</code> 객체
	 * @throws ParseException 인자로 전달된 시각이 지정된 포멧("HH:mm" or "HH/mm" 에) 맞지 않거나 올바른 시간이 아닐경우 발생.
	 */
	public static Date getDateInstance(String s)
			throws ParseException {
		String format = "HH:mm";

		if (!isValid(s, "HH:mm")) {
			if (isValid(s, "HH/mm")) {
				format = "HH/mm";
			} else {
				throw new ParseException("wrong data or format", 0);
			}
		}
		return check(s, format);
	}

	/**
	 * check date string validation with an user defined format.
	 * @param s date string you want to check.
	 * @param format string representation of the date format. For example, "yyyy-MM-dd".
	 * @return <tt>true</tt> 날짜 형식이 맞고, 존재하는 날짜일 때.
	 *                 <tt>false</tt> 날짜 형식이 맞지 않거나, 존재하지 않는 날짜일 때
	 */
	public static boolean isValid(String s, String format) {
		/*
				if ( s == null )
					throw new NullPointerException("date string to check is null");
				if ( format == null )
					throw new NullPointerException("format string to check date is null");
		*/
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		Date date = null;
		try {
			date = formatter.parse(s);
		} catch (ParseException e) {
			return false;
		}

		if (!formatter.format(date).equals(s))
			return false;

		return true;
	}

	/**
	 * 현재 날짜를 "yyyy-MM-dd" 형태의 포멧으로 표현하는 문자열을 리턴한다.
	 * @return formatted string representation of current day with  "yyyy-MM-dd".
	 */
	public static String getDateString(Date date) {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(
						"yyyy-MM-dd",
						java.util.Locale.KOREA);
		return formatter.format(date);
	}

	/**
	 * 현재 날짜를 "yyyy-MM-dd" 형태의 포멧으로 표현하는 문자열을 리턴한다.
	 * @return formatted string representation of current day with  "yyyy-MM-dd".
	 */
	public static String getDateString2(Date date) {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(
						"yyyyMMdd",
						java.util.Locale.KOREA);
		return formatter.format(date);
	}

	/**
	 *
	 * 오늘 날짜를 숫자로 리턴한다.
	 *<code>getNumberByPattern("dd");</code>
	 * @return 오늘 날짜.(1~31)
	 * @see #getNumberByPattern(String, Date)
	 */
	public static int getDay(Date date) {
		return getNumberByPattern("dd", date);
	}

	/**
	 *
	 * 올해를 숫자로 리턴한다.
	 * <code> getNumberByPattern("yyyy");</code>
	 * @return 올해를 표현하는 4자리 숫자(예:2005)
	 * @see #getNumberByPattern(String, Date)
	 */
	public static int getYear(Date date) {
		return getNumberByPattern("yyyy", date);
	}

	/**
	 *
	 * 이번달을 숫자로 리턴한다.
	 * <code>getNumberByPattern("MM");</code>
	 * @return 이번달을 표현하는 숫자 (1~12)
	 * @see #getNumberByPattern(String, Date)
	 */
	public static int getMonth(Date date) {
		return getNumberByPattern("MM", date);
	}
	/**
	 *
	 * 현재 시간을 리턴한다.
	 * <code>getNumberByPattern("HH");</code>
	 * @return 현재 시간을 표현하는 숫자(1~24)
	 * @see #getNumberByPattern(String, Date)
	 */
	public static int getHour(Date date) {
		return getNumberByPattern("HH", date);
	}

	/**
	 *
	 * 현재 시간을 리턴한다.
	 * <code>getNumberByPattern("HH");</code>
	 * @return 현재 시간을 표현하는 숫자(1~12)
	 * @see #getNumberByPattern(String, Date)
	 */
	public static int getHour12(Date date) {
		return getNumberByPattern("hh", date);
	}

	/**
	 *
	 * 현재 시각의 분을 리턴한다.
	 *<code>getNumberByPattern("mm");</code>
	 * @return 현재 시각중 분을 표현하는 숫자(0~59)
	 * @see #getNumberByPattern(String, Date)
	 */
	public static int getMin(Date date) {
		return getNumberByPattern("mm", date);
	}

	/**
	 *
	 *인자로 전달된 패턴에 해당하는 값을 숫자로 리턴한다.
	 *
	 * 코드 사용예:
	 * 	<p><blockquote><pre>
	 *  int currentYearValue = DateTimeUtil.getNumberByPattern("yyyy");
	 * </pre></blockquote>
	 *
	 *
	 * @param pattern  "yyyy, MM, dd, HH, mm, ss,SSS"
	 * @return 현재의 날짜,달,연,시간,분,초 등을 나타내는 숫자값
	 */
	public static int getNumberByPattern(String pattern, Date date) {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(pattern, java.util.Locale.KOREA);
		String dateString = formatter.format(date);
		return Integer.parseInt(dateString);
	}

	/**
	 *인자로 전달된 시각을 표현하는 문자열에서 특정 부분의(년도 or 시 or 분 or 초 ...) 값을 숫자로 리턴한다.
	 *
	 *시각을 표현하는 문자열 2005/01/21 12:45:31 에서 초 부분을 나타내는 값을 얻어오려면 아래와 같이 코딩하면 된다.
	 * <p>코드 사용예:
	 * 	<p><blockquote><pre>
	 *  int seconds = DateTimeUtiil.getNumberByPattern("2005/01/21 12:45:31","yyyy/MM/dd hh:mm:ss","ss");
	 * </pre></blockquote>
	 * @param dates 기준 시각
	 * @param spattern <code>dates</code> 시각을 표현하는 날짜 포멧
	 * @param pattern  "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and time with  your pattern.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 spattern format 에 맞지 않는 경우.
	 */
	public static int getNumberByPattern(
			String dates,
			String spattern,
			String pattern)
			throws ParseException {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(pattern, java.util.Locale.KOREA);
		String dateString = formatter.format(check(dates, spattern));
		return Integer.parseInt(dateString);
	}

	/**
	 * 현재 시각을  인자로 전달된 형태의 포멧으로 표현하는 문자열을 리턴한다.
	 *  코드 사용예:
	 * 	<p><blockquote><pre>
	 * String time = DateTime.getFormatString("yyyy-MM-dd HH:mm:ss:SSS");
	 * </pre></blockquote>
	 *
	 * @param  pattern  "yyyy, MM, dd, HH, mm, ss and more"
	 * @return formatted string representation of current day and time with  your pattern.
	 */
	public static String getFormatString(Date date, String pattern) {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(pattern, java.util.Locale.KOREA);
		String dateString = formatter.format(date);
		return dateString;
	}

	/**
	 * 현재 시각을 "yyyyMMdd" 형태의 문자열로 표현하여 리턴한다.
	 * 예) "20040205"
	 * <code>getFormatString("yyyyMMdd");</code>
	 * @return formatted string representation of current day with  "yyyyMMdd".
	 * @see #getFormatString(Date, String)
	 */
	public static String getShortDateString(Date date) {
		return getFormatString(date, "yyyyMMdd");
	}

	/**
	 * 현재 시각을 "HHmmss" 형태의 문자열로 표현하여 리턴한다.
	 * <code>getFormatString("HHmmss");</code>
	 * @return formatted string representation of current time with  "HHmmss".
	 * 	 @see #getFormatString(Date, String)
	 */
	public static String getShortTimeString(Date date) {

		return getFormatString(date, "HHmmss");
	}

	/**
	 * 현재 시각을 "yyyy-MM-dd-HH:mm:ss:SSS" 형태의 문자열로 표현하여 리턴한다.
	 * <code>getFormatString("yyyy-MM-dd-HH:mm:ss:SSS");</code>
	 * @return formatted string representation of current time with  "yyyy-MM-dd-HH:mm:ss".
	 * @see #getFormatString(Date, String)
	 */
	public static String getTimeStampString(Date date) {

		return getFormatString(date, "yyyy-MM-dd-HH:mm:ss:SSS");
	}

	/**
	 *  현재 시각을 "HH:mm:ssSSS" 형태의 문자열로 표현하여 리턴한다.
	 *  코드 사용예:
	 * 	<p><blockquote><pre>
	 * String timeString= getFormatString("HH:mm:ss");
	 * </pre></blockquote>
	 * @return formatted string representation of current time with  "HH:mm:ss".
	 *	@see #getFormatString(Date, String)
	 */
	public static String getTimeString(Date date) {
		return getFormatString(date, "HH:mm:ss");
	}

	/**
	 * 인자로 전달된 "yyyyMMdd" 형태의 날짜가 무슨 요일 인지 리턴한다.
	 * 요일에 해당하는 값은 숫자로 리턴되고 이 값은 1~7에 해당한다.
	 *
	 * 사용예:
	 * <p><blockquote><pre>
	 * String s = "20000529";
	 *  int dayOfWeek = whichDay(s);
	 *  if (dayOfWeek == java.util.Calendar.MONDAY)
	 *      System.out.println(" 월요일: " + dayOfWeek);
	 *  if (dayOfWeek == java.util.Calendar.TUESDAY)
	 *      System.out.println(" 화요일: " + dayOfWeek);
	 * </pre></blockquote>
	 *
	 * return days between two date strings with default defined format.(yyyyMMdd)
	 * @param s date string you want to check.
	 * @return 다음의 값중 하나를 리턴.<pre>
	 *          1: 일요일 (java.util.Calendar.SUNDAY)
	 *          2: 월요일 (java.util.Calendar.MONDAY)
	 *          3: 화요일 (java.util.Calendar.TUESDAY)
	 *          4: 수요일 (java.util.Calendar.WENDESDAY)
	 *          5: 목요일 (java.util.Calendar.THURSDAY)
	 *          6: 금요일 (java.util.Calendar.FRIDAY)
	 *          7: 토요일 (java.util.Calendar.SATURDAY)
	 * </pre>
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이  "yyyyMMdd" 형식에 맞지 않는 경우.
	 *
	 */
	public static int whichDay(String s) throws ParseException {
		return whichDay(s, "yyyyMMdd");
	}

	/**
	 * 인자로 전달된 <code>format</code>형태의 날짜 <code>s</code>가 무슨 요일 인지 리턴한다.
	 * 요일에 해당하는 값은 숫자로 리턴되고 이 값은 1~7에 해당한다.
	 *
	 * 사용예:
	 * <p><blockquote><pre>
	 * String s = "2000-05-29";
	 *  int dayOfWeek = whichDay(s,"yyyy-MM-dd");
	 *  if (dayOfWeek == java.util.Calendar.MONDAY)
	 *      System.out.println(" 월요일: " + dayOfWeek);
	 *  if (dayOfWeek == java.util.Calendar.TUESDAY)
	 *      System.out.println(" 화요일: " + dayOfWeek);
	 * </pre></blockquote>
	 *
	 * @param s date string you want to check.
	 * @param format 날짜를 표현하는 포멧.
	 * @return 다음의 값중 하나를 리턴.<pre>
	 *          1: 일요일 (java.util.Calendar.SUNDAY)
	 *          2: 월요일 (java.util.Calendar.MONDAY)
	 *          3: 화요일 (java.util.Calendar.TUESDAY)
	 *          4: 수요일 (java.util.Calendar.WENDESDAY)
	 *          5: 목요일 (java.util.Calendar.THURSDAY)
	 *          6: 금요일 (java.util.Calendar.FRIDAY)
	 *          7: 토요일 (java.util.Calendar.SATURDAY)
	 * </pre>
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이  format 형식에 맞지 않는 경우.
	 */
	public static int whichDay(String s, String format)
			throws ParseException {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		Date date = check(s, format);

		java.util.Calendar calendar = formatter.getCalendar();
		calendar.setTime(date);
		return calendar.get(java.util.Calendar.DAY_OF_WEEK);
	}

	/**
	 * 인자로 전달된 <code>from</code> 날짜와 <code>to</code> 날짜 사이의 '날(day)'차이를 리턴한다. 두 날짜의 표현포멧은 "yyyyMMdd"이다.
	 * <p>
	 * 2005년 1월1일부터 2005년3월25일 사이의 날짜수를 구하는 코드:
	 * 	<p><blockquote><pre>
	 * 		int daysCount=DateTimeUtil.daysBetween("20050101","20050325");
	 * </pre></blockquote>
	 * return days between two date strings with default defined format.("yyyyMMdd")
	 * @param from date string
	 * @param to date string
	 * @return  두 날짜 사이의 '날(day)'의 차이.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 "yyyyMMdd" 형식에 맞지 않는 경우.
	 */
	public static int daysBetween(String from, String to)
			throws ParseException {
		return daysBetween(from, to, "yyyyMMdd");
	}

	/**
	 * 인자로 전달된 <code>from</code> 날짜와 <code>to</code> 날짜 사이의 '날(day)'차이를 리턴한다. 이때 두 날짜를 표현하는 포멧은 <code>format</code>을 사용한다.
	 * <p>2005년 1월1일부터 2005년3월25일 사이의 날짜수를 구하는 코드:
	 * 	<p><blockquote><pre>
	 * 		int daysCount=DateTimeUtil.daysBetween("20050101","20050325","yyyyMMdd");
	 * </pre></blockquote>
	 * @param from date string
	 * @param to date string
	 * @param format 두 시각을 표현하는 포멧  문자열.
	 * @return  두 시각 사이의 '날(day)'의 차이.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이  format 형식에 맞지 않는 경우.
	 */
	public static int daysBetween(String from, String to, String format)
			throws ParseException {
		Date d1 = check(from, format);
		Date d2 = check(to, format);

		long duration = d2.getTime() - d1.getTime();

		return (int) (duration / (1000 * 60 * 60 * 24));
		// seconds in 1 day
	}
	/**
	 * 인자로 전달된 <code>from</code>시각과   <code>to</code> 시각 사이의 '시간 (time)'차이를 리턴한다. 두 시각의 표현포멧은 "yyyyMMdd"이다.
	 *<p>2005년 1월1일부터 2005년3월25일 사이의 시간을  구하는 코드:
	 * 	<p><blockquote><pre>
	 * 		int timesCount=DateTimeUtil.timesBetween("20050101","20050325");
	 * </pre></blockquote>
	 * @param from date string
	 * @param to date string
	 * @return  두 시각 사이의 '시간(time)'의 차이.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 "yyyyMMdd" 형식에 맞지 않는 경우.
	 */
	public static int timesBetween(String from, String to)
			throws ParseException {
		return timesBetween(from, to, "yyyyMMdd");
	}

	/**
	 * 인자로 전달된 <code>from</code> 시각과  <code>to</code> 시각 사이의 '시간(time)'차이를 리턴한다. 두 시각의 표현포멧은 <code>format</code>을 사용한다.
	 * <p>2005년 1월1일부터 11시와  2005년3월25일 23시 사이의 시간을 구하는 코드:
	 * 	<p><blockquote><pre>
	 * 		int timesCount=DateTimeUtil.timesBetween("2005/01/01/ 11","2005/03/25 23","yyyy/MM/dd hh");
	 * </pre></blockquote>
	 * @param from date string
	 * @param to date string
	 * @param format 시각들을 표현하는 포멧 문자열.
	 * @return  두 시각 사이의 '시간(time)'의 차이.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static int timesBetween(String from, String to, String format)
			throws ParseException {

		Date d1 = check(from, format);
		Date d2 = check(to, format);

		long duration = d2.getTime() - d1.getTime();

		return (int) (duration / (1000 * 60 * 60));
		// seconds in 1 day
	}
	/**
	 * 인자로 전달된 <code>from</code> 시각과  <code>to</code> 시각 사이의 '분(minute)'차이를 리턴한다.  두 시간의 표현포멧은 <code>format</code>을 사용한다.
	 * <p<2005년 1월1일 11시 10분 부터 2005년3월25일 23시 59분 사이의 '분'을 구하는 코드:
	 * 	<p><blockquote><pre>
	 * 		int minCount=DateTimeUtil.minsBetween("2005/01/01 11:10","2005/03/25 23:59","yyyy/MM/dd hh:mm");
	 * </pre></blockquote>
	 * @param from date string
	 * @param to date string
	 * @param format 시각들을 표현하는 포멧 문자열.
	 * @return  두 시각  사이의 '시간(time)'의 차이.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static int minsBetween(String from, String to, String format)
			throws ParseException {

		Date d1 = check(from, format);
		Date d2 = check(to, format);

		long duration = d2.getTime() - d1.getTime();

		return (int) (duration / (1000 * 60));
		// seconds in 1 day
	}

	/**
	 * 인자로 전달된 <code>from</code> 시각과  <code>to</code> 시각 사이의 시간차이를 'hh시간 mm분' 이란 문자열로  리턴한다.  두 시각의 표현 포멧은 <code>format</code>을 사용한다.
	 * 2005년 1월1일 11시 10분 부터 2005년3월25일 23시 59분 사이의 시간차를 표현하는 문자열을 구하는 코드:
	 * 	<p><blockquote><pre>
	 * 		String timeGap=DateTimeUtil.timesBetweenStr("2005/01/01 11:10","2005/03/25 23:59","yyyy/MM/dd hh:mm");
	 * </pre></blockquote>
	 * @param from date string
	 * @param to date string
	 * @param format 시각들을 표현하는 포멧 문자열.
	 * @return  두 날짜 사이의 시간차이를 표현하는 'hh시간 mm분'이란 형태의 문자열.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static String timesBetweenStr(String from, String to, String format)
			throws ParseException {

		int min = minsBetween(from, to, format);
		if (min < 0) {
			throw new  RuntimeException("비교시간 결과가 음수 입니다");
		}
		int time = min / 60;
		min = min % 60;
		return new String(time + "시간 " + min + "분");
		// seconds in 1 day
	}
	/**
	 * 인자로 전달된 <code>from</code> 시각과  <code>to</code> 시각 사이의 개월수 차이를  리턴한다.  두 시각의 표현 포멧은 <code>"yyyyMMdd"</code>를 사용한다.
	 * 2005년 1월1일부터 2005년3월25일 사이의 개월수 차 를 표현하는 문자열을 구하는 코드:
	 * 	<p><blockquote><pre>
	 * 		int monthGap=DateTimeUtil.monthsBetween("20050101","20050325");
	 *
	 * </pre></blockquote>
	 * @param from date string
	 * @param to date string
	 * @return  두 날짜 사이의 개월수 차이
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>"yyyyMMdd"</code> 형식에 맞지 않는 경우.
	 */
	public static int monthsBetween(String from, String to)
			throws ParseException {
		return monthsBetween(from, to, "yyyyMMdd");
	}
	/**
	 * 인자로 전달된 <code>from</code> 시각과  <code>to</code> 시각 사이의 개월수 차이를  리턴한다.  두 시각의 표현 포멧은 <code>format</code>을 사용한다.
	 * 2005년 1월1일 11시 10분 부터 2005년3월25일 23시 59분 사이의 개월수 차 를 표현하는 문자열을 구하는 코드:
	 * 	<p><blockquote><pre>
	 * 		int monthGap=DateTimeUtil.monthsBetween("2005/01/01 11:10","2005/03/25 23:59","yyyy/MM/dd hh:mm");
	 * </pre></blockquote>
	 * @param from date string
	 * @param to date string
	 * @param format 시각들을 표현하는 포멧 문자열.
	 * @return  두 날짜 사이의 개월수 차이
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static int monthsBetween(String from, String to, String format)
			throws ParseException {
		Date fromDate = check(from, format);
		Date toDate = check(to, format);

		// if two date are same, return 0.
		if (fromDate.compareTo(toDate) == 0)
			return 0;

		java.text.SimpleDateFormat yearFormat =
				new java.text.SimpleDateFormat("yyyy", java.util.Locale.KOREA);
		java.text.SimpleDateFormat monthFormat =
				new java.text.SimpleDateFormat("MM", java.util.Locale.KOREA);
		java.text.SimpleDateFormat dayFormat =
				new java.text.SimpleDateFormat("dd", java.util.Locale.KOREA);

		int fromYear = Integer.parseInt(yearFormat.format(fromDate));
		int toYear = Integer.parseInt(yearFormat.format(toDate));
		int fromMonth = Integer.parseInt(monthFormat.format(fromDate));
		int toMonth = Integer.parseInt(monthFormat.format(toDate));
		int fromDay = Integer.parseInt(dayFormat.format(fromDate));
		int toDay = Integer.parseInt(dayFormat.format(toDate));

		int result = 0;
		result += ((toYear - fromYear) * 12);
		result += (toMonth - fromMonth);

		//        if (((toDay - fromDay) < 0) ) result += fromDate.compareTo(toDate);
		// ceil과 floor의 효과
		if (((toDay - fromDay) > 0))
			result += toDate.compareTo(fromDate);

		return result;
	}

	/**
	 * 인자로 전달된 <code>from</code> 시각과  <code>to</code> 시각 사이의 년도 차이를 리턴한다. 두 시각의 표현 포멧은 "yyyyMMdd"이다.

	 * <p> 1975년 2월 5일 태어난 사람의 현재 만 나이를 구하는 코드:
	 *  <p><blockquote><pre>
	 * int age=DateTimeUtil.ageBetween("19750205",getFormatString("yyyyMMdd"));
	 * </pre></blockquote>
	 * @param  from date string
	 * @param  to date string
	 * @return 두 날짜 사이의 년도 차이(나이)를 리턴한다.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>"yyyyMMdd"</code> 형식에 맞지 않는 경우.
	 */
	public static int ageBetween(String from, String to)
			throws ParseException {
		return ageBetween(from, to, "yyyyMMdd");
	}

	/**
	 * 현재 시각을 기준으로 인자로 전달된 날짜 사이의 연도 차이를 리턴한다. 날짜의 표현 포멧은 "yyyyMMdd"이다.
	 * return age between two date strings with default defined format.("yyyyMMdd")
	 *<p> 1975년 2월 5일 태어난 사람의 현재 만 나이를 구하는 코드:
	 *  <p><blockquote><pre>
	 * int age=DateTimeUtil.age("19750205");
	 * </pre></blockquote>
	 * @param birth 비교하고자 하는 기준이 되는 날짜
	 * @return 현재 날짜와 birth 사이의 년도 차이를 리턴한다.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>"yyyyMMdd'</code> 형식에 맞지 않는 경우.
	 * @see #ageBetween(String, String)
	 */
	public static int age(Date date, String birth) throws ParseException {
		return ageBetween(birth, getFormatString(date, "yyyyMMdd"), "yyyyMMdd");
	}

	/**
	 * 인자로 전달된 <code>from</code> 시각과  <code>to</code> 시각 사이의 년도 차이를 리턴한다. 두 시각의 표현 포멧은<code>format</code>을 사용한다.
	 * <p>1975년 2월 5일 태어난 사람의 현재 만 나이를 구하는 코드:
	 *  <p><blockquote><pre>
	 * int age=DateTimeUtil.ageBetween("19750205",getFormatString("yyyyMMdd"),"yyyyMMdd");
	 * </pre></blockquote>
	 * @param  from date string
	 * @param  to date string
	 * @param format 날짜를 표현할 때 사용할 표현 포멧을 나타내는 문자열.
	 * @return 두 날짜 사이의 년도 차이를 리턴한다.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static int ageBetween(String from, String to, String format)
			throws ParseException {
		return (int) (daysBetween(from, to, format) / 365);
	}

	/**
	 * 인자로 전달된 날짜 <code>s</code>를 기준으로 특정 일(day) 수를 더한 날짜를 표현하는 문자열을 리턴한다. 날짜의 포현 포멧은 "yyyyMMdd"를 사용한다.
	 * <p>2005년 2월 25일에서 일주일(7일) 후의 날짜를 표현하는 문자열을 얻어오는 코드 사용예:
	 * 	<p><blockquote><pre>
	 * String dateString=DateTimeUtil.addDays("20050225",7);
	 * </pre></blockquote>
	 * @param s 기준 날짜
	 * @param day 더할 일수
	 * @return 더해진 날짜를 표현하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>"yyyyMMdd"</code> 형식에 맞지 않는 경우.
	 */
	public static String addDays(String s, int day)
			throws ParseException {
		return addDays(s, day, "yyyyMMdd");
	}

	/**
	 * 오늘을 기준으로 특정 일(day) 수를 더한 날짜를 표현하는 문자열을 리턴한다. 날짜의 포현 포멧은 "yyyyMMdd"를 사용한다.
	 * <p>오늘을 기준으로 일주일(7일) 후의 날짜를 표현하는 문자열 얻어오는  코드 사용예:
	 * 	<p><blockquote><pre>
	 * String dateString=DateTimeUtil.addDays(7);
	 * </pre></blockquote>
	 * @param day 더할 일수
	 * @return 더해진 날짜를 표현하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>"yyyyMMdd"</code> 형식에 맞지 않는 경우.
	 */
	public static String addDays(Date date, int day) throws ParseException {
		return addDays(getShortDateString(date), day, "yyyyMMdd");
	}

	/**
	 * 오늘기준으로  특정 일(day) 수를 더한 날짜를 구한 후 인자로 전달된 <code>format</code> 형식으로 표현하는 문자열을 리턴한다.
	 * <p>오늘을 기준으로 일주일(7일) 후의 날짜를 "yyyy/MM/dd" 형식으로 표현하는 문자열 얻어오는  코드 사용예:
	 * 	<p><blockquote><pre>
	 * String dateString=DateTimeUtil.addDays(7,"yyyy/MM/dd");
	 * </pre></blockquote>
	 * @param day 더할 일수
	 * @param format 날짜 표현 포멧
	 * @return 더해진 날짜를 표현하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>"yyyyMMdd"</code> 형식에 맞지 않는 경우.
	 */
	public static String addDays(Date date, int day, String format)
			throws ParseException {
		String today = getShortDateString(date);
		String tmp = addDays(today, day, "yyyyMMdd");
		return changeFormat(tmp, "yyyyMMdd", format);
	}

	/**
	 * 인자로 전달된 날짜 <code>s</code> 에서  특정 일(day) 수를 더한 날짜를 인자로 전달된 <code>format</code> 형식으로 표현하는 문자열을 리턴한다.
	 * <p>2005년 2월 25일에서 일주일(7일) 후의 날짜를 표현하는 문자열 얻어오는  코드 사용예:
	 * 	<p><blockquote><pre>
	 * String dateString=DateTimeUtil.addDays("20050225",7,"yyyyMMdd");
	 * </pre></blockquote>
	 * 	@param s 기준 날짜
	 * @param day 더할 일수
	 * @param format 날짜 표현 포멧
	 * @return 더해진 날짜를 표현하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static String addDays(String s, int day, String format)
			throws ParseException {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		Date date = check(s, format);

		date.setTime(date.getTime() + ((long) day * 1000 * 60 * 60 * 24));
		return formatter.format(date);
	}
	/**
	 * 인자로 전달된 시각 <code>s</code> 에서  특정 시간(time)을  더한 시각을  인자로 전달된 <code>format</code> 형식으로 표현하는 문자열을 리턴한다.
	 * <p>2005년 2월 25일에서 일주일(7일) 후의 날짜를 표현하는 문자열 얻어오는  코드 사용예:
	 * 	<p><blockquote><pre>
	 * String dateString=DateTimeUtil.addDays("20050225",7,"yyyyMMdd");
	 * </pre></blockquote>
	 * 	@param s 기준 날짜
	 * @param time 더할 시간
	 * @param format 날짜 표현 포멧
	 * @return 더해진 시각을 표현하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static String addTimes(String s, int time, String format)
			throws ParseException {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		Date date = check(s, format);

		date.setTime(date.getTime() + ((long)  1000 * 60 * 60 * time));
		return formatter.format(date);
	}
	/**
	 * 현재시각 에서  특정 시간(time)을  더한 시각을  인자로 전달된 <code>format</code> 형식으로 표현하는 문자열을 리턴한다.
	 * <p>현재시각에서 23시간 후를 표현하는 문자열 얻어오는  코드 사용예:
	 * 	<p><blockquote><pre>
	 * String dateString=DateTimeUtil.addDays(23,"yyyy/MM/dd hh:mm");
	 * </pre></blockquote>
	 * @param time 더할 시간
	 * @param format 시각 표현 포멧
	 * @return 더해진 시각을 표현하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static String addTimes(Date date, int time, String format)
			throws ParseException {
		String fomatted=		getFormatString(date, format);
		return addTimes(fomatted,time,format);
	}

	/**
	 * 세계표준시(Universal Time Coordinated)를 "yyyy-MM-ddTHH:mm:ss:SSSZ" 형태의 포멧으로 리턴한다.
	 * @return UTC time
	 */
	public static String getUTCTimeString(Date date)
	{
		String ret="";
		try{
			ret=addTimes(date, -9,"yyyy-MM-dd HH:mm:ss:SSS ");
			char rets[]=ret.toCharArray();
			rets[10]='T';
			rets[23]='Z';
			ret=new String(rets);

		}catch(ParseException e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	/**
	 * 인자로 전달된 날짜 <code>s</code>를 기준으로 특정 개월(month) 수를 더한 날짜를 표현하는 문자열을 리턴한다. 날짜의 포현 포멧은 "yyyyMMdd"를 사용한다.
	 * <p>2005년 2월 25일에서 7개월 전의 날짜를 표현하는 문자열을 얻어오는 코드 사용예:
	 * 	<p><blockquote><pre>
	 * String monthString=DateTimeUtil.addDays("20050225",-7);
	 * </pre></blockquote>
	 * @param s 기준 날짜
	 * @param month 더할 개월수
	 * @return 더해진 날짜를 표현하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>"yyyyMMdd"</code> 형식에 맞지 않는 경우.
	 */
	public static String addMonths(String s, int month) throws ParseException {
		return addMonths(s, month, "yyyyMMdd");
	}

	/**
	 * 인자로 전달된 시각 <code>s</code> 에서  특정 개월(month)을  더한 시각을  인자로 전달된 <code>format</code> 형식으로 표현하는 문자열을 리턴한다.
	 * <p>2005년 2월 25일에서 7개월 후의 날짜를 표현하는 문자열 얻어오는  코드 사용예:
	 * 	<p><blockquote><pre>
	 * String dateString=DateTimeUtil.addDays("20050225",7,"yyyyMMdd");
	 * </pre></blockquote>
	 * 	@param s 기준 날짜
	 * @param addMonth 더할 개월
	 * @param format 날짜 표현 포멧
	 * @return 더해진 시각을 표현하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static String addMonths(String s, int addMonth, String format)
			throws ParseException {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		Date date = check(s, format);

		java.text.SimpleDateFormat yearFormat =
				new java.text.SimpleDateFormat("yyyy", java.util.Locale.KOREA);
		java.text.SimpleDateFormat monthFormat =
				new java.text.SimpleDateFormat("MM", java.util.Locale.KOREA);
		java.text.SimpleDateFormat dayFormat =
				new java.text.SimpleDateFormat("dd", java.util.Locale.KOREA);
		int year = Integer.parseInt(yearFormat.format(date));
		int month = Integer.parseInt(monthFormat.format(date));
		int day = Integer.parseInt(dayFormat.format(date));

		month += addMonth;
		if (addMonth > 0) {
			while (month > 12) {
				month -= 12;
				year += 1;
			}
		} else {
			while (month <= 0) {
				month += 12;
				year -= 1;
			}
		}
		java.text.DecimalFormat fourDf = new java.text.DecimalFormat("0000");
		java.text.DecimalFormat twoDf = new java.text.DecimalFormat("00");
		String tempDate =
				String.valueOf(fourDf.format(year))
						+ String.valueOf(twoDf.format(month))
						+ String.valueOf(twoDf.format(day));
		Date targetDate = null;

		try {
			targetDate = check(tempDate, "yyyyMMdd");
		} catch (ParseException pe) {
			day = lastDay(year, month);
			tempDate =
					String.valueOf(fourDf.format(year))
							+ String.valueOf(twoDf.format(month))
							+ String.valueOf(twoDf.format(day));
			targetDate = check(tempDate, "yyyyMMdd");
		}

		return formatter.format(targetDate);
	}
	/**
	 * 인자로 전달된 날짜 <code>s</code>를 기준으로 특정 연도(year) 수를 더한 날짜를 표현하는 문자열을 리턴한다. 날짜의 포현 포멧은 "yyyyMMdd"를 사용한다.
	 * <p>2005년 2월 28일에서 3년 전의 날짜를 표현하는 문자열을 얻어오는 코드 사용예:
	 * 	<p><blockquote><pre>
	 * String yearString=DateTimeUtil.addDays("20050228",-3);
	 * </pre></blockquote>
	 * @param s 기준 날짜
	 * @param year 더할 년수
	 * @return 더해진 날짜를 표현하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>"yyyyMMdd"</code> 형식에 맞지 않는 경우.
	 */
	public static String addYears(String s, int year)
			throws ParseException {
		return addYears(s, year, "yyyyMMdd");
	}
	/**
	 * 인자로 전달된 날짜 <code>s</code>를 기준으로 특정 연도(year) 수를 더한 날짜를 표현하는 문자열을 리턴한다. 날짜의 포현 포멧은 <code>format</code>를 사용한다.
	 * <p>2005년 2월 28일에서 3년 전의 날짜를 표현하는 문자열을 얻어오는 코드 사용예:
	 * 	<p><blockquote><pre>
	 * String yearString=DateTimeUtil.addDays("20050228",-3,"yyyyMMdd");
	 * </pre></blockquote>
	 * @param s 기준 날짜
	 * @param year 더할 년수
	 * @param format 날짜를 표현하는 포현 포멧
	 * @return 더해진 날짜를 표현하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static String addYears(String s, int year, String format)
			throws ParseException {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		Date date = check(s, format);
		date.setTime(
				date.getTime() + ((long) year * 1000 * 60 * 60 * 24 * (365 + 1)));
		return formatter.format(date);
	}

	/**
	 * 인자로 전달된 날짜 <code>src</code>에 해당하는 달의 마지막 날을 표현하는 날짜를 리턴한다. 날짜 표시 포멧으로 "yyyyMMdd"을 사용한다.
	 *  코드 사용예:
	 * 	<p><blockquote><pre>
	 * String datesStr=DateTimeUtil.lastDayOfMonth("20050203");
	 * datesStr.equals("20050228");
	 * </pre></blockquote>
	 * @param src 기준이 되는 날짜
	 * @return src에 날짜중 그 달의 마지막 날을 표시하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>"yyyyMMdd"</code> 형식에 맞지 않는 경우.
	 */

	public static String lastDayOfMonth(String src)
			throws ParseException {
		return lastDayOfMonth(src, "yyyyMMdd");
	}
	/**
	 * 인자로 전달된 날짜 <code>src</code>에 해당하는 달의 마지막 날을 표현하는 날짜를 리턴한다. 날짜 표시 포멧으로 <code>format</code>을 사용한다.
	 *  코드 사용예:
	 * 	<p><blockquote><pre>
	 * String datesStr=DateTimeUtil.lastDayOfMonth("20050203","yyyyMMdd");
	 * datesStr.equals("20050228");
	 * </pre></blockquote>
	 * @param src 기준이 되는 날짜
	 * @param format 날짜를 표시하는 표시포멧
	 * @return src에 날짜중 그 달의 마지막 날을 표시하는 문자열
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>format</code> 형식에 맞지 않는 경우.
	 */
	public static String lastDayOfMonth(String src, String format)
			throws ParseException {
		java.text.SimpleDateFormat formatter =
				new java.text.SimpleDateFormat(format, java.util.Locale.KOREA);
		Date date = check(src, format);

		java.text.SimpleDateFormat yearFormat =
				new java.text.SimpleDateFormat("yyyy", java.util.Locale.KOREA);
		java.text.SimpleDateFormat monthFormat =
				new java.text.SimpleDateFormat("MM", java.util.Locale.KOREA);

		int year = Integer.parseInt(yearFormat.format(date));
		int month = Integer.parseInt(monthFormat.format(date));
		int day = lastDay(year, month);

		java.text.DecimalFormat fourDf = new java.text.DecimalFormat("0000");
		java.text.DecimalFormat twoDf = new java.text.DecimalFormat("00");
		String tempDate =
				String.valueOf(fourDf.format(year))
						+ String.valueOf(twoDf.format(month))
						+ String.valueOf(twoDf.format(day));
		date = check(tempDate, format);

		return formatter.format(date);
	}

	/**
	 * 특정 연도 특정 월의 일수를 리턴한다.
	 * <p>2005 년 2월은 몇일이 존재하는지 확인하는  코드 사용예:
	 * 	<p><blockquote><pre>
	 * int days=DateTimeUtil.lastDay(2005,2);
	 * </pre></blockquote>
	 * @param year 년도
	 * @param month 개월
	 * @return 마지막 일자(일 수)
	 */
	private static int lastDay(int year, int month)
	{
		int day = 0;
		switch (month) {
			case 1 :
			case 3 :
			case 5 :
			case 7 :
			case 8 :
			case 10 :
			case 12 :
				day = 31;
				break;
			case 2 :
				if ((year % 4) == 0) {
					if ((year % 100) == 0 && (year % 400) != 0) {
						day = 28;
					} else {
						day = 29;
					}
				} else {
					day = 28;
				}
				break;
			default :
				day = 30;
		}
		return day;
	}
	/**
	 * 인자로 전달된 날짜를 나타내는 <code>s</code> 문자열이 "yyyy/MM/dd HH:mm" 형식에 맞는지 확인한다.
	 * @param s 확인하려는 날짜를 나타내는 문자열
	 * @return 인자로 전달된 날짜를 나타내는 문자열.
	 * @exception ParseException 잘못된 날짜이거나. 날짜를 표현하는 문자열이 <code>"yyyy/MM/dd HH:mm"</code> 형식에 맞지 않는 경우.
	 */
	public static String checkDateTime(String s) throws ParseException {
		check(s, "yyyy/MM/dd HH:mm");
		return s;
	}

	/**
	 * 기준 시각 (<code>checkTime</code>)이 시작시각(<code>startTime</code>) 과 종료시각(<code>endTime</code>) 사이에 위치하는지 여부를 리턴한다.
	 * 인자로 전달되는 시각들은 "HH:mm" 또는  "HH/mm" 형태의 포멧이어야 한다.
	 * <p>영업시간이 오전 9시부터 18시 30분까지 일 경우 현재 시간에 영업이 가능한 지 확인하는  코드 사용예:
	 * 	<p><blockquote><pre>
	 *	boolean isOpen = DateTimeUtil.isMiddleTime("09:00","18:30",getFormatString("HH:mm"));
	 * </pre></blockquote>
	 * @param startTime 시작 시각 ("HH:mm" or "HH/mm" 포멧)
	 * @param endTime 종료 시각 ("HH:mm" or "HH/mm" 포멧)
	 * @param checkTime 기준 시각 ("HH:mm" or "HH/mm" 포멧)
	 * @return <tt>true</tt> 기준 시각 (<code>checkTime</code>)이 시작시각(<code>startTime</code>) 과 종료시각(<code>endTime</code>) 사이에 위치한다.
	 * @throws ParseException 인자로 전달된 시각이 지정된 포멧("HH:mm" or "HH/mm" 에) 맞지 않거나 올바른 시간이 아닐경우 발생.
	 */
	public static boolean isMiddleTime(
			String startTime,
			String endTime,
			String checkTime)
			throws ParseException {
		Date a = getDateInstance(startTime);
		Date b = getDateInstance(endTime);
		Date c = getDateInstance(checkTime);
		return isMiddleTime(a, b, c);

	}
	/**
	 * 기준 시각 (<code>checkTime</code>)이 시작시각(<code>startTime</code>) 과 종료시각(<code>endTime</code>) 사이에 위치하는지 여부를 리턴한다.
	 * @param startTime 시작 시각
	 * @param endTime 종료 시각
	 * @param checkTime 기준 시각
	 * @return <tt>true</tt> 기준 시각 (<code>checkTime</code>)이 시작시각(<code>startTime</code>) 과 종료시각(<code>endTime</code>) 사이에 위치한다.
	 */
	public static boolean isMiddleTime(
			Date startTime,
			Date endTime,
			Date checkTime) {

		if (startTime.before(endTime)) {
			if (endTime.after(checkTime) && startTime.before(checkTime))
				return true;
		} else {
			if (endTime.after(checkTime) || startTime.before(checkTime)) {
				return true;
			}
		}
		return false;
	}
	/**
	 * 현재 시각이 시작시각(<code>startTime</code>) 과 종료시각(<code>endTime</code>) 사이에 위치하는지 여부를 리턴한다.
	 * 인자로 전달되는 시각들은 "HH:mm" 또는  "HH/mm" 형태의 포멧이어야 한다.
	 * <p>영업시간이 오전 9시부터 18시 30분까지 일 경우 현재 시간에 영업이 가능한 지 확인하는  코드 사용예:
	 * 	<p><blockquote><pre>
	 *	boolean isOpen = DateTimeUtil.isMiddleTime("09:00","18:30");
	 * </pre></blockquote>
	 * @param startTime 시작 시각 ("HH:mm" or "HH/mm" 포멧)
	 * @param endTime 종료 시각 ("HH:mm" or "HH/mm" 포멧)
	 * @return <tt>true</tt> 현재 시각이 시작 시각(<code>startTime</code>) 과 종료 시각(<code>endTime</code>) 사이에 위치한다.
	 * @throws ParseException 인자로 전달된 시각이 지정된 포멧("HH:mm" or "HH/mm" 에) 맞지 않거나 올바른 시간이 아닐경우 발생.
	 */
	public static boolean isMiddleTime(Date date, String startTime,String endTime) throws ParseException
	{

		String curTime=getFormatString(date, "HH:mm");
		return isMiddleTime(startTime,endTime,curTime);


	}

	/*
	 * 날짜형식 YYYYMMDD 를 YYYY년 MM월 DD일로 변경
	 */
	public static String getKoreaDateFormat(String yyyymmdd){
		String yyyy = yyyymmdd.substring(0,4);
		String mm = yyyymmdd.substring(4,6);
		String dd  = yyyymmdd.substring(6);

		String stryyyymmdd = yyyy + "년" + " " + (mm.startsWith("0")?(mm.substring(1)):mm) + "월" + " " + (dd.startsWith("0")?(dd.substring(1)):dd) + "일";

		return stryyyymmdd;
	}



}
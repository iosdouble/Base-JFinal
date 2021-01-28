package com.demo.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {

	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HHmmss");
	public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");
	public static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("yyMMdd");
	public static final DateTimeFormatter SHORT_DATE_FORMATTER_CHINA = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
	public static final DateTimeFormatter SHORT_DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyMMddHHmmss");
	public static final DateTimeFormatter SHORT_DATETIME_FORMATTER_CHINA = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");
	public static final DateTimeFormatter SHORT_DATETIME_FORMATTER1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter SHORT_DATETIME_FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	public static final DateTimeFormatter DATETIME_FORMATTER2 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSS");
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

	/**
	 * 返回当前的日期
	 *
	 * @return
	 */
	public static LocalDate getCurrentLocalDate() {
		return LocalDate.now();
	}

	/**
	 * 返回当前时间
	 *
	 * @return
	 */
	public static LocalTime getCurrentLocalTime() {
		return LocalTime.now();
	}

	/**
	 * 返回当前日期时间
	 *
	 * @return
	 */
	public static LocalDateTime getCurrentLocalDateTime() {
		return LocalDateTime.now();
	}

	/**
	 * yyyyMMdd
	 *
	 * @return
	 */
	public static String getCurrentDateStr() {
		return LocalDate.now().format(DATE_FORMATTER);
	}

	/**
	 * yyMMdd
	 *
	 * @return
	 */
	public static String getCurrentShortDateStr() {
		return LocalDate.now().format(SHORT_DATE_FORMATTER);
	}

	public static String getCurrentMonthStr() {
		return LocalDate.now().format(MONTH_FORMATTER);
	}

	/**
	 * yyyyMMddHHmmss
	 *
	 * @return
	 */
	public static String getCurrentDateTimeStr() {
		return LocalDateTime.now().format(DATETIME_FORMATTER);
	}

	/**
	 * yyyy年MM月dd日
	 *
	 * @return
	 */
	public static String getCurrentDateTimeStr3() {
		return LocalDateTime.now().format(SHORT_DATE_FORMATTER_CHINA);
	}

	/**
	 * yyyy年MM月dd日 HH时mm分ss秒
	 *
	 * @return
	 */
	public static String getCurrentDateTimeStr4() {
		return LocalDateTime.now().format(SHORT_DATETIME_FORMATTER_CHINA);
	}

	/**
	 * yyyy-MM-dd HH:mm:ss
	 *
	 * @return
	 */
	public static String getCurrentDateTimeStr0() {
		return LocalDateTime.now().format(SHORT_DATETIME_FORMATTER1);
	}

	public static String getCurrentDateTime() {
		return LocalDateTime.now().format(SHORT_DATETIME_FORMATTER2);
	}

	/**
	 * yyyyMMddHHmmssSS
	 *
	 * @return
	 */
	public static String getCurrentDateTimeStr2() {
		return LocalDateTime.now().format(DATETIME_FORMATTER2);
	}

	/**
	 * yyyyMMddHHmmss + 1hour
	 * 当前 + 1 小时
	 *
	 * @return
	 */
	public static String getCurrentDateTimeStr1() {
		return LocalDateTime.now().minusHours(-1).format(DATETIME_FORMATTER);
	}

	/**
	 * yyMMddHHmmss
	 *
	 * @return
	 */
	public static String getCurrentShortDateTimeStr() {
		return LocalDateTime.now().format(SHORT_DATETIME_FORMATTER);
	}

	/**
	 * HHmmss
	 *
	 * @return
	 */
	public static String getCurrentTimeStr() {
		return LocalTime.now().format(TIME_FORMATTER);
	}

	public static String getCurrentDateStr(String pattern) {
		return LocalDate.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String getCurrentDateTimeStr(String pattern) {
		return LocalDateTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String getCurrentTimeStr(String pattern) {
		return LocalTime.now().format(DateTimeFormatter.ofPattern(pattern));
	}

	public static LocalDate parseLocalDate(String dateStr, String pattern) {
		return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern(pattern));
	}

	public static LocalDateTime parseLocalDateTime(String dateTimeStr, String pattern) {
		return LocalDateTime.parse(dateTimeStr, DateTimeFormatter.ofPattern(pattern));
	}

	public static LocalTime parseLocalTime(String timeStr, String pattern) {
		return LocalTime.parse(timeStr, DateTimeFormatter.ofPattern(pattern));
	}

	public static String formatLocalDate(LocalDate date, String pattern) {
		return date.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String formatLocalDateTime(LocalDateTime datetime, String pattern) {
		return datetime.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static String formatLocalTime(LocalTime time, String pattern) {
		return time.format(DateTimeFormatter.ofPattern(pattern));
	}

	public static LocalDate parseLocalDate(String dateStr) {
		return LocalDate.parse(dateStr, DATE_FORMATTER);
	}

	public static LocalDateTime parseLocalDateTime(String dateTimeStr) {
		return LocalDateTime.parse(dateTimeStr, DATETIME_FORMATTER);
	}

	public static LocalTime parseLocalTime(String timeStr) {
		return LocalTime.parse(timeStr, TIME_FORMATTER);
	}

	public static String formatLocalDate(LocalDate date) {
		return date.format(DATE_FORMATTER);
	}

	public static String formatLocalDateTime(LocalDateTime datetime) {
		return datetime.format(DATETIME_FORMATTER);
	}

	public static String formatLocalTime(LocalTime time) {
		return time.format(TIME_FORMATTER);
	}

	/**
	 * 日期相隔天数
	 *
	 * @param startDateInclusive
	 * @param endDateExclusive
	 * @return
	 */
	public static int periodDays(LocalDate startDateInclusive, LocalDate endDateExclusive) {
		return Period.between(startDateInclusive, endDateExclusive).getDays();
	}

	/**
	 * 日期相隔小时
	 *
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long durationHours(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toHours();
	}

	/**
	 * 日期相隔分钟
	 *
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long durationMinutes(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toMinutes();
	}

	/**
	 * 日期相隔毫秒数
	 *
	 * @param startInclusive
	 * @param endExclusive
	 * @return
	 */
	public static long durationMillis(Temporal startInclusive, Temporal endExclusive) {
		return Duration.between(startInclusive, endExclusive).toMillis();
	}

	/**
	 * 是否当天
	 *
	 * @param date
	 * @return
	 */
	public static boolean isToday(LocalDate date) {
		return getCurrentLocalDate().equals(date);
	}

	public static Long toEpochMilli(LocalDateTime dateTime) {
		return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
	}

	public static Date getDate(String datetime) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = formatter.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getDateString(Date datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = formatter.format(datetime);
		return time;
	}

	public static String getDateStringDay(Date datetime) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String time = formatter.format(datetime);
		return time;
	}
	public static final Date dateTimeUTC(final String date) {
		String utc = null;
		utc = date.replace("Z", " UTC");
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
		Date d = null;
		try {
			d = format.parse(utc);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}

	/****
	 * 传入具体日期 ，返回具体日期增加一个月。
	 * @param date 日期(2017-04-13)
	 * @return 2017-05-13
	 * @throws ParseException
	 */
	public static String subMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dt = null;
		try {
			dt = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.MONTH, 1);

		Date dt1 = rightNow.getTime();
		String reStr = sdf.format(dt1);
		return reStr;
	}

	/**
	 * 当前月份之前一个月
	 * @param date
	 * @return
	 */
	public static String lastMonth(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dt = null;
		try {
			dt = sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(dt);
		rightNow.add(Calendar.MONTH, -1);

		Date dt1 = rightNow.getTime();
		String reStr = sdf.format(dt1);
		return reStr;
	}

	public static Date getYearBegin(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		//设置月和日都为1，即为开始时间（注：月份是从0开始;日中0表示上个月最后一天，1表示本月开始第一天）
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		//将小时置为0
		cal.set(Calendar.HOUR_OF_DAY, 0);
		//将分钟置为0
		cal.set(Calendar.MINUTE, 0);
		//将秒置为0
		cal.set(Calendar.SECOND,0);
		//将毫秒置为0
		cal.set(Calendar.MILLISECOND, 0);
		return cal.getTime();
	}

	public static Date getYearEnd(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		//设置月为12，月份从0开始
		cal.set(Calendar.MONTH, 11);
		//设置为当月最后一天
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		//将小时置为23
		cal.set(Calendar.HOUR_OF_DAY, 23);
		//将分钟置为59
		cal.set(Calendar.MINUTE, 59);
		//将秒置为59
		cal.set(Calendar.SECOND,59);
		//将毫秒置为999
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}

	public static String getBeforDay(String dateStr){
		Date date = getDateNew(dateStr);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, +1);
		date = calendar.getTime();
		String dateString = getDateString(date);
		return dateString.substring(0,10);
	}

	public static Date getDateNew(String datetime) {
		Date date = null;
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = formatter.parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
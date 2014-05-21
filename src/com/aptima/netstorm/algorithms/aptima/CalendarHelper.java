package com.aptima.netstorm.algorithms.aptima;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarHelper {

	private static long numberOfMSInADay = 1000 * 60 * 60 * 24;

	// we need a "zero" date
	// this is for bitcoin, set from other code for other datasets
	// january is month 0
	public static Calendar startDateForBinning = new GregorianCalendar(2009, 0, 3);

	//2009-01-03
	private static DateFormat bitcoinFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	private static DateFormat getDateFormat() {
		DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		return formatter;
	}

	public static Calendar parseBitcoin(String calendarString) {
		try {
			Date date = bitcoinFormatter.parse(calendarString);
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Calendar parse(String calendarString) {
		try {
			Date date = getDateFormat().parse(calendarString);
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			return cal;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String currentDateAsString() {
		return toString(Calendar.getInstance());
	}

	public static String toString(Calendar c) {
		return getDateFormat().format(c.getTime());
	}

	public static int weekFromBinStart(Calendar date) {

		return numberOfDaysBetween(startDateForBinning, date) / 7;
	}

	public static int daysFromBinStart(Calendar date) {

		return numberOfDaysBetween(startDateForBinning, date);
	}

	public static int numberOfDaysBetween(Calendar start, Calendar end) {

		long span = end.getTimeInMillis() - start.getTimeInMillis();
		return (int) (span / numberOfMSInADay);
	}

	public static void main(String[] args) {
		
		Calendar startDT = CalendarHelper.parseBitcoin("2009-01-04 12:15:05");
		int bin = CalendarHelper.weekFromBinStart(startDT);
		System.out.println(bin);
		
		Calendar endDT = CalendarHelper.parseBitcoin("2013-04-10 14:22:50");
		bin = CalendarHelper.weekFromBinStart(endDT);
		System.out.println(bin);		
		
		//Calendar c = new GregorianCalendar();
		//System.out.println(c.toString());
		//c.clear();
		//System.out.println(c.toString());
		//System.out.println(CalendarHelper.toString(c));
		//c.set(2001, Calendar.JANUARY, 1);
		//System.out.println(CalendarHelper.toString(c));
	}
}

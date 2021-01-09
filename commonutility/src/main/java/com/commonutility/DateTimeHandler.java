package com.commonutility;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;


public final class DateTimeHandler {

    public final static String DATE_FORMATE_1 = "dd MMM yyyy hh:mm aaa";
    public final static String DATE_FORMATE_2 = "dd-MMM-yyyy hh:mm aaa";
    public final static String DATE_FORMATE_6 = "yyyy-MM-dd hh:mm";
    public static final String DATE_FORMATE_3 = "dd-MM-yyyy hh:mm:ss";
    public static final String DATE_FORMATE_4 = "hh:mm aaa";
    public static final String DATE_FORMATE_7 = "hh:mm";
    public static final String DATE_FORMATE_5 = "yyyy-MM-dd";
    public static final String DATE_FORMATE_8 = "dd-MMM-yyyy";
    public final static String DATE_FORMATE_9 = "yyyy-MM-dd HH:mm";
    public final static String DATE_FORMATE_10 = "HH:mm";
    public final static String DATE_FORMATE_11 = "dd-MM-yyyy hh:mm aaa";
    public final static String DATE_FORMATE_12 = "yyyy-MM-dd HH:mm:SS";
    public final static String ddMMyy = "dd/MM/yy";
    public final static String MMddyy1 = "MM/dd/yy hh:mm:ss aaa";
    public final static String MMddyy = "MM/dd/yy";


    public static String changeFormateInString(String dateStr,String oldformat,String newFormate) throws ParseException {
        Date date = changeStringToDate(dateStr,oldformat);
        return changeDateToString(date,newFormate);
    }

    public static Date changeFormateInDate(String dateStr,String oldformat,String newFormate) throws ParseException {
        Date date = changeStringToDate(dateStr,oldformat);
        String newDt = changeDateToString(date,newFormate);
        return changeStringToDate(newDt,newFormate);
    }


    public static String changeDateToString(Date date, String formate) {
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        return formatter.format(date);
    }

    public static String changeDateToString(Date date, String formate,String language) {
        SimpleDateFormat formatter = new SimpleDateFormat(formate,new Locale(language));
        return formatter.format(date);
    }

    public static String getTodayDateTimeInString(String formate) {
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        return formatter.format(new Date());
    }


    public static Date changeStringToDate(String date, String formate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        return formatter.parse(date);
    }

    public static Date changeStringToDate(String date, String formate,String language) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formate,new Locale(language));
        return formatter.parse(date);
    }


    public static Date getTodayDateTime(String formate) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formate);
        return formatter.parse(changeDateToString(new Date(), formate));
    }


    public static String getTime(String dateTime) {
        String time = dateTime;
        try {
            Date chatDate = changeStringToDate(dateTime, DATE_FORMATE_1);
            time = changeDateToString(chatDate, DATE_FORMATE_4);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static String getTime(String dateTime,String dateTimeFormate,String dateFormate) {
        String time = dateTime;
        try {
            Date chatDate = changeStringToDate(dateTime,dateTimeFormate);
            time = changeDateToString(chatDate,dateFormate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static String getDate(String dateTime) {
        String time = dateTime;
        try {
            Date today = changeStringToDate(dateTime, DATE_FORMATE_1);
            time = changeDateToString(today, DATE_FORMATE_5);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }


    public static String getDate(String dateTime,String dateTimeFormate,String timeFormate) {
        String time = dateTime;
        try {
            Date today = changeStringToDate(dateTime, dateTimeFormate);
            time = changeDateToString(today, timeFormate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }



    public static long getDifferenceInSecond(String dateStr) {
        long diff = -1;
        try {
            Date msgDate = changeStringToDate(dateStr,DATE_FORMATE_1);
            Date currentDate = new Date();

            diff = Math.abs(msgDate.getTime() - currentDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff / 1000;
    }

    public static long getDifferenceInSecondFromToday(String dateStr,String format) {
        long diff = -1;
        try {
            Date msgDate = changeStringToDate(dateStr,format);
            Date currentDate = new Date();

            diff = Math.abs(msgDate.getTime() - currentDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff / 1000;
    }


    public static long getDifferenceInSecond(String dateStr, String currentDateStr) {
        long diff = -1;
        try {
            Date msgDate = changeStringToDate(dateStr, DATE_FORMATE_1);
            Date currentDate = changeStringToDate(currentDateStr, DATE_FORMATE_1);

            diff = Math.abs(msgDate.getTime() - currentDate.getTime());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return diff / 1000;
    }

//    public static Date concatDateTime(String date, String time) throws ParseException {
//        String dateTime = date + " " + time;
//        SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMATE_1);
//        return formatter.parse(dateTime);
//    }

    public static String convertDateLocalToUTC(String dateStr, String format) throws ParseException {
        Date date = changeStringToDate(dateStr, format);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(date);
    }

    public static String convertDateUTCtoLocal(String dateStr, String format) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = df.parse(dateStr);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }


    public static String convertDateLocalToTimeZone(String dateStr, String format, String timeZone) throws ParseException {
        Date date = changeStringToDate(dateStr, format);
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
        return formatter.format(date);
    }

    public static String convertDateTimeZoneToLocal(String dateStr, String format, String timeZone) throws ParseException {
        SimpleDateFormat df = new SimpleDateFormat(format);
        df.setTimeZone(TimeZone.getTimeZone(timeZone));
        Date date = df.parse(dateStr);
        df.setTimeZone(TimeZone.getDefault());
        return df.format(date);
    }
}

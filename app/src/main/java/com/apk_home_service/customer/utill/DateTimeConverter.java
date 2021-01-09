package com.apk_home_service.customer.utill;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by Arpit Arora on 17/01/2018.
 * The aim of the class is to convert various date formats into the required format
 */

public class DateTimeConverter {

    /**
     * This function will return current device time if no time or input format is provided
     *
     * @param ctx          Context
     * @param inputFormat  input pattern
     * @param outputFormat output pattern you required
     * @param mDate        date which is to be converted
     * @return returns date in string format as specified.
     */
    public static String convert(Context ctx, String inputFormat, String outputFormat, String mDate, Date date) {
        String formattedDate = "N/A";
        Locale locale;

        inputFormat = inputFormat.replace("/mm/", "/MM/")
                .replace("-mm-", "-MM-");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = ctx.getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = ctx.getResources().getConfiguration().locale;
            }
            if (!is_valid(outputFormat)) {
                outputFormat = "yyyy/MM/dd";
            }

            SimpleDateFormat mOutputDateFormat = new SimpleDateFormat(outputFormat, locale);

            if (date == null) {
                if (is_valid(inputFormat) && is_valid(mDate)) {
//                    mDate = convertDateUtcToDefault(mDate,inputFormat);
                    SimpleDateFormat mInputDateFormat = new SimpleDateFormat(inputFormat, locale);
                    Date inputDate = mInputDateFormat.parse(mDate);
                    formattedDate = mOutputDateFormat.format(inputDate);
                } else {
                    Date inputDate = Calendar.getInstance().getTime();
                    formattedDate = mOutputDateFormat.format(inputDate);
                }
            } else {
                formattedDate = mOutputDateFormat.format(date);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("formattedDate", formattedDate);

        Log.d("inputFormat" , inputFormat);
        Log.d("outputFormat" , outputFormat);
        Log.d("mDate" , mDate);
        return formattedDate;
    }
 public static String convertInUTC(Context ctx, String inputFormat, String outputFormat, String mDate, Date date) {
        String formattedDate = "N/A";
        Locale locale;

        inputFormat = inputFormat.replace("/mm/", "/MM/")
                .replace("-mm-", "-MM-");
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = ctx.getResources().getConfiguration().getLocales().get(0);
            } else {
                locale = ctx.getResources().getConfiguration().locale;
            }
            if (!is_valid(outputFormat)) {
                outputFormat = "yyyy/MM/dd";
            }

            SimpleDateFormat mOutputDateFormat = new SimpleDateFormat(outputFormat, locale);

            if (date == null) {
                if (is_valid(inputFormat) && is_valid(mDate)) {
                    mDate = convertDateUtcToDefault(mDate,inputFormat);
                    SimpleDateFormat mInputDateFormat = new SimpleDateFormat(inputFormat, locale);
                    Date inputDate = mInputDateFormat.parse(mDate);
                    formattedDate = mOutputDateFormat.format(inputDate);
                } else {
                    Date inputDate = Calendar.getInstance().getTime();
                    formattedDate = mOutputDateFormat.format(inputDate);
                }
            } else {
                formattedDate = mOutputDateFormat.format(date);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("formattedDate" , formattedDate);

        Log.d("inputFormat" , inputFormat);
        Log.d("outputFormat" , outputFormat);
        Log.d("mDate" , mDate);
        return formattedDate;
    }

    public static String convertDateUtcToDefault(String mDate, String format) {
        try {
            format = format.replace("-mm-", "-MM-")
                    .replace("/mm/", "/MM/");
            SimpleDateFormat inputFormat = new SimpleDateFormat(format);
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date value = inputFormat.parse(mDate);
            SimpleDateFormat outputFormat = new SimpleDateFormat(format, Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getDefault());
            mDate = outputFormat.format(value);
        } catch (Exception e) {
            Log.e("convertDateUtcToDefault", e.toString());
        }
        return mDate;
    }
    private static boolean is_valid(String str) {
        return !(str == null || str.equalsIgnoreCase("null") || str.trim().length() == 0);
    }

    /**
     * @param d1
     * @param d2
     * @param formatToCheck
     * @param value         1: to check after
     *                      2: to check before
     *                      3: to check equal
     * @return
     */
    public static boolean compareDates(String d1, String d2, String formatToCheck, int value) {
        try {
            // If you already have date objects then skip 1

            //1
            // Create 2 dates starts
            Log.d("===date" , d1);
            Log.d("===date==d2" , d2);
            SimpleDateFormat sdf = new SimpleDateFormat(formatToCheck);
            Date date1 = sdf.parse(d1);
            Date date2 = sdf.parse(d2);

            Log.d("Date1" , sdf.format(date1));
            Log.d("Date2" , sdf.format(date2));

            return compareDates(date1, date2, value);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static boolean compareDates(Date date1, Date date2, int value) {
        // if you already have date objects then skip 1
        //1

        //1
        Log.d("compareDates",value + "==arpit==Date1" + date1 + "==DATE 2 " + date2 + "=after=" + date1.after(date2));
        Log.d("compareDates",value + "==arpit==Date1" + date1 + "==DATE 2 " + date2 + "=before=" + date1.before(date2));
        Log.d("compareDates",value + "==arpit==Date1" + date1 + "==DATE 2 " + date2 + "=equal=" + date1.equals(date2));
        Log.d("compareDates",value + "==arpit==Date1" + date1 + "==DATE 2 " + date2 + "=equal=" + date1.toString().equals(date2.toString()));
//        Log.d("Date2"+sdf.format(date2));

        //date object is having 3 methods namely after,before and equals for comparing
        //after() will return true if and only if date1 is after date 2
        if (value == 1) {
            boolean b = false;
            SimpleDateFormat fmt = new SimpleDateFormat("MM/yyyy");
            Log.d("==EQUALITY CHECK 1== " , String.valueOf((fmt.format(date1).equals(fmt.format(date2)))));
            Log.d("==EQUALITY CHECK 2== " , String.valueOf(date1.after(date2)));
            Log.d("==EQUALITY CHECK 3== " , String.valueOf((b = ((fmt.format(date1).equals(fmt.format(date2))) || date1.after(date2)))));
            return date1.after(date2);
        } else if (value == 2) {
            Log.d("","Date1 is before Date2");
            return date1.before(date2);
        }

        //equals() returns true if both the dates are equal

        else if (value == 3) {
            SimpleDateFormat fmt = new SimpleDateFormat("MM/yyyy");
            return fmt.format(date1).equals(fmt.format(date2));
        } else {
            return false;
        }
    }

    private String formats(String format) {
/*        h:mm a                        //12:08 PM
        yyyy-MM-dd
        dd-MM-yyyy
        yyyy-MM-dd kk:mm:ss
        yyyy-MM-dd HH:mm:ss
        MM/dd/yyyy HH:mm:ss
        dd MMM yyyy
        MMM dd, yyyy hh:mm:ss aaa//   Mar 10, 2016 6:30:00 PM
        E, MMM dd yyyy                Fri, June 7 2013
        EEEE, MMM dd, yyyy HH:mm:ss a   //Friday, Jun 7, 2013 12:10:56 PM


        No.	Format	Example
        1	dd/mm/yy	03/08/06
        2	dd/mm/yyyy	03/08/2006
        3	d/m/yy	3/8/06
        4	d/m/yyyy	3/8/2006
        5	ddmmyy	030806
        6	ddmmyyyy	03082006
        7	ddmmmyy	03Aug06
        8	ddmmmyyyy	03Aug2006
        9	dd-mmm-yy	03-Aug-06
        10	dd-mmm-yyyy	03-Aug-2006
        11	dmmmyy	3Aug06
        12	dmmmyyyy	3Aug2006
        13	d-mmm-yy	3-Aug-06
        14	d-mmm-yyyy	3-Aug-2006
        15	d-mmmm-yy	3-August-06
        16	d-mmmm-yyyy	3-August-2006
        17	yymmdd	060803
        18	yyyymmdd	20060803
        19	yy/mm/dd	06/08/03
        20	yyyy/mm/dd	2006/08/03
        21	mmddyy	080306
        22	mmddyyyy	08032006
        23	mm/dd/yy	08/03/06
        24	mm/dd/yyyy	08/03/2006
        25	mmm-dd-yy	Aug-03-06
        26	mmm-dd-yyyy	Aug-03-2006
        27	yyyy-mm-dd	2006-08-03
        28	weekday, dth mmmm yyyy	Monday, 3 of August 2006
        29	weekday	Monday
        30	mmm-yy	Aug-06
        31	yy	06
        32	yyyy	2006
        33	dd-mmm-yyyy time	03-Aug-2006 18:55:30.35
        34	yyyy-mm-dd time24 (ODBC Std)	2006-08-03 18:55:30
        35	dd-mmm-yyyy time12	03-Aug-2006 6:55:30 pm
        36	time24	18:55:30
        37	time12	6:55:30 pm
        38	hours	48:55:30
        39	seconds	68538.350*/
        return "";
    }
}
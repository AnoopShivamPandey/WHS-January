package com.commonutility.Database;

import android.content.Context;
import android.content.SharedPreferences;


import java.lang.reflect.Field;

/**
 * Created by Rajnish on 7/7/2017.
 */

public class PrefrenceHandler {
    public static SharedPreferences preferences = null;

    private final static String LOGIN_STATUS = "LOGIN_STATUS";

    private PrefrenceHandler(Context context) {
        preferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }

    private PrefrenceHandler() {

    }

    public static SharedPreferences getPreferences(Context context) {
        if (preferences == null) {
            new PrefrenceHandler(context);
        }
        return preferences;
    }

    public static void putObjectValue(Object object, Context context) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            Object value = field.get(object);
            if (value != null) {
                getPreferences(context).edit().putString(field.getName(), value.toString().trim()).commit();
            } else {
                getPreferences(context).edit().putString(field.getName(), "").commit();
            }
        }
    }


    public static Object getObjectValue(Object object, Context context) throws IllegalAccessException {
        for (Field field : object.getClass().getDeclaredFields()) {
            field.set(object, getPreferences(context).getString(field.getName(), ""));
        }
        return object;
    }


    public static void putStringValue(String key, String value, Context context) {
        getPreferences(context).edit().putString(key, value).commit();
    }

    public static String getStringValue(String key, Context context) {
        return getPreferences(context).getString(key, "");
    }

    public static void setLogin(Context context) {
        getPreferences(context).edit().putString(LOGIN_STATUS, "Login").commit();
    }

    public static void setLogout(Context context) {
        getPreferences(context).edit().putString(LOGIN_STATUS, "Logout").commit();
//        getPreferences(context).edit().clear().commit();
    }

    public static boolean isLogin(Context context) {
        return getPreferences(context).getString(LOGIN_STATUS, "").equalsIgnoreCase("login");
    }
}

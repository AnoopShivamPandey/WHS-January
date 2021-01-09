package com.apk_home_service.customer.utill;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


import java.util.Map;



public class Constants {

    public static final String PREF_KEY = "MyTreatPref";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static String USER_ID = "user_id";
    public static String FIREBASE_TOKEN = "firebase_token";

    public static boolean savePreferences(Context c, String key, String value) {
        SharedPreferences sp = initializeSharedPreferences(c);
        if (sp != null) {
            Editor editor = sp.edit();
            editor.putString(key, value);
            return editor.commit();
        }
        return false;
    }

    public static String getSavedPreferences(Context c, String key,
                                             String defValue) {
        SharedPreferences sp = initializeSharedPreferences(c);
        if (sp != null) {
            return sp.getString(key, defValue);
        }
        return defValue;
    }

    public static Map<String, ?> getAllPreferences(Context c) {
        SharedPreferences sp = initializeSharedPreferences(c);
        return sp.getAll();
    }

    private static SharedPreferences initializeSharedPreferences(Context c) {
        if (c != null)
            return c.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE);
        return null;
    }

    public static boolean clearSavedPreferences(Context c, String key) {
        SharedPreferences sp = initializeSharedPreferences(c);
        Editor editor = sp.edit();
        editor.remove(key);
        return editor.commit();
    }

    public static boolean clearAllSavedPreferences(Context c) {
        SharedPreferences sp = initializeSharedPreferences(c);
        if (sp != null) {
            Editor editor = sp.edit().clear();
            return editor.commit();
        }
        return false;
    }

    public static boolean isUserLoggedIn(Context context) {
        if (Constants.getSavedPreferences(context, Constants.USER_ID, "")
                .length() > 0) {
            return true;
        }
        return false;
    }


    public static String getUserID(Context context) {
        if (Constants.getSavedPreferences(context, Constants.USER_ID, "")
                .length() > 0) {
            return Constants.getSavedPreferences(context, Constants.USER_ID, "");
        }
        return "";
    }


}

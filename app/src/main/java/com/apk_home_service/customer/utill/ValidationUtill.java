package com.apk_home_service.customer.utill;

import android.util.Patterns;

import java.util.regex.Pattern;


public class ValidationUtill {
//    private static final String PASSWORD_PATTERN =
//            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[ @#*&$%!^()?;:'\"<>,.]).{8,15})";


    private static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[A-Z]).{8,15})";

//    private static final String EMAIL_PATTERN =
//            "^[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";


    private static final String EMAIL_PATTERN =
            "^[a-z0-9_]+(?:\\.[a-z0-9]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$";


    private static final String ONLY_TEXT_PATTERN =
            "^[a-zA-Z ]*$";

    private static final String POSTAL_CODE =
//            "^[A-Z0-9{\\-() }]{3,15}$";
            "^[A-Z0-9{\\-() }]{3,15}$";

    private static final String PHONE_NUMBER =
//            "^(^[+]([0-9]{6,20}))$";
//            "^(([0-9+ ()-]{6,20}))$";
            "^(([0-9+]{6,20}))$";
//            "^(([0-9]{8,15}))$";

//    private static final String CAR_NUMBER =
//            "^((^[2])((a|A)?(b|B)?(c|C)?(d|D)?(e|E)?(f|F)?(g|G)?(h|H)?(i|I)?(j|J)?(k|K)?(l|L)?(m|M)?(n|N)?(o|O)?(p|P)?(q|Q)?(r|R)?(s|S)?(t|T)?(u|U)?(v|V)?(w|W)?(x|X)?(y|Y)?(z|Z)?)[-]([0-9]{4}))$";


    private static final String URL = "(^(https|http|ftp|file|www):\\/\\/[-a-zA-Z0-9+&@#\\/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#\\/%=~_|])";


    public static boolean isValidPassword(String password) {
        return (Pattern.compile(PASSWORD_PATTERN).matcher(password).matches());
    }

    public static boolean isValidEmailOld(String email) {
        return (Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    public static boolean isValidEmail(String email) {
        return (Pattern.compile(EMAIL_PATTERN).matcher(email).matches());
    }

    public static boolean isValidPhoneNumber(String phone) {
//        String num = "0123456789";
        if (phone.indexOf("+") > 0)
            return false;
        return (Pattern.compile(PHONE_NUMBER).matcher(phone).matches());
    }

    public static boolean isValidPostalCode(String potalCode) {
        return (Pattern.compile(POSTAL_CODE).matcher(potalCode).matches());
    }


    public static boolean isValidName(String name) {
        return (Pattern.compile(ONLY_TEXT_PATTERN).matcher(name).matches());
    }

    public static boolean isValidURL(String name) {
        return (Pattern.compile(URL).matcher(name).matches());
    }

    public static boolean isValid(String text, String pattern) {
        return (Pattern.compile(pattern).matcher(text).matches());
    }

}

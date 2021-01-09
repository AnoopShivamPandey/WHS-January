package com.commonutility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.IBinder;
import android.provider.Settings;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.okhttp.RequestBody;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import okio.Buffer;


public class CommonUtill {
    public static void printLog(Context context, String tag, String message) {
        Log.d(context.getPackageName() + " " + tag, message);
    }

    public static void print(Context context, String message) {
        System.out.println(context.getPackageName() + "---> " + message);
    }

    public static void printLog(String tag, String message) {
        Log.d(tag, message);
    }

    public static void print(String message) {
        System.out.println(message);
    }


    public static void showTost(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void setFont(Context context, Typeface font, View view) {
        if (view instanceof Button)
            ((Button) view).setTypeface(font);
        else if (view instanceof TextView)
            ((TextView) view).setTypeface(font);
        else if (view instanceof EditText)
            ((EditText) view).setTypeface(font);
    }

    public static void setFont(Context context, Typeface font, int style, View view) {
        if (view instanceof Button)
            ((Button) view).setTypeface(font, style);
        else if (view instanceof TextView)
            ((TextView) view).setTypeface(font, style);
        else if (view instanceof EditText)
            ((EditText) view).setTypeface(font, style);
    }


    public static String getDeviceId(Context pContext) {
        String identifier = Settings.Secure.getString(pContext.getContentResolver(), Settings.Secure.ANDROID_ID);
        return identifier;
    }

    public static void hideSoftKeyboard(Activity activity) {
        try {
            if (activity != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) activity
                        .getSystemService(Activity.INPUT_METHOD_SERVICE);
                View v = activity.getCurrentFocus();
                if (v != null) {
                    IBinder binder = activity.getCurrentFocus()
                            .getWindowToken();
                    if (binder != null) {
                        inputMethodManager.hideSoftInputFromWindow(binder, 0);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void openBrowser(Activity activity, String url) {
        try {
            if (!url.startsWith("http://") && !url.startsWith("https://"))
                url = "http://" + url;
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            activity.startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void printFormData(HashMap<String, String> dataMap) {
        Set<String> set = dataMap.keySet();
        Iterator<String> iterator = set.iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            CommonUtill.print(key + "==" + dataMap.get(key));
        }
    }


    public static void printClassData(Object object) {
        Gson gson = new Gson();
        CommonUtill.print(object.getClass().getName() + " ==" + gson.toJson(object));
    }


    /*public static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }*/

    public static String fbKeyHash(Activity activity) {
        String keyHash = "";
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                keyHash = Base64.encodeToString(md.digest(), Base64.DEFAULT);
//                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return keyHash;
    }


    public static String linkedInHashkey(Activity activity) {
        String keyHash = "";
        try {
            PackageInfo info = activity.getPackageManager().getPackageInfo(
                    activity.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());

                keyHash = Base64.encodeToString(md.digest(),
                        Base64.NO_WRAP);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Name not found", e.getMessage(), e);

        } catch (NoSuchAlgorithmException e) {
            Log.d("Error", e.getMessage(), e);
        }
        return keyHash;
    }


    public static void shareApp(Activity act, String msg, String app) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                msg + ": " + "https://play.google.com/store/apps/details?id=" + app);
        sendIntent.setType("text/plain");
        act.startActivity(sendIntent);
    }


    public static String trimString(String myStr, String ch, int num) {

        if (myStr.lastIndexOf(ch) > 0 && (myStr.lastIndexOf(ch) + num) < myStr.length()) {
            return myStr.substring(0, myStr.lastIndexOf(ch) + num);
        }

        return myStr;
    }

    public static SpannableString decorString(String myStr, int decorType, int start, int end, int flag) {
        SpannableString content = new SpannableString(myStr);

        switch (decorType) {
            case 0:
                content.setSpan(new UnderlineSpan(), start, end, flag);
                break;
        }

        return content;
    }







public void createNotification(String title, String message) {
        /**Creates an explicit intent for an Activity in your app**/
//        Intent resultIntent = new Intent(this , SomeOtherActivity.class);
//        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(this,
                0 /* Request code */, new Intent(),
                PendingIntent.FLAG_UPDATE_CURRENT);

        final int icon = R.mipmap.ic_launcher_round;
        final Uri alarmSound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                + "://" + getPackageName() + "/raw/notification");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.ic_launcher);
        mBuilder.setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(icon)
                .setSound(alarmSound)
                .setAutoCancel(false)
                .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                .setContentIntent(resultPendingIntent);

        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "NOTIFICATION_CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setShowBadge(true);
            notificationChannel.setSound(alarmSound, attributes);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            assert mNotificationManager != null;
            mBuilder.setChannelId(NOTIFICATION_CHANNEL_ID);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        assert mNotificationManager != null;
        mNotificationManager.notify(0 /* Request Code */, mBuilder.build());
    }





}









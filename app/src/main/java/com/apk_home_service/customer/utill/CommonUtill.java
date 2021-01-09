package com.apk_home_service.customer.utill;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;


public class CommonUtill {
    public static void printLog(Context context, String tag, String message) {
        Log.d(context.getPackageName() + " " + tag, message);
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;


        return (dist * 1000);
    }


    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
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

    public static void setFont(Context context, String font, View view) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), font);
        if (view instanceof Button)
            ((Button) view).setTypeface(typeface);
        else if (view instanceof TextView)
            ((TextView) view).setTypeface(typeface);
        else if (view instanceof EditText)
            ((EditText) view).setTypeface(typeface);
    }

    public static void setFont(Context context, String font, int style, View view) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), font);
        if (view instanceof Button)
            ((Button) view).setTypeface(typeface, style);
        else if (view instanceof TextView)
            ((TextView) view).setTypeface(typeface, style);
        else if (view instanceof EditText)
            ((EditText) view).setTypeface(typeface, style);
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

    public static boolean appInstalledOrNot(Activity act, String uri) {
        PackageManager pm = act.getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


    public static void downloadApp(Activity act, String pkg) {
        try {
            act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + pkg)));
        } catch (android.content.ActivityNotFoundException anfe) {
            act.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + pkg)));
        }
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public static boolean copyToClipboard(Context context, String text) {
        try {
            int sdk = android.os.Build.VERSION.SDK_INT;
            if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
                android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context
                        .getSystemService(context.CLIPBOARD_SERVICE);
                clipboard.setText(text);
            } else {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context
                        .getSystemService(context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData
                        .newPlainText(
                                (
                                        "Coupon copied"), text);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Coupon code copied.", Toast.LENGTH_SHORT).show();

            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void shareApp(Activity act, String msg, String app) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT,
                msg + ": " + "https://play.google.com/store/apps/details?id=" + app);
        sendIntent.setType("text/plain");
        act.startActivity(sendIntent);
    }


    public static File saveBitmap(Bitmap finalBitmap, String folderNAme,
                                  String picName, Activity act, String format, Bitmap.CompressFormat form,
                                  int quality) {
        String getRoot = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(getRoot + "/" + folderNAme);
        myDir.mkdirs();
        Long n = System.currentTimeMillis();
        String fname = picName + n + format;
        File file = new File(myDir, fname);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(form, quality, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(act.getApplicationContext(),
                "Your image is saved in " + myDir,
                Toast.LENGTH_LONG).show();
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        act.sendBroadcast(mediaScanIntent);
        return file;
    }


    public static boolean isStorageAvailable(Context ctx) {
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Toast.makeText(ctx, "sd card is not writeable", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(ctx, "SD card  is not available..!!",
                    Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    public static void changeToDone(Activity act, int resId, ProgressDialog mProgressDialog) {
        //Getting a progressBar from dialog

        ProgressBar bar = (ProgressBar) mProgressDialog.findViewById(android.R.id.progress);
        //Getting a DONE(new) drawable from resources
        Drawable drawable = act.getResources().getDrawable(resId);
        //Getting a drawable from progress dialog
        Drawable indeterminateDrawable = bar.getIndeterminateDrawable();
        //Obtain a bounds of current drawable
        Rect bounds = indeterminateDrawable.getBounds();
        //Set bounds to DONE(new) drawable
        drawable.setBounds(bounds);
        //Set a new drawable
        bar.setIndeterminateDrawable(drawable);

//        mProgressDialog.setTitle("Done.");
//        mProgressDialog.setMessage("Connected.");
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

    public static ProgressDialog showProgressDialog(Activity act, String msg) {
        ProgressDialog pDialog = new ProgressDialog(act);
        pDialog.setMessage(msg);
        pDialog.setCancelable(false);
        pDialog.setCanceledOnTouchOutside(false);
        return pDialog;
    }


    public static String getCurrentFragment(FragmentManager fM) {
        String fragmentTag = fM.getBackStackEntryAt(fM.getBackStackEntryCount() - 1).getName();
        return fragmentTag;
    }


    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }

    public static void openNetworkSetting(Context act) {
//        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
//        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        Intent intent = new Intent(Settings.ACTION_SETTINGS);
//        intent.setClassName("com.android.phone", "com.android.phone.NetworkSetting");
//        ComponentName cName = new ComponentName("com.android.phone", "com.android.phone.Settings");
//        intent.setComponent(cName);
        act.startActivity(intent);
    }

    public static String getVersionInfo(Context context) {
        String versionName = "";
        int versionCode = -1;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

//        return String.format("Version %s \nVersion code = %d", versionName, versionCode);
        return String.format("Version %s", versionName);
    }


    private void prepareShareIntent(Context context, Bitmap bmp) {
        Uri bmpUri = getLocalBitmapUri(context, bmp); // see previous remote images section
        // Construct share intent as described above based on bitmap

        Intent shareIntent = new Intent();
//        shareIntent = new Intent();
//        shareIntent.setPackage("com.whatsapp");
        shareIntent.setAction(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_TEXT, "share text");
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        context.startActivity(Intent.createChooser(shareIntent, "Share Opportunity"));

    }

    private Uri getLocalBitmapUri(Context context, Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

}

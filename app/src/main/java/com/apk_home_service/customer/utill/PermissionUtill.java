package com.apk_home_service.customer.utill;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.custom.MessageDialog;

import java.util.ArrayList;

/**
 * Created by ss126 on 23/8/16.
 */
public class PermissionUtill {

    public static String requestLocationPermission[] = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    public static String requestSMSPermission[] = {Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};
    public static String callPhonePermission[] = {Manifest.permission.CALL_PHONE};
    public static String requestSDCardPermission[] = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public static String requestCamera[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public static boolean checkAndRequestGPSPermissions(Activity activity, String[] permissions, int requestCode) {

        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps(activity);
            return false;
        } else {
            return requestPermission(activity, permissions, requestCode);
        }

    }


    public static boolean requestPermission(Activity activity, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> listPermissionsNeeded = new ArrayList<>();

            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions[i]);
                } else {
                }
            }

            if (!listPermissionsNeeded.isEmpty()) {
                ActivityCompat.requestPermissions(activity, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), requestCode);
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }


    private static void buildAlertMessageNoGps(final Activity activity) {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//                    }
//                })
//                .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
//                        dialog.cancel();
//                    }
//                });
//        final AlertDialog alert = builder.create();
//        alert.show();


        new MessageDialog(activity, new MessageDialog.ConfirmationDialogListener() {
            @Override
            public void onYesButtonClicked(MessageDialog.confirmTask task) {
                activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }

            @Override
            public void onNoButtonClicked(MessageDialog.confirmTask task) {
                activity.finish();
            }
        }, MessageDialog.confirmTask.NONE,
                "Your GPS seems to be disabled, do you want to enable it?", activity.getResources().getString(R.string.app_name), "No", "Yes", true, true);


    }


    public static boolean hasPermissions(Activity context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                if (ActivityCompat.checkSelfPermission(context, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    public static boolean hasPermission(Activity context, String permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }


    public static boolean hasRationalPermissions(Activity context, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, permissions[i])) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void openDialog(final Activity activity) {
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
//        alertDialogBuilder.setMessage("Allow required permission from settings.");
//
//        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", activity.getPackageName(), null));
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                activity.startActivity(intent);
//            }
//        });
//
//        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                activity.finish();
//            }
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.setCancelable(false);
//        alertDialog.show();

        new MessageDialog(activity, new MessageDialog.ConfirmationDialogListener() {
            @Override
            public void onYesButtonClicked(MessageDialog.confirmTask task) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", activity.getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activity.startActivity(intent);
            }

            @Override
            public void onNoButtonClicked(MessageDialog.confirmTask task) {
                activity.finish();
            }
        }, MessageDialog.confirmTask.NONE,
                "Allow required permission from settings.", activity.getResources().getString(R.string.app_name), "No", "Yes", true, true);



    }


}

package com.apk_home_service.customer.UI.custom;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;


import com.apk_home_service.customer.BuildConfig;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.utill.CommonUtill;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Rajnish kumar .
 * This is an Activity class.
 * Work of this class is to get image from gallery and camera.
 */

public class ChooseImageActivity extends Activity {

    final int FROM_GALLERY = 101;
    final int FROM_CAMERA = 102;
    final int FROM_CROPPER = 104;
    final int WIDTH = 600;
    final int HEIGHT = 800;
    final int REQUEST_PERMISSIONS = 103;
    String filePath = "";
    String fileTitle = "";
    String requestPermission[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String appId = BuildConfig.APPLICATION_ID;
    boolean isCoping = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_image);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fileTitle = getIntent().getStringExtra("file_name");
        isCoping = getIntent().getBooleanExtra("crop_option", false);
        if (fileTitle == null || fileTitle.trim().length() <= 0) {
            fileTitle = getIntent().getStringExtra("appName");
        }
        initViews();
    }

    /*
    * Method to initialize views
    * */
    private void initViews() {

        findViewById(R.id.btnFromGallery).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(ChooseImageActivity.this, requestPermission, REQUEST_PERMISSIONS)) {
                    imageFromGallery();
                }
            }
        });
        findViewById(R.id.btnFromCamera).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAndRequestPermissions(ChooseImageActivity.this, requestPermission, REQUEST_PERMISSIONS)) {
                    openCamera();
                }
            }
        });
//		findViewById(R.id.back_id);
        findViewById(R.id.cancel).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findViewById(R.id.parent).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    /*
    * Method to check requested permissions.
    * */
    public static boolean checkAndRequestPermissions(Activity activity, String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> listPermissionsNeeded = new ArrayList<>();

            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    listPermissionsNeeded.add(permissions[i]);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (!hasPermissions(this, permissions)) {
                    if (hasRationalPermissions(this, permissions)) {
                        openDialog();
                    } else {
                        Toast.makeText(this, "Please go to setting and enable permissions.", Toast.LENGTH_LONG).show();
//                        checkAndRequestPermissions(this, permissions, REQUEST_PERMISSIONS);
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void openDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Allow required permission from setting.");

        alertDialogBuilder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", getPackageName(), null));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    protected void openCamera() {

        File f = null;
        Uri photoURI = null;
        try {
            f = createImageFile();

            photoURI = FileProvider.getUriForFile(this,
                    appId + ".provider",
                    f);

        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent cameraIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(cameraIntent, FROM_CAMERA);
    }

    public File createImageFile() throws IOException {
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
        File getRoot = Environment.getExternalStorageDirectory();
        File myDir = new File(getRoot + File.separator + getIntent().getStringExtra("appName") + File.separator);
        myDir.mkdirs();
        String imageFileName = fileTitle + "_" + new Date().getTime() + ".jpeg";
//        String imageFileName = new Date().getTime() + ".jpg";
        File image = new File(myDir.getAbsolutePath(), imageFileName);
//        File image = File.createTempFile(imageFileName, ".jpg", myDir);
        filePath = image.getAbsolutePath();
        return image;
    }


//    private void imageFromCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, getImageUri());
//        startActivityForResult(intent, FROM_CAMERA);
//    }

//    private Uri getImageUri() {
//        // Store image in dcim
//        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "camera.jpg");
//
//        file = RotateImage(file);
//
//        Uri imgUri = Uri.fromFile(file);
////        filePath = file.getAbsolutePath();
//        return imgUri;
//    }


//    content://com.estrongs.files/storage/emulated/0/CSHire/report_change_particulars_appointment##149##.pdf

    private File RotateImage(String filestr) {
        File file = new File(filestr);
        try {
//            File file = new File(filePath);
//                filePath = data.getStringExtra("PhotoPath");
            if (file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
//                Bitmap bitmaps = BitmapFactory.decodeStream(new FileInputStream(file), null, options);

//                Bitmap bitmaps = BitmapFactory.decodeFile(filestr);

//                int scale = calculateInSampleSize(options, bitmaps.getWidth(), bitmaps.getHeight());
                int scale = calculateInSampleSize(options,WIDTH , HEIGHT);

                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = scale;

                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, option);
                bitmap = setRotation(bitmap, file.getAbsolutePath());
//                            ((RoundedImageView) findViewById(R.id.profile_pic)).setImageBitmap(bitmap);

                file = SaveImage(bitmap);
                CommonUtill.print("RotateImage ==" + filePath);
                if (isCoping) {
//                    Intent intent = new Intent(ChooseImageActivity.this, CropperActivity.class);
//                    intent.putExtra("image_path", file.getAbsolutePath());
//                    startActivityForResult(intent, FROM_CROPPER);
                } else {
                    Intent intent = new Intent();
                    CommonUtill.print("image_path ==" + filePath);
                    intent.putExtra("image_path", filePath);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Bitmap getThumbImage(String filePath, int reqWidth, int reqHeight) {
        try {
            File file = new File(filePath);
//                filePath = data.getStringExtra("PhotoPath");
            if (file.exists()) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(new FileInputStream(file), null, options);
                int scale = calculateInSampleSize(options, reqWidth, reqHeight);

                BitmapFactory.Options option = new BitmapFactory.Options();
                option.inSampleSize = scale;

                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file), null, option);
//                bitmap = setRotation(bitmap, filePath);
//                            ((RoundedImageView) findViewById(R.id.profile_pic)).setImageBitmap(bitmap);
                if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0)
                    return bitmap;
                else
                    return null;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private File SaveImage(Bitmap finalBitmap) {

//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/saved_images");
//        myDir.mkdirs();
//        File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "camera.jpg");
        File file = new File(filePath);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Intent mediaScanIntent = new Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            mediaScanIntent.setData(contentUri);
            sendBroadcast(mediaScanIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static Bitmap setRotation(Bitmap bmp, String imagePath) {

        ExifInterface exifcam = null;
        Matrix matrix = new Matrix();
        try {
            exifcam = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifcam
                .getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.postRotate(270);
                Log.e("rotation", "270");
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.postRotate(180);
                Log.e("rotation", "180");
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.postRotate(90);
                Log.e("rotation", "90");
                break;

        }

        bmp = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                matrix, false);
        return bmp;
    }

    private void imageFromGallery() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, FROM_GALLERY);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CommonUtill.print("data ==" + (data != null));
        if (data != null) {

            switch (requestCode) {
                case FROM_GALLERY:
                    if (resultCode == RESULT_OK) {
                        Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(
                                selectedImage, filePathColumn, null, null, null);


                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        filePath = cursor.getString(columnIndex);
                        CommonUtill.print("FROM_GALLERY ==" + filePath);
                        cursor.close();


                        if (filePath != null && filePath.length() > 0) {
                            CommonUtill.print("FROM_GALLERY 0==" + filePath);
                            filePath = RotateImage(filePath).getAbsolutePath();
                        }

                    }
                    break;
                case FROM_CAMERA:

                    if (resultCode == RESULT_OK) {
////                    Uri imagePath = getImageUri();
////                    filePath = imagePath.getPath();
////                    doSomething();
//                    filePath = RotateImage(filePath).getAbsolutePath();

                        CommonUtill.print("image choose result == FROM_CAMERA ==" + filePath);

                        if (filePath != null && filePath.length() > 0) {
                            filePath = RotateImage(filePath).getAbsolutePath();
                        }

                    }


                    break;

                case FROM_CROPPER:
                    String Path = data.getStringExtra("image_path");
                    Intent intent = new Intent();
                    intent.putExtra("image_path", Path);
                    setResult(RESULT_OK, intent);
                    finish();

                    break;

                default:
                    break;
            }

        } else {

            if (requestCode == FROM_CAMERA) {
                CommonUtill.print("FROM_CAMERA ==" + filePath);
                if (filePath != null && filePath.length() > 0) {
                    filePath = RotateImage(filePath).getAbsolutePath();
                }
            } else {
                finish();
            }


        }
    }


}
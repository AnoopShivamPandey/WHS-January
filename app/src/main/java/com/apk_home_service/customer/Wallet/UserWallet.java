package com.apk_home_service.customer.Wallet;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.apk_home_service.TransactionHistory;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.data.Constants;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

import static android.graphics.Color.BLACK;
import static android.graphics.Color.WHITE;
import static com.apk_home_service.customer.Wallet.API_Config.getApiClient_ByPost;

public class UserWallet extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    TextView totalPoints, totalBalance;
    ImageView menuImg;
    EditText couponText;
    TextView confirmTxt;
    UserData userData;
    CardView sendMoneyCard;
    String walletBalance = "0";
    TextView myCode, walletText;
    String requestPermission[] = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    final int REQUEST_PERMISSIONS = 1013;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_wallet);
        mActivity = this;
        sendMoneyCard = findViewById(R.id.sendMoneyCard);
        try {
            userData = (UserData) PrefrenceHandler.getPreferences(this).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        initialize();
        setQrImage();
        setData();
        findViewById(R.id.shareCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAndRequestPermissions(mActivity, requestPermission, REQUEST_PERMISSIONS)) {
                    share();
                }
            }
        });

        findViewById(R.id.myPassbookImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, TransactionHistory.class));

            }
        });
        myCode.setText(userData.getqrcode());
    }


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
                        share();
                    } else {
                        Toast.makeText(this, "Please go to setting and enable permissions.", Toast.LENGTH_LONG).show();
//                        checkAndRequestPermissions(this, permissions, REQUEST_PERMISSIONS);
                    }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void setData() {
        try {
            JSONObject mJsonObject = new JSONObject(Constants.getGeneralSettings(mActivity));
            walletText.setText(mJsonObject.getJSONObject("data").getJSONObject("my_wallet").getString("wallet_text"));
            walletBalance = mJsonObject.getJSONObject("data").getString("wallet_total");
            totalBalance.setText(getResources().getString(R.string.rupee) +
                    walletBalance);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void share() {
        try {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/png");

            String bitmapPath = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Home Service", null);
            Uri bitmapUri = Uri.parse(bitmapPath);
            intent.putExtra(Intent.EXTRA_TEXT, "Use the code for transactions using HomeService.");
            intent.putExtra(Intent.EXTRA_STREAM, bitmapUri);
            startActivity(Intent.createChooser(intent, "Share"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGeneralSettings();
    }

    public void getGeneralSettings() {
        ApiService client = getApiClient_ByPost();
        String otp = "";
        String userId = "";
        try {
            UserData userData;
            userData = (UserData) PrefrenceHandler.getPreferences(mActivity).getObjectValue(new UserData());
            userId = userData.getUserId();
            otp = GlobalAppApis.getUserID(userId);
        } catch (Exception e) {
            e.printStackTrace();
            otp = "";
        }

        Call<String> call = client.getGeneralSettings(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
//                    Constants.savePreferences(mActivity,"qrcode",jsonObject.getJSONObject("data").getString("qrcode"));
//                    Constants.savePreferences(mActivity,"qimage",jsonObject.getJSONObject("data").getString("qrcode_image"));
                    Constants.storeGeneralSettings(mActivity, jsonObject);
                    setData();
                } else {
                    Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }

            }
        }, mActivity, call, "", false);


    }

    Bitmap bitmap;

    void setQrImage() {
        ImageView qrImage = findViewById(R.id.qrImage);

        try {
            bitmap = encodeAsBitmap(userData.getqrcode());
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
            Bitmap bitmap = null;
            try {
                bitmap = encodeAsBitmap("My Treat");
            } catch (WriterException e1) {
                e1.printStackTrace();
            }
            qrImage.setImageBitmap(bitmap);
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            result = new MultiFormatWriter().encode(str,
                    BarcodeFormat.QR_CODE, 300, 300, null);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, 300, 0, 0, w, h);
        return bitmap;
    }

    void initialize() {
        totalPoints = findViewById(R.id.totalPoints);
        walletText = findViewById(R.id.walletText);
        totalBalance = findViewById(R.id.totalBalance);
        menuImg = findViewById(R.id.menuImg);
        couponText = findViewById(R.id.couponText);
        confirmTxt = findViewById(R.id.confirmTxt);
        myCode = findViewById(R.id.myCode);
        menuImg.setOnClickListener(this);
        confirmTxt.setOnClickListener(this);
        sendMoneyCard.setOnClickListener(this);
    }


    void setData(JSONObject data) {
        try {
            totalPoints.setText((data.has("total_points") && data.getString("total_points").length() > 0)
                    ? data.getString("total_points") : "0");

            totalBalance.setText(getResources().getString(R.string.Rs) + ((data.has("total_balance") && data.getString("total_balance").length() > 0)
                    ? data.getString("total_balance") : "0"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    boolean isValid() {
        if (couponText.getText().toString().trim().length() == 0) {
            Toast.makeText(mActivity, "Please enter coupon code.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    public void updateWallet() {
        String otp = new GlobalAppApis().walletDeatils(couponText.getText().toString().trim(), userData.getUserId());
        ApiService client = getApiClient_ByPost();
        Call<String> call = client.updateWallet(PrefrenceHandler.getPreferences(this).getAPI_TOKEN(), otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
                    Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    if (jsonObject.has("data") && jsonObject.getJSONObject("data").length() > 0) {
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    } else {

                    }
                } else {
                    try {
                        Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }, mActivity, call, "", true);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menuImg:
                onBackPressed();
                break;
            case R.id.confirmTxt:
                if (isValid()) {
                    updateWallet();
                }
                break;
            case R.id.sendMoneyCard:
                startActivityForResult(new Intent(UserWallet.this, MoneyTransferActivity.class)
                                .putExtra("walletBalance", walletBalance),
                        123);
                break;
        }
    }
}

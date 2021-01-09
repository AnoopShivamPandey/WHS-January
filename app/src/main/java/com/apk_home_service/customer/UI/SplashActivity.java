package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.data.Constants;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.utill.PermissionUtill;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

import static com.apk_home_service.customer.Wallet.API_Config.getApiClient_ByPost;

public class SplashActivity extends AppCompatActivity {

    VideoView videoPlayer;
    final int REQUEST_PERMISSIONS = 100;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mActivity = this;
        ImageView splashImage = findViewById(R.id.splashImage);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getGeneralSettings();
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtill.checkAndRequestGPSPermissions(mActivity, PermissionUtill.requestLocationPermission, REQUEST_PERMISSIONS)) {
                startNextScreen();
            }
        } else {
            startNextScreen();

        }
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
                } else {
                    Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                }

            }
        }, mActivity, call, "", false);


    }



    private void startNextScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PrefrenceHandler.getPreferences(mActivity).isLogin()) {
                    Intent intent = new Intent(mActivity, BottomNavigationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
//            Intent intent = new Intent(mActivity, MobileActivity.class);
                    Intent intent = new Intent(mActivity, BottomNavigationActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (!PermissionUtill.hasPermissions(mActivity, permissions)) {
                    if (PermissionUtill.hasRationalPermissions(mActivity, permissions)) {
                        PermissionUtill.openDialog(mActivity);
                    } else {
                        Toast.makeText(mActivity, "Please go to setting and enable permissions.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    startNextScreen();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}

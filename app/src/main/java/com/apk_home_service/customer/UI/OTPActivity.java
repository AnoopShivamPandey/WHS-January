package com.apk_home_service.customer.UI;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.controll.IncomingSms1;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.RequestModal;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.network.FPModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.google.gson.Gson;
import com.apk_home_service.customer.utill.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OTPActivity extends AppCompatActivity {

    EditText otpEdt;
    ProgressDialog pDialog;
    LocalBroadcastManager localBroadcastManager;


    public BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            CommonUtill.print("smsReceiver ==");
            CommonUtill.print("smsReceiver sender ==" + intent.getStringExtra("sender"));
            CommonUtill.print("smsReceiver msg ==" + intent.getStringExtra("msg"));
            otpEdt.setText(intent.getStringExtra("msg"));
            requestVerifyOTPAPI();
        }
    };
     SharedPreferences city_preference;
     SharedPreferences.Editor city_editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        pDialog = CommonUtill.showProgressDialog(this, "Please wait...");
        otpEdt = (EditText) findViewById(R.id.otpEdt);

        findViewById(R.id.verifyOTP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid()) {
                    requestVerifyOTPAPI();
                }
            }
        });

        findViewById(R.id.resendOTP).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTPAPI();
            }
        });
    }

    private void resendOTPAPI() {
        RequestModal requestModal = new RequestModal();
        requestModal.setPhoneNo(getIntent().getStringExtra("mobile"));
        CommonUtill.printClassData(requestModal);
        pDialog.show();
        new FPModel(OTPActivity.this, FPModel.CRAZYBILLA_API).fpApi.SendOTP(requestModal, new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                pDialog.dismiss();
                CommonUtill.print("resendOTPAPI result ==" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        CommonUtill.showTost(OTPActivity.this, jsonObject.getString("message"));
                    } else {
                        CommonUtill.showTost(OTPActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();
                CommonUtill.print("resendOTPAPI error ==" + error.getMessage());
            }
        });
    }

    private void requestVerifyOTPAPI() {
        RequestModal requestModal = new RequestModal();
        requestModal.setPhoneNo(getIntent().getStringExtra("mobile"));
        requestModal.setOTP(otpEdt.getText().toString().trim());
        CommonUtill.printClassData(requestModal);
        pDialog.show();
        new FPModel(OTPActivity.this, FPModel.CRAZYBILLA_API).fpApi.ValidateOTP(requestModal, new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                pDialog.dismiss();
                CommonUtill.print("requestVerifyOTPAPI result ==" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        PrefrenceHandler.getPreferences(OTPActivity.this).setAPI_TOKEN(jsonData.getString("api_token"));
                        Gson gson = new Gson();
                        String jsonInString = jsonObject.getString("data");
                        UserData user = gson.fromJson(jsonInString, UserData.class);
                        CommonUtill.printClassData(user);
                        PrefrenceHandler.getPreferences(OTPActivity.this).putObjectValue(user);
                        if (TextUtils.isEmpty(user.getUserName())) {
                            Intent i = new Intent(OTPActivity.this, SignupActivity.class);
                            CommonUtill.hideSoftKeyboard(OTPActivity.this);
                            if (getIntent().hasExtra("trolly")) {
                                i.putExtra("trolly", getIntent().getSerializableExtra("trolly"));
                                i.putExtra("resultJSON", getIntent().getStringExtra("resultJSON"));
                                i.putExtra("mapLat", getIntent().getDoubleExtra("mapLat", 0));
                                i.putExtra("mapLong", getIntent().getDoubleExtra("mapLong", 0));
                                i.putExtra("foodArrayList", getIntent().getSerializableExtra("foodArrayList"));
                            }
                            startActivity(i);
                            finish();
                        } else {
                            CommonUtill.hideSoftKeyboard(OTPActivity.this);
                            Constants.savePreferences(OTPActivity.this, Constants.USER_ID, "logged");
                            PrefrenceHandler.getPreferences(OTPActivity.this).setLogin();
                            if (getIntent().hasExtra("trolly")) {
                                Intent i = new Intent(OTPActivity.this, OrderPreviewActivity.class);
                                i.putExtra("trolly", getIntent().getSerializableExtra("trolly"));
                                i.putExtra("resultJSON", getIntent().getStringExtra("resultJSON"));
                                i.putExtra("mapLat", getIntent().getDoubleExtra("mapLat", 0));
                                i.putExtra("mapLong", getIntent().getDoubleExtra("mapLong", 0));
                                i.putExtra("foodArrayList", getIntent().getSerializableExtra("foodArrayList"));
                                startActivity(i); }
                            else { startActivity(new Intent(OTPActivity.this, BottomNavigationActivity.class));
                            }
                            finish();
                        }
                        //login perference
                        city_preference = getSharedPreferences("city_preference", MODE_PRIVATE);
                        city_editor = city_preference.edit();
                        city_editor.putString("city",jsonObject.getString("city"));
                        city_editor.putString("id",jsonObject.getString("id"));
                        city_editor.commit();
                        CommonUtill.showTost(OTPActivity.this, jsonObject.getString("message"));
                    } else {
                        CommonUtill.showTost(OTPActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();
                CommonUtill.print("requestVerifyOTPAPI error ==" + error.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(OTPActivity.this, MobileActivity.class);
        if (getIntent().hasExtra("trolly")) {
            i.putExtra("trolly", getIntent().getSerializableExtra("trolly"));
            i.putExtra("resultJSON", getIntent().getStringExtra("resultJSON"));
            i.putExtra("mapLat", getIntent().getDoubleExtra("mapLat", 0));
            i.putExtra("mapLong", getIntent().getDoubleExtra("mapLong", 0));
            i.putExtra("foodArrayList", getIntent().getSerializableExtra("foodArrayList"));
        }
        startActivity(i);
        finish();
    }

    private boolean isValid() {
        if (TextUtils.isEmpty(otpEdt.getText().toString().trim())) {
            CommonUtill.showTost(OTPActivity.this, "Please enter OTP.");
            return false;
        }
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        localBroadcastManager.unregisterReceiver(smsReceiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        localBroadcastManager.registerReceiver(smsReceiver, new IntentFilter(IncomingSms1.SMS_RECEIVED));
    }
}

package com.apk_home_service.customer.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.modal.RequestModal;
import com.apk_home_service.customer.network.FPModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.PermissionUtill;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MobileActivity extends AppCompatActivity {
    final int REQUEST_SMS = 100;
    EditText mobileEdt;
    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile);
        pDialog = CommonUtill.showProgressDialog(this, "Please wait...");
        mobileEdt = (EditText) findViewById(R.id.mobileEdt);

        findViewById(R.id.sendOtpImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    requestSendOTPAPI(); }
            }
        });
    }

    private void requestSendOTPAPI() {
        RequestModal requestModal = new RequestModal();
        requestModal.setPhoneNo(mobileEdt.getText().toString().trim());
        CommonUtill.printClassData(requestModal);
        pDialog.show();
        new FPModel(MobileActivity.this, FPModel.CRAZYBILLA_API).fpApi.SendOTP(requestModal, new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                pDialog.dismiss();
                CommonUtill.print("requestSendOTPAPI result ==" + result);
                Log.d("res","response"+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject jsonData = jsonObject.getJSONObject("data");
                        Intent i = new Intent(MobileActivity.this, OTPActivity.class).putExtra("mobile", mobileEdt.getText().toString().trim());
                        if(getIntent().hasExtra("trolly"))
                        {
                            i.putExtra("trolly", getIntent().getSerializableExtra("trolly"));
                            i.putExtra("resultJSON", getIntent().getStringExtra("resultJSON"));
                            i.putExtra("mapLat", getIntent().getDoubleExtra("mapLat", 0));
                            i.putExtra("mapLong", getIntent().getDoubleExtra("mapLong", 0));
                            i.putExtra("foodArrayList",  getIntent().getSerializableExtra("foodArrayList"));
                        }
//                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                        CommonUtill.showTost(MobileActivity.this, jsonObject.getString("message"));
                    } else {
                        CommonUtill.showTost(MobileActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();
                CommonUtill.print("requestSendOTPAPI error ==" + error.getMessage());
            }
        });
    }
    private boolean isValid() {
        if (TextUtils.isEmpty(mobileEdt.getText().toString().trim())) {
            CommonUtill.showTost(MobileActivity.this, "Please enter mobile no.");
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SMS: {
                if (!PermissionUtill.hasPermissions(this, permissions)) {
                    if (PermissionUtill.hasRationalPermissions(this, permissions)) {
                        PermissionUtill.openDialog(MobileActivity.this);
                    } else {
                        Toast.makeText(this, "Please go to setting and enable permissions.", Toast.LENGTH_LONG).show();
//                        checkAndRequestPermissions(this, permissions, REQUEST_PERMISSIONS);
                    }
                } else {
                    requestSendOTPAPI();
                }
            }
            break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

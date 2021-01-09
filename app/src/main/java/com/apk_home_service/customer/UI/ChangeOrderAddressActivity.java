package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.ValidationUtill;

public class ChangeOrderAddressActivity extends Activity {

    ProgressDialog pDialog;
    EditText nameEdt, mobileEdt, emailEdt, streetEdt, cityEdt, stateEdt, countryEdt, pinEdt;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_order_address);
        pDialog = CommonUtill.showProgressDialog(this, "Please wait...");

        try {
            userData = (UserData) PrefrenceHandler.getPreferences(ChangeOrderAddressActivity.this).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        nameEdt = (EditText) findViewById(R.id.nameEdt);
        mobileEdt = (EditText) findViewById(R.id.mobileEdt);
        streetEdt = (EditText) findViewById(R.id.streetEdt);
        emailEdt = (EditText) findViewById(R.id.emailEdt);
        cityEdt = (EditText) findViewById(R.id.cityEdt);
        stateEdt = (EditText) findViewById(R.id.stateEdt);
        countryEdt = (EditText) findViewById(R.id.countryEdt);
        pinEdt = (EditText) findViewById(R.id.pinEdt);
        cityEdt.setEnabled(false);
        stateEdt.setEnabled(false);
        countryEdt.setEnabled(false);
        pinEdt.setEnabled(false);

        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (isValid()) {
                    String address = nameEdt.getText().toString().trim().toUpperCase() + "\n"
                            + mobileEdt.getText().toString().trim() + "\n\n"
                            + streetEdt.getText().toString().trim() + ", "
                            + cityEdt.getText().toString().trim() + ", "
                            + stateEdt.getText().toString().trim() + ", "
                            + countryEdt.getText().toString().trim() + ", "
                            + pinEdt.getText().toString().trim();


                    Intent intent = new Intent();
                    intent.putExtra("address", address);
                    intent.putExtra("name", nameEdt.getText().toString());
                    intent.putExtra("mobile", mobileEdt.getText().toString());
                    intent.putExtra("email", emailEdt.getText().toString());
                    intent.putExtra("street", streetEdt.getText().toString());
                    intent.putExtra("city", cityEdt.getText().toString());
                    intent.putExtra("state", stateEdt.getText().toString());
                    intent.putExtra("country", countryEdt.getText().toString());
                    intent.putExtra("pin", pinEdt.getText().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }


            }
        });

        setProfileData();

    }

    private void setProfileData() {
        nameEdt.setText(userData.getUserName());
        mobileEdt.setText(userData.getPhoneNo());
        emailEdt.setText(userData.getEmailId());
        streetEdt.setText(userData.getStreet());
        cityEdt.setText(userData.getCityName());
        stateEdt.setText(userData.getStateName());
        countryEdt.setText(userData.getCountry());
        pinEdt.setText(userData.getPin_code());
    }


    private boolean isValid() {
        if (TextUtils.isEmpty(nameEdt.getText().toString().trim())) {
            CommonUtill.showTost(ChangeOrderAddressActivity.this, "Please enter name.");
            return false;
        } else if (TextUtils.isEmpty(mobileEdt.getText().toString().trim())) {
            CommonUtill.showTost(ChangeOrderAddressActivity.this, "Please enter mobile no.");
            return false;
        } else if (!ValidationUtill.isValidPhoneNumber(mobileEdt.getText().toString().trim())) {
            CommonUtill.showTost(ChangeOrderAddressActivity.this, "Please enter valid mobile no.");
            return false;
        } else if (!TextUtils.isEmpty(emailEdt.getText().toString().trim()) && !ValidationUtill.isValidEmail(emailEdt.getText().toString().trim())) {
            CommonUtill.showTost(ChangeOrderAddressActivity.this, "Please enter valid email id.");
            return false;
        } else if (TextUtils.isEmpty(streetEdt.getText().toString().trim())) {
            CommonUtill.showTost(ChangeOrderAddressActivity.this, "Please Enter Street.");
            return false;
        } else if (TextUtils.isEmpty(cityEdt.getText().toString().trim())) {
            CommonUtill.showTost(ChangeOrderAddressActivity.this, "Please Enter City.");
            return false;
        } else if (TextUtils.isEmpty(stateEdt.getText().toString().trim())) {
            CommonUtill.showTost(ChangeOrderAddressActivity.this, "Please Enter State.");
            return false;
        } else if (TextUtils.isEmpty(countryEdt.getText().toString().trim())) {
            CommonUtill.showTost(ChangeOrderAddressActivity.this, "Please Enter Country.");
            return false;
        } else if (TextUtils.isEmpty(pinEdt.getText().toString().trim())) {
            CommonUtill.showTost(ChangeOrderAddressActivity.this, "Please Enter Pin code.");
            return false;
        } else if (!ValidationUtill.isValidPostalCode(pinEdt.getText().toString().trim())) {
            CommonUtill.showTost(ChangeOrderAddressActivity.this, "Please enter valid Pin code.");
            return false;
        }
        return true;
    }

}

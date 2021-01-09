package com.apk_home_service.customer.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.custom.ChooseImageActivity;
import com.apk_home_service.customer.controll.CircleTransform;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.network.FPModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.Constants;
import com.apk_home_service.customer.utill.ValidationUtill;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class SignupActivity extends AppCompatActivity {

    final int CHOOSE_IMAGE = 100;
    String imgPath = "";
    UserData userData;
    EditText nameEdt, mobileEdt, emailEdt, streetEdt, countryEdt, pinEdt;
    ImageView userImg;
    TextView stateEdt, cityEdt;
    ProgressDialog pDialog;
    CheckBox termCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        pDialog = CommonUtill.showProgressDialog(this, "Please wait...");
        try {
            userData = (UserData) PrefrenceHandler.getPreferences(SignupActivity.this).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        nameEdt = (EditText) findViewById(R.id.nameEdt);
        mobileEdt = (EditText) findViewById(R.id.mobileEdt);
        emailEdt = (EditText) findViewById(R.id.emailEdt);
        streetEdt = (EditText) findViewById(R.id.streetEdt);
        cityEdt = findViewById(R.id.cityEdt);
        stateEdt = findViewById(R.id.stateEdt);
        countryEdt = (EditText) findViewById(R.id.countryEdt);
        pinEdt = (EditText) findViewById(R.id.pinEdt);

        userImg = (ImageView) findViewById(R.id.userImg);
        termCheck = (CheckBox) findViewById(R.id.termCheck);

        nameEdt.setText(userData.getUserName());
        mobileEdt.setText(userData.getPhoneNo());
        emailEdt.setText(userData.getEmailId());
//        add1Edt.setText(userData.getPaytmAddress());
//        add2Edt.setText(userData.getUserName());

        stateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(SignupActivity.this, CountryListing.class)
                        .putExtra("stateId", ""), 1234);
            }
        });
        cityEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (stateId.length() == 0) {
                    Toast.makeText(SignupActivity.this, "Please select the state first.", Toast.LENGTH_SHORT).show();
                } else {
                    startActivityForResult(new Intent(SignupActivity.this, CountryListing.class)
                                    .putExtra("stateId", stateId),
                            4321);

                }
            }
        });
        userImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, ChooseImageActivity.class);
                intent.putExtra("crop_option", false);
                intent.putExtra("file_name", "cragybilla");
                startActivityForResult(intent, CHOOSE_IMAGE);
            }
        });


        findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValid()) {
                    requestSignup();
                }


//                startActivity(new Intent(SignupActivity.this, HomeActivity.class));
            }
        });
    }


    private void requestSignup() {

        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("full_name", nameEdt.getText().toString().trim());
        datamap.put("email", emailEdt.getText().toString().trim());
        datamap.put("mobile", mobileEdt.getText().toString().trim());
        datamap.put("password", "");
        datamap.put("device_id", CommonUtill.getDeviceId(this));
        datamap.put("fcm_token", PrefrenceHandler.getPreferences(SignupActivity.this).getFCM_KEY());
        datamap.put("city", cityId);
        datamap.put("state", stateId);
//        datamap.put("country", countryEdt.getText().toString().trim());
        datamap.put("address", streetEdt.getText().toString().trim());
        datamap.put("pincode", pinEdt.getText().toString().trim());
        datamap.put("latitude", "0");
        datamap.put("longitude", "0");


        HashMap<String, TypedFile> fileMap = new HashMap<>();
        if (!TextUtils.isEmpty(imgPath)) {
            File file = new File(imgPath);
            TypedFile typeFile = new TypedFile("*/*", file);

            fileMap.put("profile_picture", typeFile);
        }
//
//
        CommonUtill.print("requestSignup files==" + fileMap.toString());
        CommonUtill.print("requestSignup datas==" + datamap.toString());


//        RequestModal requestModal = new RequestModal();
//        requestModal.setName(nameEdt.getText().toString().trim());
//        requestModal.setEmail(emailEdt.getText().toString().trim());
//        requestModal.setMobile(mobileEdt.getText().toString().trim());
//        requestModal.setPassword("");
//        requestModal.setDevice_id(CommonUtill.getDeviceId(this));
//        requestModal.setFirebase_token("");
//        requestModal.setCity(cityEdt.getText().toString().trim());
//        requestModal.setState(stateEdt.getText().toString().trim());
//        requestModal.setCountry(countryEdt.getText().toString().trim());
//        requestModal.setStreet(streetEdt.getText().toString().trim());
//        requestModal.setPin_code(pinEdt.getText().toString().trim());
//        requestModal.setLongitude("0");
//        requestModal.setLatitude("0");
//        CommonUtill.printClassData(requestModal);


        pDialog.show();
        new FPModel(SignupActivity.this, FPModel.CRAZYBILLA_API).fpApi.Signup(datamap, fileMap, new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                pDialog.dismiss();
                CommonUtill.print("requestSignup result ==" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {

                        Gson gson = new Gson();
                        String jsonInString = jsonObject.getString("data");

                        UserData user = gson.fromJson(jsonInString, UserData.class);
                        CommonUtill.printClassData(user);
                        Constants.savePreferences(SignupActivity.this, Constants.USER_ID, "logged");
                        PrefrenceHandler.getPreferences(SignupActivity.this).putObjectValue(user);
                        PrefrenceHandler.getPreferences(SignupActivity.this).setLogin();
                        if (getIntent().hasExtra("trolly")) {
                            Intent i = new Intent(SignupActivity.this, OrderPreviewActivity.class);
                            i.putExtra("trolly", getIntent().getSerializableExtra("trolly"));
                            i.putExtra("resultJSON", getIntent().getStringExtra("resultJSON"));
                            i.putExtra("mapLat", getIntent().getDoubleExtra("mapLat", 0));
                            i.putExtra("mapLong", getIntent().getDoubleExtra("mapLong", 0));
                            i.putExtra("foodArrayList", getIntent().getSerializableExtra("foodArrayList"));
                            startActivity(i);
                        } else
                            startActivity(new Intent(SignupActivity.this, BottomNavigationActivity.class));
                        CommonUtill.hideSoftKeyboard(SignupActivity.this);
                        finish();
                        CommonUtill.showTost(SignupActivity.this, jsonObject.getString("message"));

                    } else {
                        CommonUtill.showTost(SignupActivity.this, jsonObject.getString("message"));
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
                CommonUtill.print("requestSignup error ==" + error.getMessage());
            }
        });

    }

    private boolean isValid() {
        if (TextUtils.isEmpty(nameEdt.getText().toString().trim())) {
            CommonUtill.showTost(SignupActivity.this, "Please enter name.");
            return false;
        } else if (TextUtils.isEmpty(mobileEdt.getText().toString().trim())) {
            CommonUtill.showTost(SignupActivity.this, "Please enter mobile no.");
            return false;
        } else if (!ValidationUtill.isValidPhoneNumber(mobileEdt.getText().toString().trim())) {
            CommonUtill.showTost(SignupActivity.this, "Please enter valid mobile no.");
            return false;
        } else if (!TextUtils.isEmpty(emailEdt.getText().toString().trim()) && !ValidationUtill.isValidEmail(emailEdt.getText().toString().trim())) {
            CommonUtill.showTost(SignupActivity.this, "Please enter valid email id.");
            return false;
        } else if (!TextUtils.isEmpty(pinEdt.getText().toString().trim()) && !ValidationUtill.isValidPostalCode(pinEdt.getText().toString().trim())) {
            CommonUtill.showTost(SignupActivity.this, "Please enter valid Pin code.");
            return false;
        } else if (!termCheck.isChecked()) {
            CommonUtill.showTost(SignupActivity.this, "Please select terms and conditions.");
            return false;
        }
        return true;
    }

    String stateId = "";
    String cityId = "";

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null) {
            imgPath = data.getStringExtra("image_path");
            File file = new File(data.getStringExtra("image_path"));
            Uri imageUri = Uri.fromFile(file);
            Glide.with(this)
                    .load(new File(imageUri.getPath()))
                    .error(R.drawable.user_image)
                    .crossFade()
                    .centerCrop()
                    .transform(new CircleTransform(this))
                    .into(userImg);

        } else if (requestCode == 1234 && resultCode == RESULT_OK && data != null) {
            stateId = data.getStringExtra("id");
            stateEdt.setText(data.getStringExtra("name"));
            cityEdt.setText("");
            cityId = "";
        } else if (requestCode == 4321 && resultCode == RESULT_OK && data != null) {
            cityId = data.getStringExtra("id");
            cityEdt.setText(data.getStringExtra("name"));
        }

    }

}
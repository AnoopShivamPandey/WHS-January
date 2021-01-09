package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.custom.ChooseImageActivity;
import com.apk_home_service.customer.controll.CircleTransform;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.network.FPModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.ValidationUtill;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;

public class UserProfile extends AppCompatActivity {
    final int CHOOSE_IMAGE = 100;
    String imgPath = "";
    View mainView;
    UserData userData;
    EditText nameEdt, mobileEdt, emailEdt, streetEdt, countryEdt, pinEdt;
    ImageView userPic;
    ProgressDialog pDialog;
    TextView cityEdt, stateEdt;
    Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        mActivity = this;
        pDialog = CommonUtill.showProgressDialog(mActivity, "Please wait...");

        try {
            userData = (UserData) PrefrenceHandler.getPreferences(mActivity).getObjectValue(new UserData());
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

        userPic = (ImageView) findViewById(R.id.userPic);
        stateEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(mActivity, CountryListing.class)
                        .putExtra("stateId", ""), 1234);
            }
        });
        cityEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    startActivityForResult(new Intent(mActivity, CountryListing.class)
                                    .putExtra("stateId", stateEdt.getTag().toString()),
                            4321);

                }
            }
        });


        userPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ChooseImageActivity.class);
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
//                    userData.setEmailId(emailEdt.getText().toString());
//                    userData.setUserName(nameEdt.getText().toString());
//                    try {
//                        PrefrenceHandler.getPreferences(mActivity).putObjectValue(userData);
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    }
                }

            }
        });

        setProfileData();

    }


    private void requestSignup() {
        HashMap<String, String> datamap = new HashMap<>();
        datamap.put("user_id", userData.getUserId());
        datamap.put("full_name", nameEdt.getText().toString().trim());
        datamap.put("email", emailEdt.getText().toString().trim());
//        datamap.put("mobile", mobileEdt.getText().toString().trim());
        datamap.put("city", cityEdt.getTag().toString().trim());
        datamap.put("state", stateEdt.getTag().toString().trim());
        datamap.put("country", countryEdt.getText().toString().trim());
        datamap.put("address", streetEdt.getText().toString().trim());
        datamap.put("pincode", pinEdt.getText().toString().trim());
        datamap.put("latitude", "0");
        datamap.put("longitude", "0");
        datamap.put("device_id", CommonUtill.getDeviceId(mActivity));


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


        pDialog.show();
        new FPModel(mActivity, FPModel.CRAZYBILLA_API).fpApi.updateProfile
                (datamap, fileMap, new Callback<String>() {
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

                                PrefrenceHandler.getPreferences(mActivity).putObjectValue(user);


                                CommonUtill.showTost(mActivity, jsonObject.getString("message"));
finish();
                            } else {
                                if (jsonObject.getString("status_code").equalsIgnoreCase("401")) {
                                    PrefrenceHandler.getPreferences(mActivity).setLogout();
                                    startActivity(new Intent(mActivity, MobileActivity.class));
                                    finish();
                                }
                                CommonUtill.showTost(mActivity, jsonObject.getString("message"));
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
            CommonUtill.showTost(mActivity, "Please enter name.");
            return false;
        } else if (TextUtils.isEmpty(mobileEdt.getText().toString().trim())) {
            CommonUtill.showTost(mActivity, "Please enter mobile.");
            return false;
        } else if (!ValidationUtill.isValidPhoneNumber(mobileEdt.getText().toString().trim())) {
            CommonUtill.showTost(mActivity, "Please enter valid mobile no.");
            return false;
        } else if (TextUtils.isEmpty(emailEdt.getText().toString().trim())) {
            CommonUtill.showTost(mActivity, "Please enter email.");
            return false;
        } else if (!ValidationUtill.isValidEmail(emailEdt.getText().toString().trim())) {
            CommonUtill.showTost(mActivity, "Please enter valid email id.");
            return false;
        } else if (!TextUtils.isEmpty(pinEdt.getText().toString().trim()) && !ValidationUtill.isValidPostalCode(pinEdt.getText().toString().trim())) {
            CommonUtill.showTost(mActivity, "Please enter valid Pin code.");
            return false;
        }
        return true;
    }

    private void setProfileData() {
        nameEdt.setText(userData.getUserName());
        mobileEdt.setText(userData.getPhoneNo());
        emailEdt.setText(userData.getEmailId());
        streetEdt.setText(userData.getStreet());
        cityEdt.setText(userData.getCityName());
        stateEdt.setText(userData.getStateName());
        stateEdt.setTag(userData.getState());
        cityEdt.setTag(userData.getCity());
        countryEdt.setText(userData.getCountry());
        pinEdt.setText(userData.getPin_code());

        Glide.with(this)
                .load(userData.getUserPic())
                .error(R.drawable.user_image)
                .crossFade()
                .placeholder(R.drawable.user_image)
                .centerCrop()
                .transform(new CircleTransform(mActivity))
                .into(userPic);
    }


    @Override
    public void onResume() {
        super.onResume();
    }

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
                    .transform(new CircleTransform(mActivity))
                    .into(userPic);

        } else if (requestCode == 1234 && resultCode == Activity.RESULT_OK && data != null) {
            stateEdt.setText(data.getStringExtra("name"));
            stateEdt.setTag(data.getStringExtra("id"));
            cityEdt.setText("");
            cityEdt.setTag("");
        } else if (requestCode == 4321 && resultCode == Activity.RESULT_OK && data != null) {
            cityEdt.setText(data.getStringExtra("name"));
            cityEdt.setTag(data.getStringExtra("id"));
        }

    }
}

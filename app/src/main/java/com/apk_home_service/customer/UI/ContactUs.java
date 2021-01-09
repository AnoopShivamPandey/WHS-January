package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.Wallet.API_Config;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.utill.CommonUtill;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;

public class ContactUs extends AppCompatActivity implements View.OnClickListener {
    TextView mailTv;
    TextView contactTv;
    TextView webTv;
    ImageView instaIcon, googleIcon, fbicon;
    EditText emailEt, userMessage;
    Activity mActivity;
    String facebook_url = "";
    String instagram_url = "";
    TextView submitBtn;
    ImageView back_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        mActivity = this;
        initialize();
        getGeneralSettings();

        back_img=findViewById(R.id.back_img);
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    boolean isValid() {
        try {
            if (emailEt.getText().toString().trim().length() == 0) {
                Toast.makeText(mActivity, "Please enter contact number.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (emailEt.getText().toString().trim().length() != 10) {
                Toast.makeText(mActivity, "Please enter valid contact number.", Toast.LENGTH_SHORT).show();
                return false;
            } else if (userMessage.getText().toString().trim().length() == 0) {
                Toast.makeText(mActivity, "Please enter your message.", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public void postContactMsg() {
        String otp = new GlobalAppApis().getContactUsDetails(emailEt.getText().toString(), userMessage.getText().toString());
        ApiService client = API_Config.getApiClient_ByPost();
        Call<String> call = client.getContactUsDetails(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
                    if (jsonObject.has("data") && jsonObject.getJSONObject("data").length() > 0) {
                        Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        CommonUtill.hideSoftKeyboard(mActivity);
//                        setData(jsonObject.getJSONObject("data"));
                        finish();
                    } else {

                        Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                }
            }

        }, mActivity, call, "", true);
    }


    void initialize() {
        mailTv = findViewById(R.id.mailTv);
        contactTv = findViewById(R.id.contactTv);
        webTv = findViewById(R.id.webTv);
        instaIcon = findViewById(R.id.instaIcon);
        googleIcon = findViewById(R.id.googleIcon);
        fbicon = findViewById(R.id.fbicon);
        userMessage = findViewById(R.id.userMessage);
        submitBtn = findViewById(R.id.submitBtn);
        emailEt = findViewById(R.id.emailEt);

        mailTv.setOnClickListener(this);
        contactTv.setOnClickListener(this);
        webTv.setOnClickListener(this);
        fbicon.setOnClickListener(this);
        instaIcon.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
    }

    void getGeneralSettings() {
        try {
            JSONObject mObject = new JSONObject(com.apk_home_service.customer.data.Constants.getGeneralSettings(this));
            if (mObject.getJSONObject("data").has("social_media")) {
                JSONObject socialJSON = mObject.getJSONObject("data").getJSONObject("social_media");
                mailTv.setText(socialJSON.getString("customercare_email"));
                contactTv.setText(socialJSON.getString("customer_phone"));
                webTv.setText(socialJSON.getString("website_url"));
                facebook_url = socialJSON.getString("facebook");
                instagram_url = socialJSON.getString("instagram");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openBrowser() {
        try {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webTv.getText().toString()));
            startActivity(browserIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openDialer() {
        try {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + contactTv.getText().toString()));
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void openEmail() {
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", mailTv.getText().toString(), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.webTv:
                openBrowser();
                break;
            case R.id.contactTv:
                openDialer();
                break;
            case R.id.mailTv:
                openEmail();
                break;
            case R.id.fbicon:
                openInFb();
                break;
            case R.id.instaIcon:
                openInInsta();
                break;
            case R.id.submitBtn:
                if (isValid()) {
                    postContactMsg();
                }
                break;
        }
    }

    private void openInFb() {
        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://profile/426253597411506"));
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook_url));
            startActivity(intent);
        } catch (Exception e) {


//            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/appetizerandroid")));
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebook_url)));
        }

    }

    private void openInInsta() {
        Uri uri = Uri.parse(instagram_url);
        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

        likeIng.setPackage("com.instagram.android");

        try {
            startActivity(likeIng);
        } catch (Exception e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse(instagram_url)));
        }

    }

}

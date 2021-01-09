package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.apk_home_service.customer.R;

import org.json.JSONObject;

public class ReachUs extends AppCompatActivity implements View.OnClickListener {
    TextView mailTv;
    TextView contactTv;
    TextView webTv;
    ImageView instaIcon, googleIcon, fbicon;
    Activity mActivity;
    String facebook_url = "";
    String instagram_url = "";
    ImageView menuImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reach_us);
        menuImg=findViewById(R.id.menuImg);
        mActivity = this;
        initialize();
        getGeneralSettings();
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    void initialize() {
        mailTv = findViewById(R.id.mailTv);
        contactTv = findViewById(R.id.contactTv);
        webTv = findViewById(R.id.webTv);
        instaIcon = findViewById(R.id.instaIcon);
        googleIcon = findViewById(R.id.googleIcon);
        fbicon = findViewById(R.id.fbicon);
        mailTv.setOnClickListener(this);
        contactTv.setOnClickListener(this);
        webTv.setOnClickListener(this);
        fbicon.setOnClickListener(this);
        instaIcon.setOnClickListener(this);
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
        }
    }

    private void openInFb() {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebook_url));
            startActivity(intent);
        } catch (Exception e) {
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

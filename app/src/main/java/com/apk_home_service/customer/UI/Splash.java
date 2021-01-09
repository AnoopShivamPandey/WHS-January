package com.apk_home_service.customer.UI;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.data.PrefrenceHandler;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView splashImage = findViewById(R.id.splashImage);
//        Glide.with(this).load(R.mipmap.splash).into(splashImage);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startNextScreen();
            }
        },3000);
    }

    private void startNextScreen() {
        if (PrefrenceHandler.getPreferences(Splash.this).isLogin()) {
            Intent intent = new Intent(Splash.this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else {
//            Intent intent = new Intent(Splash.this, MobileActivity.class);
            Intent intent = new Intent(Splash.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

}

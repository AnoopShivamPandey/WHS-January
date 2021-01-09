package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.controll.CircleTransform;
import com.apk_home_service.customer.modal.TrollyModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.Constants;

import java.util.Locale;

public class TrollyActivity extends Activity {

    TrollyModel trolly;
    ImageView banerImg, trollyPic;
    TextView nameTxt, distanceTxt, orderTxt;
    RatingBar rateStar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trolly);
        trolly = (TrollyModel) getIntent().getSerializableExtra("trolly");
        CommonUtill.printClassData(trolly);
//        {"distance":"0","email":"stall1@gmail.com","id":"12","latitude":"26.8466937","longitude":"80.946166","rating":"","name":"Veg Momos Stall","profile_picture":"http://35.154.238.50/staging/crazybilla/public/assets/default/default_user.png","mobile":"8795768686"}

        orderTxt = (TextView) findViewById(R.id.orderTxt);
        distanceTxt = (TextView) findViewById(R.id.distanceTxt);
        nameTxt = (TextView) findViewById(R.id.nameTxt);
        banerImg = (ImageView) findViewById(R.id.banerImg);
        trollyPic = (ImageView) findViewById(R.id.trollyPic);
        rateStar = (RatingBar) findViewById(R.id.rateStar);

//        distanceTxt.setText(trolly.getDistance() + " Km away");


        try {
            if (!trolly.getDistance().trim().isEmpty())
                distanceTxt.setText(String.format(Locale.US, "%.2f", Float.parseFloat(trolly.getDistance().trim())) + " Km away");
            else
                distanceTxt.setText("0 Km away");
        } catch (Exception e) {
            e.printStackTrace();
            distanceTxt.setText("0 Km away");
        }
        nameTxt.setText(trolly.getFullName());

        Glide.with(this)
                .load("")
                .error(R.drawable.food)
                .crossFade()
                .centerCrop()
                .into(banerImg);


        Glide.with(this)
                .load(trolly.getRestaurantImage())
                .error(R.drawable.food)
                .crossFade()
                .centerCrop()
                .transform(new CircleTransform(this))
                .into(trollyPic);

        orderTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.isUserLoggedIn(TrollyActivity.this)) {
                    startActivity(new Intent(TrollyActivity.this, MobileActivity.class));
                } else {
                    Intent intent = new Intent(TrollyActivity.this, TrollyMenuListActivity.class);
                    intent.putExtra("trolly", trolly);
                    intent.putExtra("mapLat", getIntent().getDoubleExtra("mapLat", 0));
                    intent.putExtra("mapLong", getIntent().getDoubleExtra("mapLong", 0));
                    startActivity(intent);
                    finish();

                }
            }
        });

    }
}

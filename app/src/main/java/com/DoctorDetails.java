package com;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apk_home_service.Appoinment;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.Doctor_List;
import com.apk_home_service.customer.UI.RestaurantList;
import com.apk_home_service.customer.controll.CircleTransform;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.apk_home_service.customer.Wallet.Urls.BASE_URL;

public class DoctorDetails extends AppCompatActivity {
    private ImageView appotment_btn, vediocall;
    private ImageView menuImg;
    private TextView user_name_tv, department_tv, category_tv, speclist_tv, location_tv;
    Activity mActivity;
    UserData userData;
    Intent intents;
    String doctorId;
    String mobile;
    ImageView call_btn;
    SimpleRatingBar rating_bar;
    TextView time_tv;
    CircularImageView circular_userPic;
    TextView fee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);
        call_btn = (ImageView) findViewById(R.id.call_btn);
        appotment_btn = (ImageView) findViewById(R.id.appotment_btn);
        vediocall = (ImageView) findViewById(R.id.vediocall);
        menuImg = (ImageView) findViewById(R.id.menuImg);

        user_name_tv = findViewById(R.id.user_name_tv);
        department_tv = findViewById(R.id.department_tv);
        category_tv = findViewById(R.id.category_tv);
        speclist_tv = findViewById(R.id.speclist_tv);
        location_tv = findViewById(R.id.location_tv);
        rating_bar = (SimpleRatingBar) findViewById(R.id.rating);
        time_tv = (TextView) findViewById(R.id.time_tv);

        circular_userPic = findViewById(R.id.userPic);
        fee = findViewById(R.id.fee);


        intents = getIntent();
        doctorId = intents.getStringExtra("doctor_id");
        //   Toast.makeText(getApplicationContext(),""+doctorId,Toast.LENGTH_LONG).show();

        mActivity = this;
        try {
            userData = (UserData) PrefrenceHandler.getPreferences(mActivity).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        getDoctorDetails();

//        if (userData.getUserName().equalsIgnoreCase("")){
//            user_name_tv.setText("Dr. Anoop Pandey");
//        }
//        else {
//            user_name_tv.setText(userData.getUserName());
//        }
        // location_tv.setText("Location:- "+userData.getCity()+","+userData.getState()+","+userData.getCountry());


        call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+mobile));
                startActivity(intent);

            }
        });

        vediocall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Toast.makeText(getApplicationContext(),"Coming Soon!",Toast.LENGTH_LONG).show();
            }
        });

        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        appotment_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Appoinment.class);
                intent.putExtra("doctorid", doctorId);
                startActivity(intent);
            }
        });

    }

    public void getDoctorDetails() {
        final ProgressDialog progressDialog = new ProgressDialog(DoctorDetails.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"customer/doctor_details", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                 // Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        //   Log.d("res","res"+listInString);
                        if (jsonObject.has("data")) {
                            JSONArray my_noti_Array = jsonObject.getJSONArray("data");
                            for (int i = 0; i < my_noti_Array.length(); i++) {
                                JSONObject jsonObject11 = my_noti_Array.getJSONObject(i);
                                String id = jsonObject11.getString("id");
                                String full_name = jsonObject11.getString("full_name");
                                mobile = jsonObject11.getString("mobile");
                                String email = jsonObject11.getString("email");
                                String opening_time = jsonObject11.getString("opening_time");
                                String closing_time = jsonObject11.getString("closing_time");
                                String user_type = jsonObject11.getString("user_type");
                                String city_name = jsonObject11.getString("city_name");
                                String state_name = jsonObject11.getString("state_name");
                                String street = jsonObject11.getString("street");
                                String specialities = jsonObject11.getString("specialities");
                                String pin_code = jsonObject11.getString("pin_code");
                                String restaurant_image = jsonObject11.getString("restaurant_image");
                                String complete_address = jsonObject11.getString("complete_address");
                                String distance = jsonObject11.getString("distance");
                                String rating = jsonObject11.getString("rating");
                                String profile_status = jsonObject11.getString("profile_status");

                                String feesCharge = jsonObject11.getString("fees_charge");
                                fee.setText("Fee:- "+"\u20B9"+feesCharge);


                                Glide.with(DoctorDetails.this)
                                        .load(restaurant_image)
                                        .error(R.drawable.user_image)
                                        .crossFade()
                                        .placeholder(R.drawable.user_image)
                                        .centerCrop()
                                        .transform(new CircleTransform(mActivity))
                                        .into(circular_userPic);

                                time_tv.setText(opening_time+" to "+closing_time);
                                user_name_tv.setText(full_name);
                                department_tv.setText(specialities);
                                category_tv.setText("Caterory: "+user_type);
                                speclist_tv.setText("Specialist: "+specialities);
                                location_tv.setText("Location: "+complete_address);
                                rating_bar.setRating(Float.parseFloat(rating));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("id",doctorId);
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

}
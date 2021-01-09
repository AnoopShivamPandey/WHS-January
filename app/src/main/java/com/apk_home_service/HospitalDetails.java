package com.apk_home_service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.DoctorDetails;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.Doctor_List;
import com.apk_home_service.customer.UI.TrollyMenuListActivity;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;
import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import static com.apk_home_service.customer.Wallet.Urls.BASE_URL;

public class HospitalDetails extends AppCompatActivity {
    Activity mActivity;
    LinearLayout detailBg;
    ImageView noRecordsText;
    UserData userData;
    private String cat_id;
    private TextView headerText;
    private Intent it;
    private String from_where;

    CircularImageView restaurantImage;
    TextView restaurantName, restaurantType, restaurantTiming, restaurantDistance, restaurantRating, orderNowBtn;
    List<BannerData> bannerData = new ArrayList<>();
    String bannerUrl = "banner";
    ViewPager viewPager_banner;
    private static int currentPage = 0;
    CirclePageIndicator indicator;
    private ArrayList<Doctor_List> doctor_dataArray;
    private LinearLayout dynamic_ll;
    private LocationManager locationManager;
    String lati,longi;
    TextView des;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospital_details);
        mActivity = this;
        noRecordsText = findViewById(R.id.noRecordsText);
        viewPager_banner = (ViewPager) findViewById(R.id.viewPager_banner);
        indicator = (CirclePageIndicator) findViewById(R.id.indicator);
        dynamic_ll = (LinearLayout) findViewById(R.id.dynamic_ll);
        headerText = (TextView) findViewById(R.id.headerText);
        headerText.setText("Available Doctor's");
        it = getIntent();



        getBanner();
        findViewById(R.id.backImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getData();

    }


    public void getData() {
        doctor_dataArray = new ArrayList<Doctor_List>();
        final ProgressDialog progressDialog = new ProgressDialog(HospitalDetails.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + "customer/" + "hospitalDoctorList", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
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
                                String mobile = jsonObject11.getString("mobile");
                                String email = jsonObject11.getString("email");
                                String restaurant_owner = jsonObject11.getString("restaurant_owner");
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

                                Doctor_List doctorlist = new Doctor_List(id, full_name, mobile, email, restaurant_owner, opening_time,
                                        closing_time, user_type, city_name, state_name, street, specialities, pin_code, restaurant_image, complete_address, distance, rating, profile_status);
                                doctor_dataArray.add(doctorlist);
                                getDataList(i);
                            }
                        }
                    } else {
                        noRecordsText.setVisibility(View.VISIBLE);
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
                params.put("hospital_id", it.getStringExtra("id"));
                params.put("lat", String.valueOf(getIntent().getDoubleExtra("mapLat", 0)));
                params.put("lon", String.valueOf(getIntent().getDoubleExtra("mapLong", 0)));
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    //Latest_add_Screen function to set data in data array(dataarray1)layout
    private void getDataList(final int i) {
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.list_row, null);
        dynamic_ll.addView(convertView);
        restaurantName = convertView.findViewById(R.id.restaurantName);
        restaurantImage = convertView.findViewById(R.id.restaurantImage);
        restaurantType = convertView.findViewById(R.id.restaurantType);
        restaurantTiming = convertView.findViewById(R.id.restaurantTiming);
        restaurantDistance = convertView.findViewById(R.id.restaurantDistance);
        restaurantRating = convertView.findViewById(R.id.restaurantRating);
        orderNowBtn = convertView.findViewById(R.id.orderNowBtn);
        // des=convertView.findViewById(R.id.des);


        Glide.with(mActivity).load(doctor_dataArray.get(i).getRestaurant_image())
                .error(R.drawable.logo)
                .placeholder(R.drawable.logo).fitCenter()
                .into(restaurantImage);
        restaurantName.setText(doctor_dataArray.get(i).getFull_name());


        restaurantType.setText("Location:- " + doctor_dataArray.get(i).getComplete_address());
        restaurantTiming.setText("Timing:-" + doctor_dataArray.get(i).getOpening_time() + " - " + doctor_dataArray.get(i).getClosing_time());
        restaurantDistance.setText("Distance:- " + doctor_dataArray.get(i).getDistance());
        restaurantRating.setText(doctor_dataArray.get(i).getRating());
        if (!doctor_dataArray.get(i).getProfile_status().equalsIgnoreCase("online")) {
            orderNowBtn.setVisibility(View.GONE);
        } else {
            orderNowBtn.setVisibility(View.VISIBLE);

        }
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!doctor_dataArray.get(i).getProfile_status().equalsIgnoreCase("online")) {
                    Toast.makeText(mActivity, "Not Available!, please try again later", Toast.LENGTH_SHORT).show();
                } else if (doctor_dataArray.get(i).getUser_type().equalsIgnoreCase("hospital_doctor")) {
                    Intent intent = new Intent(getApplicationContext(), DoctorDetails.class);
                    intent.putExtra("doctor_id", doctor_dataArray.get(i).getId());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(mActivity, TrollyMenuListActivity.class);
                    intent.putExtra("rest_id", doctor_dataArray.get(i).getId());
                    intent.putExtra("rest_name", doctor_dataArray.get(i).getFull_name());
                    intent.putExtra("rest_img", doctor_dataArray.get(i).getRestaurant_image());
                    intent.putExtra("mapLat", getIntent().getDoubleExtra("mapLat", 0));
                    intent.putExtra("mapLong", getIntent().getDoubleExtra("mapLong", 0));
                    startActivity(intent);
                }
            }
        });
        View view = new View(HospitalDetails.this);
        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
        view.setLayoutParams(params_view);
        dynamic_ll.addView(view);
    }


    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(HospitalDetails.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL+"customer/banner-list", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("True")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String JsonInString = jsonObject1.getString("banner");
                        bannerData = BannerData.createJsonInList(JsonInString);
                    }
                    viewPager_banner.setAdapter(new AdapterForBanner(HospitalDetails.this, bannerData));
                    indicator.setViewPager(viewPager_banner);
                    final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
                    indicator.setRadius(2 * density);
                    // Auto start of viewpager
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == bannerData.size()) {
                                currentPage = 0;
                            }
                            viewPager_banner.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 5000, 3000);
// Pager listener over indicator
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }

                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) {
                        }

                        @Override
                        public void onPageScrollStateChanged(int pos) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(HospitalDetails.this, response, Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(HospitalDetails.this, "Please check your Internet Connection! try again...", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(HospitalDetails.this);
        requestQueue.add(stringRequest);
    }
}
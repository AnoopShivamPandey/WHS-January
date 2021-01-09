package com.apk_home_service;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.RestaurantList;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.apk_home_service.customer.Wallet.Urls.BASE_URL;

public class ViewAppoitmentList extends AppCompatActivity {

    private ImageView menuImg;
    private LinearLayout dynamic_ll;
    private ImageView norecord_img;
    UserData userData;
    private ArrayList<Appotment_Data_List> appoitment_dataArray;
    private Intent intents;
    String doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appoitment_list);
        menuImg = (ImageView) findViewById(R.id.menuImg);
        dynamic_ll = (LinearLayout) findViewById(R.id.dynamic_ll);
        norecord_img=(ImageView)findViewById(R.id.norecord_img);
        intents = getIntent();
        doctorId = intents.getStringExtra("doctorid");
       // Toast.makeText(getApplicationContext(),""+doctorId,Toast.LENGTH_LONG).show();

        try {
            userData = (UserData) PrefrenceHandler.getPreferences(getApplicationContext()).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        appoitment_dataArray = new ArrayList<Appotment_Data_List>();


        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getData();
    }

    public void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(ViewAppoitmentList.this);
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"customer/bookappointmentmylist", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
              //  Toast.makeText(getApplicationContext(),""+response,Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        if (jsonObject != null) {
                                JSONArray my_noti_Array = jsonObject.getJSONArray("data");
                                for (int i = 0; i < my_noti_Array.length(); i++) {
                                    JSONObject jsonObject11 = my_noti_Array.getJSONObject(i);
                                    String name = jsonObject11.getString("name");
                                    String mobile_no = jsonObject11.getString("mobile_no");
                                    String scheduled_time = jsonObject11.getString("scheduled_time");
                                    String scheduled_date = jsonObject11.getString("scheduled_date");
                                    String purpose = jsonObject11.getString("purpose");
                                    String status = jsonObject11.getString("status");
                                    Appotment_Data_List noti_screen_lists = new Appotment_Data_List(name, mobile_no, scheduled_time, scheduled_date, purpose,status);
                                    appoitment_dataArray.add(noti_screen_lists);
                                    getDataList(i);
                                }
                        } else {
                            norecord_img.setVisibility(View.VISIBLE);
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
                norecord_img.setVisibility(View.VISIBLE);
                 Toast.makeText(ViewAppoitmentList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("cust_id", userData.getUserId());
                if (doctorId != null){
                    params.put("doctor_id",doctorId);
                }
                    return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    //Latest_add_Screen function to set data in data array(dataarray1)layout
    private void getDataList(final int i) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dynamic_view_appoitment, null);
        dynamic_ll.addView(convertView);
        TextView paitentname_tv = (TextView) convertView.findViewById(R.id.paitentname_tv);
        TextView mobile_tv = (TextView) convertView.findViewById(R.id.mobile_tv);
        TextView date_tv = (TextView) convertView.findViewById(R.id.date_tv);
        TextView time_tv = (TextView) convertView.findViewById(R.id.time_tv);
        TextView purpose_tv = (TextView) convertView.findViewById(R.id.purpose_tv);
        TextView bookingStatus = (TextView) convertView.findViewById(R.id.bookingStatus);

        paitentname_tv.setText(appoitment_dataArray.get(i).getName());
        mobile_tv.setText("Mobile No:-"+appoitment_dataArray.get(i).getMobile_no());
        date_tv.setText("Date:-"+appoitment_dataArray.get(i).getScheduled_date());
        time_tv.setText("Time:-"+appoitment_dataArray.get(i).getScheduled_time());
        purpose_tv.setText("Purpose:- "+appoitment_dataArray.get(i).getPurpose());
        bookingStatus.setText(appoitment_dataArray.get(i).getStatus());

        View view = new View(getApplicationContext());
        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
        view.setLayoutParams(params_view);
        dynamic_ll.addView(view);
    }


}
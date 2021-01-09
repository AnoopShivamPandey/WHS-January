package com.apk_home_service;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;

import org.androidannotations.annotations.App;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.apk_home_service.customer.Wallet.Urls.BASE_URL;

public class Appoinment extends AppCompatActivity {

    private ImageView menuImg;
    private RelativeLayout submit;
    EditText nameEdt, mobileEdt, age_et, date_et, message_et;
    final Calendar myCalendar = Calendar.getInstance();
    Spinner sp_gender;
    String[] spinnerGender = new String[]{
            "Select Gender",
            "male",
            "female",
            "other",
    };
    UserData userData;
    private Intent intents;
    String doctorId;
    EditText time_et;

    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appoinment);
        menuImg = (ImageView) findViewById(R.id.menuImg);


        nameEdt = findViewById(R.id.nameEdt);
        mobileEdt = findViewById(R.id.mobileEdt);
        sp_gender = findViewById(R.id.sp_gender);
        age_et = findViewById(R.id.age_et);
        date_et = findViewById(R.id.date_et);
        message_et = findViewById(R.id.message_et);
        time_et=findViewById(R.id.time_et);

        submit = (RelativeLayout) findViewById(R.id.submit);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try {
            userData = (UserData) PrefrenceHandler.getPreferences(getApplicationContext()).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        intents = getIntent();
        doctorId = intents.getStringExtra("doctorid");
        //   Toast.makeText(getApplicationContext(),""+doctorId,Toast.LENGTH_LONG).show();



        time_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(Appoinment.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        time_et.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();
            }
        });



        date_et.setKeyListener(null);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        date_et.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(Appoinment.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        ArrayAdapter<String> spinnerArrayAdapterGender = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, spinnerGender);
        spinnerArrayAdapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item_divider);
        sp_gender.setAdapter(spinnerArrayAdapterGender);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEdt.getText().toString().equalsIgnoreCase("")) {
                    nameEdt.setError("Filed can't be blank!");
                    nameEdt.requestFocus();
                } else if (mobileEdt.getText().toString().equalsIgnoreCase("")) {
                    mobileEdt.setError("Filed can't be blank!");
                    mobileEdt.requestFocus();
                } else if (sp_gender.getSelectedItem().toString().equalsIgnoreCase("Select Gender")) {
                    Toast.makeText(getApplicationContext(), "Please Select gender", Toast.LENGTH_SHORT).show();
                } else if (age_et.getText().toString().equalsIgnoreCase("")) {
                    age_et.setError("Filed can't be blank!");
                    age_et.requestFocus();
                } else if (date_et.getText().toString().equalsIgnoreCase("")) {
                    date_et.setError("Filed can't be blank!");
                    date_et.requestFocus();
                } else if (message_et.getText().toString().equalsIgnoreCase("")) {
                    message_et.setError("Filed can't be blank!");
                    message_et.requestFocus();
                } else {
                    AppoitmentAPI();
                }
            }
        });
    }

    public void AppoitmentAPI() {
        final ProgressDialog progressDialog = new ProgressDialog(Appoinment.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL + "customer/bookappointment", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(Appoinment.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject.getString("message").equalsIgnoreCase("Appoitment Book Successfull")) {
                          Intent intent=new Intent(getApplicationContext(),ViewAppoitmentList.class);
                            intent.putExtra("doctorid", doctorId);
                            startActivity(intent);
                        }
                    } else {
                        Toast.makeText(Appoinment.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(Appoinment.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("cust_id", userData.getUserId());
                params.put("doctor_id",doctorId);
                params.put("name", nameEdt.getText().toString());
                params.put("mobile_no", mobileEdt.getText().toString());
                params.put("gender", sp_gender.getSelectedItem().toString());
                params.put("age", age_et.getText().toString());
                params.put("scheduled_date", date_et.getText().toString());
                params.put("scheduled_time", time_et.getText().toString());
                params.put("purpose", message_et.getText().toString());
                return params;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Appoinment.this);
        requestQueue.add(stringRequest);
    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        date_et.setText(sdf.format(myCalendar.getTime()));
    }
}
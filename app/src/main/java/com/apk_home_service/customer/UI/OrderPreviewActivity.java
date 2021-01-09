package com.apk_home_service.customer.UI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.apk_home_service.Payu.MainPaymentActivuity;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.Wallet.API_Config;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.CreateOrder;
import com.apk_home_service.customer.modal.FoodItemList;
import com.apk_home_service.customer.modal.TrollyModel;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.network.FPModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.Constants;
import com.apk_home_service.customer.utill.DateTimeHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

import static com.apk_home_service.customer.Wallet.API_Config.getApiClient_ByPost;

public class OrderPreviewActivity extends AppCompatActivity {

    final int REQUEST_DATE = 100;
    final int SELECT_ADDRESS = 110;
    ProgressDialog pDialog;
    TrollyModel trolly;
    UserData userData;
    boolean isCash = true;
    boolean isNow = true;
    String dateStr = "", paymentMode = "";
    ImageView trollyImg, nowImg, codImg, cashImg, laterImg, edtImg, timePicker;
    TextView trollyName, addTxt, nowTxt, laterTxt, delvDateTxt,
            codTxt, cashTxt, amntTxt, tamntTxt, totalValue, cgstamntTxt, gstamntTxt, gstTxt, cgstTxt;
    LinearLayout orderListLayout;
    Activity mActivity;
    JSONObject dataJson = new JSONObject();
    TextView discountTxt, timePickerText;
    CheckBox walletChkbox;
    TextView applyCouponText;
    TextView shippingAmt, packagingTxt, couponTxt;
    ImageView datePicker;
    Intent intents;
    String res_id;
    String res_name,res_image;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_preview);
        mActivity = this;
        applyCouponText = findViewById(R.id.applyCouponText);
        pDialog = CommonUtill.showProgressDialog(this, "Please wait...");

        intents = getIntent();
        res_id=intents.getStringExtra("rest_id");
        res_name=intents.getStringExtra("rest_name");
        res_image=intents.getStringExtra("rest_img");



        getGeneralDetails();
        getAvailableCoupons();

        try {
            userData = (UserData) PrefrenceHandler.getPreferences(OrderPreviewActivity.this).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            dataJson = new JSONObject(getIntent().getStringExtra("resultJSON"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        datePicker = findViewById(R.id.datePicker);
        dateStr = DateTimeHandler.changeDateToString(new Date(), DateTimeHandler.DATE_FORMATE_8);
        paymentMode = "COD";
        orderListLayout = (LinearLayout) findViewById(R.id.orderListLayout);
        shippingAmt = (TextView) findViewById(R.id.shippingAmt);
        couponTxt = (TextView) findViewById(R.id.couponTxt);
        packagingTxt = (TextView) findViewById(R.id.packagingTxt);
        trollyImg = (ImageView) findViewById(R.id.trollyImg);
        nowImg = (ImageView) findViewById(R.id.nowImg);
        edtImg = (ImageView) findViewById(R.id.edtImg);
        laterImg = (ImageView) findViewById(R.id.laterImg);
        codImg = (ImageView) findViewById(R.id.codImg);
        cashImg = (ImageView) findViewById(R.id.cashImg);
        cashTxt = (TextView) findViewById(R.id.cashTxt);
        amntTxt = (TextView) findViewById(R.id.amntTxt);
        discountTxt = (TextView) findViewById(R.id.discountTxt);
        timePickerText = (TextView) findViewById(R.id.timePickerText);
        totalValue = (TextView) findViewById(R.id.totalValue);
        tamntTxt = (TextView) findViewById(R.id.tamntTxt);
        cgstamntTxt = (TextView) findViewById(R.id.cgstamntTxt);
        gstamntTxt = (TextView) findViewById(R.id.gstamntTxt);
        gstTxt = (TextView) findViewById(R.id.gstTxt);
        cgstTxt = (TextView) findViewById(R.id.cgstTxt);
        trollyName = (TextView) findViewById(R.id.trollyName);
        addTxt = (TextView) findViewById(R.id.addTxt);
        nowTxt = (TextView) findViewById(R.id.nowTxt);
        laterTxt = (TextView) findViewById(R.id.laterTxt);
        delvDateTxt = (TextView) findViewById(R.id.delvDateTxt);
        walletChkbox = (CheckBox) findViewById(R.id.walletChkbox);
        codTxt = (TextView) findViewById(R.id.codTxt);
        applyCouponText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isCouponCodeSelected) {
                    addCouponPopUp();
                } else {
                    Toast.makeText(mActivity, "Coupon already applied", Toast.LENGTH_SHORT).show();
                }
            }
        });
        edtImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderPreviewActivity.this, ChangeOrderAddressActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
            }
        });

        addTxt.setText(userData.getStreet() + " " + userData.getCityName() + " " + userData.getStateName() + " " +
                userData.getCountry() + ", " + userData.getPin_code());
        dishList = (ArrayList<FoodItemList>) getIntent().getSerializableExtra("foodArrayList");
        CommonUtill.printClassData(dishList);
        addOrderRows(dishList);


        setFinalAmount();


        findViewById(R.id.deliverNowBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNow = true;
                handleDelivery();
            }
        });

        findViewById(R.id.deliverLaterBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNow = false;
                handleDelivery();
            }
        });

        findViewById(R.id.codBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCash = false;
                handlePayment();
            }
        });

        findViewById(R.id.cashBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCash = true;
                handlePayment();
            }
        });
        getDate();

//        findViewById(R.id.datePicker).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isNow) {
//                    Intent intent = new Intent(OrderPreviewActivity.this, DateChooseActivity.class);
//                    startActivityForResult(intent, REQUEST_DATE);
//                }
//            }
//        });
        findViewById(R.id.timePicker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mHour, mMinute;
                if (!isNow) {
                    final Calendar c = Calendar.getInstance();
                    mHour = c.get(Calendar.HOUR_OF_DAY);
                    mMinute = c.get(Calendar.MINUTE);

                    // Launch Time Picker Dialog
                    TimePickerDialog timePickerDialog = new TimePickerDialog(mActivity,
                            new TimePickerDialog.OnTimeSetListener() {

                                @Override
                                public void onTimeSet(TimePicker view, int hourOfDay,
                                                      int minute) {

                                    timePickerText.setText(hourOfDay + ":" + minute);
                                }
                            }, mHour, mMinute, true);
                    timePickerDialog.show();
                }
            }
        });


        findViewById(R.id.orderTxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentMode.equalsIgnoreCase("payumoney")) {
                    startActivityForResult(new Intent(mActivity, MainPaymentActivuity.class)
                            .putExtra("name", userData.getUserName())
                            .putExtra("email", userData.getEmailId())
                            .putExtra("mobile", userData.getPhoneNo())
                            .putExtra("product", "MyTreatDishes")
                            .putExtra("amount", String.format("%.2f", (totalAmt))  + ""), 1321
                    );
                } else {
                    placeOrder();
                }


            }
        });

//        findViewById(R.id.redeemCoupon).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isCouponCodeSelected)
//                    checkCoupon();
//                else
//                    Toast.makeText(OrderPreviewActivity.this, "Coupon already applied.", Toast.LENGTH_SHORT).show();
//            }
//        });


        findViewById(R.id.menuImg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getWalletDetails();
    }

    Calendar myCalendar;

    void getDate() {
        myCalendar = Calendar.getInstance();

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

        datePicker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                if (!isNow) {
                    //start changes...
                    DatePickerDialog dialog =
                            new DatePickerDialog(OrderPreviewActivity.this, date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH));
                    dialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (4 * 24 * 60 * 60 * 1000));
                    dialog.getDatePicker().setMinDate(System.currentTimeMillis() + (0));

                    dialog.show();
                }
            }
        });
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        delvDateTxt.setText(sdf.format(myCalendar.getTime()));
    }

    double shippingChargePercent = 0;
    double freeShippingChargesAmount = 0;
    double packagingCharges = 0;
    double packagingMaxValue = 0;
    double discountPercent = 0;
    double cgstPercent = 0;
    double gstPercent = 0;
    JSONObject generalDetailsJSON;

    void getGeneralDetails() {
        String str = com.apk_home_service.customer.data.Constants.getGeneralSettings(mActivity);
        try {
            generalDetailsJSON = new JSONObject(str);
            shippingChargePercent = Double.parseDouble(generalDetailsJSON.getJSONObject("data").getJSONObject("shipping").getString("shipping_charges"));
            freeShippingChargesAmount = Double.parseDouble(generalDetailsJSON.getJSONObject("data").getJSONObject("shipping").getString("no_shipping_above"));
            packagingCharges = Double.parseDouble(generalDetailsJSON.getJSONObject("data").getJSONObject("packaging").getString("packing_charges"));
            packagingMaxValue = Double.parseDouble(generalDetailsJSON.getJSONObject("data").getJSONObject("packaging").getString("no_packing_below"));
            gstPercent = Double.parseDouble(generalDetailsJSON.getJSONObject("data").getJSONObject("gst").getString("sgst"));
            cgstPercent = Double.parseDouble(generalDetailsJSON.getJSONObject("data").getJSONObject("gst").getString("csgt"));
            discountPercent = Double.parseDouble(generalDetailsJSON.getJSONObject("data").getJSONObject("offer").getString("amount"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void setFinalAmount() {
        try {
            float gst = 0;
            float cgst = 0;
            float discount = 0;
            {
                try {

                    try {
                        discount = (float) (totalAmt * discountPercent);
                        discount = discount / 100;
                        discount = Float.parseFloat(String.format(Locale.US, "%.2f", discount));
                        totalAmt = totalAmt - discount;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    totalAmt = totalAmt - couponDiscount;
                    gst = (float) (totalAmt * gstPercent);
                    gst = gst / 100;
                    gst = Float.parseFloat(String.format(Locale.US, "%.2f", gst));
                    cgst = (float) (totalAmt * cgstPercent);
                    cgst = cgst / 100;
                    cgst = Float.parseFloat(String.format(Locale.US, "%.2f", cgst));

                    if (totalAmt > freeShippingChargesAmount) {
                        freeShippingChargesAmount = 0;
                    } else {
                        freeShippingChargesAmount = shippingChargePercent / 100.0 * totalAmt;
                    }
                    if (totalAmt <= packagingMaxValue) {
                        packagingCharges = 0;
                    }


                    gstamntTxt.setText(getResources().getString(R.string.rupee) + String.format("%.2f", (gst + cgst)) + "");
                    cgstamntTxt.setText(getResources().getString(R.string.rupee) + String.format("%.2f", (gst + cgst)) + "");
                    discountTxt.setText(getResources().getString(R.string.rupee) + String.format("%.2f", discount) + "");
                    shippingAmt.setText(getResources().getString(R.string.rupee) + String.format("%.2f", freeShippingChargesAmount) + "");
                    packagingTxt.setText(getResources().getString(R.string.rupee) + String.format("%.2f", packagingCharges) + "");
                    tamntTxt.setText(getResources().getString(R.string.rupee) + String.format("%.2f", totalAmt) + "");
                    couponTxt.setText(getResources().getString(R.string.rupee) + String.format("%.2f", couponDiscount) + "");
                    totalAmt = totalAmt + cgst + gst + freeShippingChargesAmount + packagingCharges;

                    totalValue.setText(getResources().getString(R.string.rupee) + (int) Math.round(totalAmt) + "");


                } catch (Exception e) {
                    e.printStackTrace();
                }


//            ArrayList<FoodItemList> foodArrayList = (Trolly) getIntent().getSerializableExtra("trolly");
                trollyName.setText(res_name);
                Glide.with(this)
                        .load(res_image)
                        .error(R.drawable.logo)
                        .crossFade()
                        .centerCrop()
                        .into(trollyImg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void checkCoupon(String code) {
        if (code.trim().length() == 0) {
            Toast.makeText(this, "Please enter coupon code.", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            boolean flag = false;
            String selectedId = "";
            for (int i = 0; i < availableCouponCodeList.size(); i++) {
                if (availableCouponCodeList.get(i).getString("coupan_key").equalsIgnoreCase(code.trim())
                        && availableCouponCodeList.get(i).getString("validity").equalsIgnoreCase("available")) {
                    flag = true;
                    selectedId = availableCouponCodeList.get(i).getString("id");
                    break;
                }
            }
            if (flag) {
                applyCoupon(selectedId);
            } else {
                Toast.makeText(this, "Invalid Coupon.", Toast.LENGTH_SHORT).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    double maxWalletAmount = 0;
    double currentWalletAmount = 0;
    boolean useWallet = true;
    double usableWalletAmount = 0;

    void getWalletDetails() {
        try {
            JSONObject mObject = new JSONObject(com.apk_home_service.customer.data.Constants.getGeneralSettings(mActivity));
            maxWalletAmount = Double.parseDouble(mObject.getJSONObject("data").getJSONObject("use_wallet_amt").getString("max_amount"));
            useWallet = (mObject.getJSONObject("data").getJSONObject("use_wallet_amt").getString("is_wallet").equalsIgnoreCase("yes"));
            currentWalletAmount = Double.parseDouble(mObject.getJSONObject("data").getString("wallet_total"));
            if (!useWallet) {
                walletChkbox.setChecked(false);
                walletChkbox.setClickable(false);

            }
            usableWalletAmount = currentWalletAmount > maxWalletAmount ? maxWalletAmount : currentWalletAmount;
            walletChkbox.setText(getResources().getString(R.string.walletText).replace("20", usableWalletAmount + ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    double couponDiscount = 0;
    String couponId = "";

    void applyCoupon(String couponID) {
        try {
            String otp = new GlobalAppApis().applyCoupon(couponID, userData.getUserId());
            ApiService client = API_Config.getApiClient_ByPost();
            Call<String> call = client.applyCoupon(otp);
            new ConnectToRetrofit(new RetrofitCallBackListenar() {
                @Override
                public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        if (couponDialog != null && couponDialog.isShowing()) {
                            couponDialog.dismiss();
                        }
                        double CouponValue = Double.parseDouble(jsonObject.getJSONObject("data").getString("coupan_value"));
                        double minAmt = Double.parseDouble(jsonObject.getJSONObject("data").getString("restrict_min_order"));
                        if (intialAmt < minAmt) {
                            Toast.makeText(mActivity, "Minimum Order amount should be atleast " + getResources().getString(R.string.rupee) + minAmt, Toast.LENGTH_SHORT).show();
                        } else {
                            isCouponCodeSelected = true;
                            couponDiscount = CouponValue;
                            totalAmt = intialAmt;
                            couponId = jsonObject.getJSONObject("data").getString("id");
                            getGeneralDetails();
                            setFinalAmount();
                        }

                    } else {
                        Toast.makeText(mActivity, "Please try again.", Toast.LENGTH_SHORT).show();

                    }
                }

            }, mActivity, call, "", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Dialog couponDialog;

    void addCouponPopUp() {
        couponDialog = new Dialog(mActivity);
        couponDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        couponDialog.setCancelable(true);
        couponDialog.setContentView(R.layout.add_coupon_dialog);
        couponDialog.setCanceledOnTouchOutside(false);
        couponDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        final EditText dialogTitle = (EditText) couponDialog.findViewById(R.id.msgTxt);
        TextView noBtn = (TextView) couponDialog.findViewById(R.id.cancelTxt);
        TextView yesBtn = (TextView) couponDialog.findViewById(R.id.confirmTxt);
        noBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                couponDialog.dismiss();
            }
        });
        yesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = dialogTitle.getText().toString().trim();
                if (text.length() == 0) {
                    Toast.makeText(mActivity, "Please enter coupon code", Toast.LENGTH_SHORT).show();
                } else {
                    checkCoupon(text);
                }
            }
        });
        couponDialog.show();
    }


    ArrayList<FoodItemList> dishList;
    double totalAmt = 0, intialAmt = 0;

    void addOrderRows(ArrayList<FoodItemList> orderList) {
        try {
            orderListLayout.removeAllViews();
            for (int i = 0; i < orderList.size(); i++) {
                FoodItemList mItem = orderList.get(i);
                View view = LayoutInflater.from(mActivity).inflate(R.layout.order_row, null);
                TextView itemName = view.findViewById(R.id.itemName);
                TextView cosdtTv = view.findViewById(R.id.cosdtTv);
                TextView qntyTv = view.findViewById(R.id.qntyTv);
                if (mItem.getQuantity() > 0) {
                    try {
                        itemName.setText(mItem.getDishName());
                        qntyTv.setText(mItem.getQuantity() + " pcs");
                        totalAmt = totalAmt + (mItem.getQuantity()) *
                                Double.parseDouble(mItem.getPrice());
                        cosdtTv.setText(getResources().getString(R.string.rupee) + String.format("%.2f", mItem.getQuantity() *
                                Double.parseDouble(mItem.getPrice())));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    intialAmt = totalAmt;
                    orderListLayout.addView(view);

                }
            }
            amntTxt.setText(getResources().getString(R.string.rupee) + "" + String.format("%.2f", totalAmt));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ArrayList<JSONObject> availableCouponCodeList = new ArrayList<>();
    boolean isCouponCodeSelected = false;

    void getAvailableCoupons() {
        try {
            ApiService client = API_Config.getApiClient_ByPost();
            Call<String> call = client.availableCoupons("");
            new ConnectToRetrofit(new RetrofitCallBackListenar() {
                @Override
                public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray mArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < mArray.length(); i++) {
                            availableCouponCodeList.add(mArray.getJSONObject(i));
                        }
                    } else {
                        Toast.makeText(mActivity, "Please try again.", Toast.LENGTH_SHORT).show();

                    }
                }

            }, mActivity, call, "", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void placeOrder() {
//       String otp = new GlobalAppApis().walletDeatils(this);
        ApiService client = getApiClient_ByPost();
        Call<String> call = client.createOrder(createRequestJSON());
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
                    if (jsonObject.has("data") && jsonObject.getJSONObject("data").length() > 0) {
                        Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(mActivity, BottomNavigationActivity.class);
                        i.putExtra("order_id", jsonObject.getJSONObject("data").getString("id"));
// set the new task and clear flags
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        finish();
                    } else {
                        Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            }

        }, mActivity, call, "", true);
    }

    String createRequestJSON() {
        JSONObject requestJSONObject = new JSONObject();
        try {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat df1 = new SimpleDateFormat("HH:mm");
            String formattedDate = df.format(c);
            requestJSONObject.put("shipping_address", userData.getStreet());
            requestJSONObject.put("city_id", userData.getCity());
            requestJSONObject.put("state_id", userData.getState());

            requestJSONObject.put("pincode", userData.getPin_code());
            requestJSONObject.put("latitude", Constants.getSavedPreferences(mActivity, com.apk_home_service.customer.utill.Constants.LATITUDE, ""));
            requestJSONObject.put("longitude", Constants.getSavedPreferences(mActivity, Constants.LONGITUDE, ""));

            requestJSONObject.put("restaurant_id", res_id);
          //  Toast.makeText(mActivity, res_id, Toast.LENGTH_SHORT).show();

            requestJSONObject.put("customer_id", userData.getUserId());
            requestJSONObject.put("pay_mode", paymentMode);
            requestJSONObject.put("wallet_used", walletChkbox.isChecked() ? "yes" : "no");
            requestJSONObject.put("wallet_money", "0");
//            requestJSONObject.put("payfrom_wallet", walletChkbox.isChecked() ? "yes" : "no");
            requestJSONObject.put("scheduled_date", formattedDate);
            requestJSONObject.put("coupan_used", isCouponCodeSelected ? "yes" : "no");
            requestJSONObject.put("coupan_id", couponId);
            requestJSONObject.put("coupan_amount", couponDiscount + "");
            requestJSONObject.put("scheduled_time", df1.format(c));
            requestJSONObject.put("sub_total", amntTxt.getText().toString()
                    .replace(getResources().getString(R.string.rupee), "").trim());
            requestJSONObject.put("cgst", cgstamntTxt.getText().toString()
                    .replace(getResources().getString(R.string.rupee), "").trim());
            requestJSONObject.put("sgst", gstamntTxt.getText().toString()
                    .replace(getResources().getString(R.string.rupee), "").trim());
            requestJSONObject.put("discount", "0");
            requestJSONObject.put("total", totalValue.getText().toString()
                    .replace(getResources().getString(R.string.rupee), "").trim());


            requestJSONObject.put("shipping", (freeShippingChargesAmount == 0) ? "no" : "yes");
            requestJSONObject.put("shipping_charges", freeShippingChargesAmount + "");
            requestJSONObject.put("packing", (packagingCharges == 0) ? "no" : "yes");
            requestJSONObject.put("packing_charges", packagingCharges + "");
            requestJSONObject.put("cgst_percent", cgstPercent + "");
            requestJSONObject.put("sgst_percent", gstPercent + "");
            requestJSONObject.put("discount_percent", discountPercent + "");
            requestJSONObject.put("shipping_percent", shippingChargePercent + "");


            requestJSONObject.put("dishes", createDishesArray());

            if(paymentMode.equalsIgnoreCase("payumoney"))
            {
                requestJSONObject.put("trnsID", trnsID);
                requestJSONObject.put("payUHash", payUHash);

            }

            System.out.println("==response array is " + requestJSONObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return requestJSONObject.toString();
    }


    String payUHash = "";
    String trnsID = "";
    JSONArray createDishesArray() {
        JSONArray responseArray = new JSONArray();
        try {
            for (int i = 0; i < dishList.size(); i++) {
                FoodItemList mItem = dishList.get(i);
                if (mItem.getQuantity() > 0) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("dish_id", mItem.getDishId());
                  //  Toast.makeText(getApplicationContext(),"esgrdgf"+mItem.getDishId(),Toast.LENGTH_LONG).show();

                    jsonObject.put("dish_quantity", mItem.getQuantity());
                    jsonObject.put("dish_sale_price", mItem.getPrice());
                    responseArray.put(jsonObject);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseArray;
    }

    private void handlePayment() {
        if (isCash) {
            paymentMode = "handlePayment";
            cashImg.setImageResource(R.drawable.selected);
            codImg.setImageResource(R.drawable.unselected);
            cashTxt.setTextColor(getResources().getColor(R.color.colorGreen));
            codTxt.setTextColor(getResources().getColor(R.color.colorDivider));
        } else {
            paymentMode = "COD";
            cashImg.setImageResource(R.drawable.unselected);
            codImg.setImageResource(R.drawable.selected);
            cashTxt.setTextColor(getResources().getColor(R.color.colorDivider));
            codTxt.setTextColor(getResources().getColor(R.color.colorGreen));
        }
    }

    private void handleDelivery() {
        if (isNow) {
            delvDateTxt.setText("");
            dateStr = DateTimeHandler.changeDateToString(new Date(), DateTimeHandler.DATE_FORMATE_8);
            nowImg.setImageResource(R.drawable.selected);
            laterImg.setImageResource(R.drawable.unselected);
            nowTxt.setTextColor(getResources().getColor(R.color.colorGreen));
            laterTxt.setTextColor(getResources().getColor(R.color.colorDivider));
        } else {
            dateStr = "";
            nowImg.setImageResource(R.drawable.unselected);
            laterImg.setImageResource(R.drawable.selected);
            nowTxt.setTextColor(getResources().getColor(R.color.colorDivider));
            laterTxt.setTextColor(getResources().getColor(R.color.colorGreen));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_DATE && resultCode == RESULT_OK && data != null) {
            CommonUtill.print("REQUEST_DATE ==" + data.getStringExtra("date"));
            try {
                Date date = DateTimeHandler.changeStringToDate(data.getStringExtra("date"), DateTimeHandler.DATE_FORMATE_5);
                dateStr = DateTimeHandler.changeDateToString(date, DateTimeHandler.DATE_FORMATE_8);
                delvDateTxt.setText(dateStr);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else if (requestCode == SELECT_ADDRESS && resultCode == RESULT_OK && data != null) {
            CommonUtill.print("SELECT_ADDRESS ==" + data.getStringExtra("address"));
            addTxt.setText(data.getStringExtra("address"));
//            intent.putExtra("name", data.getStringExtra("name"));
//            intent.putExtra("mobile", data.getStringExtra("mobile"));
//            intent.putExtra("email", data.getStringExtra("email"));
//            intent.putExtra("street",data.getStringExtra("street"));
//            intent.putExtra("city", data.getStringExtra("city"));
//            intent.putExtra("state", data.getStringExtra("state"));
//            intent.putExtra("country", data.getStringExtra("country"));
//            intent.putExtra("pin", data.getStringExtra("pin"));
//            createOrderAddress(data);
        } else if (requestCode == 1321 && resultCode == RESULT_OK) {
            trnsID = data.getStringExtra("trnsID");
            payUHash = data.getStringExtra("payUHash");


            placeOrder();
        }
    }


    private void createOrderAddress(Intent data) {

        CreateOrder requestModal = new CreateOrder();
        requestModal.setName(data.getStringExtra("name"));
        requestModal.setOrder_id(getIntent().getStringExtra("orderId"));
        requestModal.setPin_code(data.getStringExtra("pin"));
        requestModal.setCountry(data.getStringExtra("country"));
        requestModal.setState(data.getStringExtra("state"));
        requestModal.setCity(data.getStringExtra("city"));
        requestModal.setStreet(data.getStringExtra("street"));
        requestModal.setMobile(data.getStringExtra("mobile"));
        requestModal.setEmail(data.getStringExtra("email"));

        CommonUtill.printClassData(requestModal);

        pDialog.show();
        new FPModel(this, FPModel.CRAZYBILLA_API).fpApi.getUpdateShipingAddress(PrefrenceHandler.getPreferences(this).getAPI_TOKEN(), requestModal, new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                pDialog.dismiss();
                CommonUtill.print("createOrderAddress result ==" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        CommonUtill.showTost(OrderPreviewActivity.this, jsonObject.getString("message"));
                    } else {
                        CommonUtill.showTost(OrderPreviewActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();
                CommonUtill.print("createOrderAddress error ==" + error.getMessage());
            }
        });

    }


}

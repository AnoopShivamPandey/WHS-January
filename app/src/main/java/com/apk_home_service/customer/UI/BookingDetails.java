package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.Wallet.API_Config;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.data.Constants;
import com.apk_home_service.customer.modal.TrollyModel;
import com.apk_home_service.customer.utill.CancelOrderDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import retrofit2.Call;


public class BookingDetails extends AppCompatActivity implements View.OnClickListener {
    Activity mActivity;
    LinearLayout orderListLayout;
    String order_id = "";
    TextView proceedToDeliveryTv;
    TextView cancelOrderBtn;
    CardView trackOrderCard;
    TextView repeatOrder;
    String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_details);
        mActivity = this;
        initialize();
        order_id = getIntent().getStringExtra("order_id");
        findViewById(R.id.contactUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, ContactUs.class));
            }
        });

    }

    CircularImageView customerImage;
    ImageView  sendSMSIV, mobIV, locIv;
    TextView acceptBooking, rejectBooking, userDistance, customerName, customerAddress, customerContactNumber;
    TextView totalBill, headerTxt;
    ImageView proceedToDeliveryBtn;
    TextView assignedWorkerName, rejectTv;
    ImageView backImg;
    String stall_id = "";

    @Override
    protected void onResume() {
        super.onResume();
        try {
//            rejectTv.setVisibility(View.GONE);
            order_id = getIntent().getStringExtra("order_id");
            totalAmt = 0;
            getBookingListing(order_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void initialize() {
        try {
            repeatOrder = findViewById(R.id.repeatOrder);
            trackOrderCard = findViewById(R.id.trackOrderCard);
            rejectTv = findViewById(R.id.rejectTv);
            cancelOrderBtn = findViewById(R.id.cancelOrderBtn);
            proceedToDeliveryTv = findViewById(R.id.proceedToDeliveryTv);
            headerTxt = findViewById(R.id.headerTxt);
            backImg = findViewById(R.id.backImg);
            proceedToDeliveryBtn = findViewById(R.id.proceedToDeliveryBtn);
            assignedWorkerName = findViewById(R.id.assignedWorkerName);
            customerImage = findViewById(R.id.customerImage);
            orderListLayout = findViewById(R.id.orderListLayout);
            userDistance = findViewById(R.id.userDistance);
            customerName = findViewById(R.id.customerName);
            customerAddress = findViewById(R.id.customerAddress);
            customerContactNumber = findViewById(R.id.customerContactNumber);
            locIv = findViewById(R.id.locIv);
            sendSMSIV = findViewById(R.id.sendSMSIV);
            totalBill = findViewById(R.id.totalBill);
            mobIV = findViewById(R.id.mobIV);
            headerTxt.setText("Booking Detail");
            proceedToDeliveryBtn.setOnClickListener(this);
            sendSMSIV.setOnClickListener(this);
            mobIV.setOnClickListener(this);
            locIv.setOnClickListener(this);
            backImg.setOnClickListener(this);
            rejectTv.setOnClickListener(this);
            proceedToDeliveryTv.setOnClickListener(this);
            cancelOrderBtn.setOnClickListener(this);
            repeatOrder.setOnClickListener(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    TrollyModel mModel;

    public void getBookingListing(String order_id) {
        String otp = new GlobalAppApis().getBookingDetails(order_id);
        ApiService client = API_Config.getApiClient_ByPost();
        Call<String> call = client.getBookingDetails(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
                    if (jsonObject.has("data") && jsonObject.getJSONObject("data").length() > 0) {
                        Gson gson = new Gson();
                        JsonElement element = gson.fromJson(jsonObject.getJSONObject("data").getJSONObject("restaurant").toString(),
                                JsonElement.class);

                        Type listType = new TypeToken<TrollyModel>() {
                        }.getType();
                        mModel = gson.fromJson(element, listType);
                        setData(jsonObject.getJSONObject("data"));
                    } else {

                    }
                } else {
                }
            }

        }, mActivity, call, "", true);
    }


    String bookingId = "";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    void setData(JSONObject data) {
        try {
            try {
                stall_id = data.getJSONObject("restaurant").getString("id");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (data.has("items") && data.getJSONArray("items").length() > 0) {
                JSONArray orderDetailArray = data.getJSONArray("items");
                addOrderRows(orderDetailArray);
                setCustomerDetails(data.getJSONObject("restaurant"));
                status = (data.getString("orderstatus").toLowerCase());
                totalBill.setText(getResources().getString(R.string.rupee) + " " + (data.getString("total")) + "");
                if (data.has("orderstatus")) {
                    switch (data.getString("orderstatus").toLowerCase()) {
                        case "pending":
                            proceedToDeliveryTv.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.rounded_red));
                            proceedToDeliveryTv.setText("Pending");
                            proceedToDeliveryTv.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                            proceedToDeliveryTv.setGravity(Gravity.CENTER);
                            trackOrderCard.setVisibility(View.GONE);
                            break;
                        case "accepted":
                            proceedToDeliveryTv.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.rounded_green));
                            proceedToDeliveryTv.setText("Accepted");
                            proceedToDeliveryTv.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                            proceedToDeliveryTv.setGravity(Gravity.CENTER);
                            cancelOrderBtn.setVisibility(View.VISIBLE);
                            trackOrderCard.setVisibility(View.VISIBLE);
                            break;

                        case "processing":
                        case "out_for_delivery":
                            proceedToDeliveryTv.setText("On the Way");
                            proceedToDeliveryTv.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.rounded_green));
                            proceedToDeliveryTv.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                            proceedToDeliveryTv.setGravity(Gravity.CENTER);
                            cancelOrderBtn.setVisibility(View.GONE);
                            trackOrderCard.setVisibility(View.VISIBLE);
                            break;

                        case "cancelled":
                            proceedToDeliveryTv.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.rounded_red));
                            proceedToDeliveryTv.setText("Cancelled");
                            proceedToDeliveryTv.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                            proceedToDeliveryTv.setGravity(Gravity.CENTER);
                            trackOrderCard.setVisibility(View.GONE);
                            cancelOrderBtn.setVisibility(View.GONE);
                            break;

                        case "delivered":
                            proceedToDeliveryTv.setText("Delivered");

                            proceedToDeliveryTv.setBackground(ContextCompat.getDrawable(mActivity, R.drawable.rounded_green));
                            proceedToDeliveryTv.setTextColor(ContextCompat.getColor(mActivity, R.color.white));
                            proceedToDeliveryTv.setGravity(Gravity.CENTER);

                            trackOrderCard.setVisibility(View.GONE);
                            cancelOrderBtn.setVisibility(View.GONE);
                            repeatOrder.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                proceedToDeliveryTv.setOnClickListener(this);
                rejectTv.setOnClickListener(this);
                rejectTv.setPadding(40, 15, 40, 15);
                rejectTv.setGravity(Gravity.CENTER);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    String customerNumber = "";
    double totalAmt = 0;

    void setCustomerDetails(JSONObject customerJSON) {
        try {
            Glide.with(mActivity).load(customerJSON.getString("restaurant_image"))
                    .error(R.drawable.logo).fitCenter()
                    .placeholder(R.drawable.logo)
                    .into(customerImage);
            customerName.setText(customerJSON.getString("full_name"));
            customerNumber = customerJSON.getString("mobile");
            customerContactNumber.setText(customerNumber);
            customerAddress.setText(customerJSON.getString("street"));
//            customerlat = Double.parseDouble(customerJSON.getString("latitude"));
//            customerLong = Double.parseDouble(customerJSON.getString("longitude"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    double customerlat = 0, customerLong = 0;

    void setCustomerAddress(JSONObject resultJSON) {
        try {
            String userAddress = (resultJSON.has("street") ? resultJSON.getString("street") + "," : "") + " "
                    + (resultJSON.has("city") ? resultJSON.getString("city") + "," : "") + " "
                    + (resultJSON.has("state") ? resultJSON.getString("state") + "," : "") + " "
                    + (resultJSON.has("country") ? resultJSON.getString("country") + "," : "") + " "
                    + (resultJSON.has("pin_code") ? resultJSON.getString("pin_code") : "");
            customerAddress.setText(userAddress);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addOrderRows(JSONArray orderDetailArray) {
        try {
            orderListLayout.removeAllViews();
            for (int i = 0; i < orderDetailArray.length(); i++) {
                View view = LayoutInflater.from(mActivity).inflate(R.layout.order_row, null);
                TextView itemName = view.findViewById(R.id.itemName);
                TextView cosdtTv = view.findViewById(R.id.cosdtTv);
                TextView qntyTv = view.findViewById(R.id.qntyTv);
                try {
                    itemName.setText(orderDetailArray.getJSONObject(i).getString("dish_name"));
                    qntyTv.setText(orderDetailArray.getJSONObject(i).getString("dish_qty") + " pcs");
                    totalAmt = totalAmt + Double.parseDouble(orderDetailArray.getJSONObject(i).getString("dish_qty")) *
                            Double.parseDouble(orderDetailArray.getJSONObject(i).getString("dish_sale_price"));
                    cosdtTv.setText(String.format("%.2f", Double.parseDouble(orderDetailArray.getJSONObject(i).getString("dish_qty")) *
                            Double.parseDouble(orderDetailArray.getJSONObject(i).getString("dish_sale_price"))));
//                    cosdtTv.setText(getResources().getString(R.string.rupee) + " " + Double.parseDouble(orderDetailArray.getJSONObject(i).getString("dish_qty")) *
//                            Double.parseDouble(orderDetailArray.getJSONObject(i).getString("dish_sale_price")));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                orderListLayout.addView(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mobIV:
                Constants.openDiallerPad(customerNumber, mActivity);
                break;
            case R.id.sendSMSIV:
                Constants.sendSMSDialog(customerNumber, mActivity);
                break;
            case R.id.locIv:
                Constants.openGoogleMapNaviagtion(mActivity, customerlat, customerLong);
                break;

            case R.id.proceedToDeliveryBtn:
                proceedToOrder("ontheway");
                break;
            case R.id.backImg:
                finish();
                break;
            case R.id.rejectTv:
                Intent intent = new Intent(mActivity, OrderTrackActivity.class);
                intent.putExtra("orderId", order_id);
                intent.putExtra("status", status);
                startActivity(intent);
                break;
            case R.id.cancelOrderBtn:
                rejectOrder();
                break;
            case R.id.repeatOrder:
                Intent mIntent = new Intent(mActivity, TrollyMenuListActivity.class);
                mIntent.putExtra("trolly", mModel);
                mIntent.putExtra("mapLat", Double.parseDouble(com.apk_home_service.customer.utill.Constants.getSavedPreferences(mActivity, com.apk_home_service.customer.utill.Constants.LATITUDE, "")));
                mIntent.putExtra("mapLong", Double.parseDouble(com.apk_home_service.customer.utill.Constants.getSavedPreferences(mActivity, com.apk_home_service.customer.utill.Constants.LATITUDE, "")));
                startActivity(mIntent);
                break;

        }
    }

    void acceptRejectOrder(String status) {
        try {
           /* String bookingID = getIntent().getStringExtra("order_id");
            String otp = new GlobalAppApis().sendAcceptRejectDetails(this, bookingID, status);
            ApiService client = API_Config.getApiClient_ByPost();
            Call<String> call = client.acceptrejectOrder(Constants.getAPIToken(mActivity), otp);
            new ConnectToRetrofit(new RetrofitCallBackListenar() {
                @Override
                public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        if (jsonObject.has("data") && jsonObject.getJSONObject("data").length() > 0) {
                            Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                            finish();
                       onResume();
                        } else {
                            Toast.makeText(mActivity, "Please try again.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                    }
                }

            }, mActivity, call, "", true);
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Dialog mDialog;

    void rejectOrder() {
        mDialog = new CancelOrderDialog(mActivity, new CancelOrderDialog.ConfirmationDialogListener() {
            @Override
            public void onYesButtonClicked(CancelOrderDialog.confirmTask task, String msg) {
//                cancelOrder(msg);
                cancelUserOrder(msg);
            }

            @Override
            public void onNoButtonClicked(CancelOrderDialog.confirmTask task) {

            }
        }, CancelOrderDialog.confirmTask.NONE);
    }

    void cancelUserOrder(String msg) {
        try {
            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();

            }
            String otp = new GlobalAppApis().cancelBookingDetails(order_id, msg);
            ApiService client = API_Config.getApiClient_ByPost();
            Call<String> call = client.cancelUserOrder(otp);
            new ConnectToRetrofit(new RetrofitCallBackListenar() {
                @Override
                public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                            finish();
                        onResume();
                    } else {
                        Toast.makeText(mActivity, "Please try again.", Toast.LENGTH_SHORT).show();

                    }
                }

            }, mActivity, call, "", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void proceedToOrder(String status) {
        try {
           /* String otp = new GlobalAppApis().sendAcceptRejectDetails(this, order_id, status);
            ApiService client = API_Config.getApiClient_ByPost();
            Call<String> call = client.acceptrejectOrder(Constants.getAPIToken(mActivity), otp);
            new ConnectToRetrofit(new RetrofitCallBackListenar() {
                @Override
                public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        if (jsonObject.has("data") && jsonObject.getJSONObject("data").length() > 0) {
                            Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(mActivity, "Please try again.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                    }
                }

            }, mActivity, call, "", true);
*/

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}

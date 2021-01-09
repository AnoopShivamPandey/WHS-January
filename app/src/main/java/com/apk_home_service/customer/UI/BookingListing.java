package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.data.Constants;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

import static com.apk_home_service.customer.Wallet.API_Config.getApiClient_ByPost;


public class BookingListing extends AppCompatActivity {
    RecyclerView bookingListRecyclerView;
    Activity mActivity;
    ImageView backImg;
    TextView headerTxt;
    UserData userData;
    ImageView noRecordsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_listing);
        mActivity = this;
        try {
            userData = (UserData) PrefrenceHandler.getPreferences(mActivity).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        ArrayList<JSONObject> resultList = new ArrayList<>();
        bookingListRecyclerView.setAdapter(new BookingsAdapter(resultList));

        bookingListRecyclerView = findViewById(R.id.bookingListRecyclerView);
        backImg = findViewById(R.id.backImg);
        headerTxt = findViewById(R.id.headerTxt);
        noRecordsText = findViewById(R.id.noRecordsText);
        headerTxt.setText("Booking List");
        bookingListRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        bookingListRecyclerView.setLayoutManager(layoutManager);
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBookingListing();
    }

    public void getBookingListing() {
        String otp = new GlobalAppApis().getBookingList(userData.getUserId());
        ApiService client = getApiClient_ByPost();
        Call<String> call = client.getBookingList(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
                    if (jsonObject.has("data") && jsonObject.getJSONArray("data").length() > 0) {
                        showNoRecords(false);
                        setData(jsonObject);
                    } else {

                        showNoRecords(true);
                    }
                } else {
                    showNoRecords(true);
                }
            }

        }, mActivity, call, "", true);


    }

    private void showNoRecords(boolean show) {
        if (show) {
            noRecordsText.setVisibility(View.VISIBLE);
            bookingListRecyclerView.setVisibility(View.GONE);
        } else {
            noRecordsText.setVisibility(View.GONE);
            bookingListRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    void setData(JSONObject data) {
        try {
            ArrayList<JSONObject> resultList = new ArrayList<>();
            for (int i = 0; i < data.getJSONArray("data").length(); i++) {
                if(!resultList.contains(data.getJSONArray("data").getJSONObject(i)))
                resultList.add(data.getJSONArray("data").getJSONObject(i));
            }
            if (resultList.size() > 0) {
                bookingListRecyclerView.setAdapter(new BookingsAdapter(resultList));
            } else {
                showNoRecords(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class BookingsAdapter extends RecyclerView.Adapter<BookingsAdapter.BookingsHolder> {
        ArrayList<JSONObject> resultList;

        public BookingsAdapter(ArrayList<JSONObject> resultList) {
            this.resultList = resultList;
        }

        @NonNull
        @Override
        public BookingsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View mView = LayoutInflater.from(mActivity).inflate(R.layout.include_booking_list_row, null);
            return new BookingsHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull BookingsHolder holder, int position) {
            try {
                final JSONObject resultJSON = resultList.get(position);
                holder.customerName.setText(resultJSON.has("customer_name") ?
                        resultJSON.getString("customer_name") : "N/A");
                holder.customerContactNumber.setText(resultJSON.has("customer_phone") ?
                        resultJSON.getString("customer_phone") : "N/A");
                if (resultJSON.has("customer_pic") && resultJSON.getString("customer_pic").length() > 0) {
                    Glide.with(mActivity)
                            .load(resultJSON.getString("customer_pic"))
                            .error(R.drawable.logo)
                            .placeholder(R.drawable.logo)
                            .into(holder.customerImage);
                }

                if (resultJSON.has("customer_phone") && resultJSON.getString("customer_phone").length() > 0) {
                    holder.mobIV.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            try {
                                Constants.openDiallerPad(resultJSON.getString("customer_phone"), mActivity);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    holder.smsIv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            try {
                                Constants.sendSMSDialog(resultJSON.getString("customer_phone"), mActivity);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                } else {
                    holder.mobIV.setVisibility(View.GONE);
                    holder.smsIv.setVisibility(View.GONE);
                }
                try {
                    holder.customerDistance.setText(setDistance(resultJSON));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    String userAddress = (resultJSON.has("street") ? resultJSON.getString("street") + "," : "") + " "
                            + (resultJSON.has("city") ? resultJSON.getString("city") + "," : "") + " "
                            + (resultJSON.has("state") ? resultJSON.getString("state") + "," : "") + " "
                            + (resultJSON.has("country") ? resultJSON.getString("country") + "," : "") + " "
                            + (resultJSON.has("pin_code") ? resultJSON.getString("pin_code") : "");
                    holder.customerAddress.setText(userAddress);
                } catch (Exception e) {
                    holder.customerAddress.setText("N/A");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return resultList.size();
        }

        String setDistance(JSONObject customerJSON) {
            try {

                double distance = Constants.getDistanceInKm(Double.parseDouble(customerJSON.getString("customer_latitude").length() == 0 ? "0" : customerJSON.getString("customer_latitude")),
                        Double.parseDouble(customerJSON.getString("customer_longitude").length() == 0 ? "0" : customerJSON.getString("customer_longitude")),
                        Constants.getLatitude(mActivity), Constants.getLongitude(mActivity));
                return (Math.round(distance) + " km.");
            } catch (Exception e) {
                e.printStackTrace();
                return "0 km";
            }
        }

        class BookingsHolder extends RecyclerView.ViewHolder {
            ImageView customerImage, mobIV, smsIv;
            TextView customerName;
            TextView customerAddress;
            TextView customerContactNumber;
            TextView customerDistance;

            public BookingsHolder(@NonNull View itemView) {
                super(itemView);
                mobIV = itemView.findViewById(R.id.mobIV);
                smsIv = itemView.findViewById(R.id.smsIv);
                customerImage = itemView.findViewById(R.id.customerImage);
                customerName = itemView.findViewById(R.id.customerName);
                customerAddress = itemView.findViewById(R.id.customerAddress);
                customerContactNumber = itemView.findViewById(R.id.customerContactNumber);
                customerDistance = itemView.findViewById(R.id.customerDistance);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            startActivity(new Intent(mActivity, BookingDetails.class)
                                    .putExtra("order_id", resultList.get(getAdapterPosition()).getString("id")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        }
    }
}
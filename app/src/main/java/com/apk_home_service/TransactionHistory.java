package com.apk_home_service;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.Wallet.API_Config;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

public class TransactionHistory extends AppCompatActivity {
    Activity mActivity;
    RecyclerView transactionRecycler;
    UserData userData;
    private ImageView menuImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        menuImg=findViewById(R.id.menuImg);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mActivity = this;
        try {
            userData = (UserData) PrefrenceHandler.getPreferences(mActivity).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        transactionRecycler = findViewById(R.id.transactionRecycler);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        transactionRecycler.setLayoutManager(manager);
        getTransactionList();
    }

    public void getTransactionList() {
        String otp = new GlobalAppApis().getTransactionData(userData.getUserId());
        ApiService client = API_Config.getApiClient_ByPost();
        Call<String> call = client.getTransactionList(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
                    if (jsonObject.has("data") && jsonObject.getJSONArray("data").length() > 0) {
                        ArrayList<JSONObject> mList = new ArrayList<>();
                        for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {
                            mList.add(jsonObject.getJSONArray("data").getJSONObject(i));
                        }

                        transactionRecycler.setAdapter(new TransactionAdapter(mList));

                    } else {

                    }
                } else {
                }
            }

        }, mActivity, call, "", true);
    }


    class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {
        ArrayList<JSONObject> mList;

        public TransactionAdapter(ArrayList<JSONObject> mList) {
            this.mList = mList;
        }

        @NonNull
        @Override
        public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mActivity).inflate(R.layout.transaction_layout, parent, false);
            return new TransactionHolder(mView);
        }

        @Override
        public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
            try {
                JSONObject mObject = mList.get(position);
                if (mObject.getString("type").equalsIgnoreCase("credit")) {
                    holder.transactionImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_call_received_black_24dp));
                    holder.transactionStatus.setText("Received ");
                    holder.senderName.setText(mObject.getString("message"));
                } else {
                    holder.transactionImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_call_made_black_24dp));
                    holder.transactionStatus.setText("Sent");
                    holder.senderName.setText(mObject.getString("message"));
                }
                holder.priceTv.setText(getResources().getString(R.string.rupee) + " " + mObject.getString("amount"));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        class TransactionHolder extends RecyclerView.ViewHolder {
            ImageView transactionImage;
            TextView transactionStatus, priceTv, senderName;

            public TransactionHolder(View itemView) {
                super(itemView);
                transactionImage = itemView.findViewById(R.id.transactionImage);
                transactionStatus = itemView.findViewById(R.id.transactionStatus);
                senderName = itemView.findViewById(R.id.senderName);
                priceTv = itemView.findViewById(R.id.priceTv);
            }
        }
    }
}

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

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

import static com.apk_home_service.customer.Wallet.API_Config.getApiClient_ByPost;

public class CountryListing extends AppCompatActivity {
    RecyclerView countryRecyclerView;
    TextView noRecordsText;
    TextView headerTxt;
    ImageView backImg;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country_listing);
        countryRecyclerView = findViewById(R.id.countryRecyclerView);
        noRecordsText = findViewById(R.id.noRecordsText);
        headerTxt = findViewById(R.id.headerTxt);
        backImg = findViewById(R.id.backImg);
        mActivity = this;
        backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        showNoRecords(true);
        if (getIntent().getStringExtra("stateId").length() == 0) {
            headerTxt.setText("State List");
            getStateListing();

        } else {
            headerTxt.setText("City List");
            getCityListing();
        }
    }

    private void showNoRecords(boolean show) {
        if (show) {
            noRecordsText.setVisibility(View.VISIBLE);
            countryRecyclerView.setVisibility(View.GONE);
        } else {
            noRecordsText.setVisibility(View.GONE);
            countryRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void getStateListing() {
        ApiService client = getApiClient_ByPost();
        Call<String> call = client.getStateList("");
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

    public void getCityListing() {
        String otp = new GlobalAppApis().cityList(getIntent().getStringExtra("stateId"));
        ApiService client = getApiClient_ByPost();
        Call<String> call = client.getStateList(otp);
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


    void setData(JSONObject jsonObject) {
        try {
            JSONArray mArray = jsonObject.getJSONArray("data");
            ArrayList<JSONObject> stateList = new ArrayList<>();
            for (int i = 0; i < mArray.length(); i++) {
                stateList.add(mArray.getJSONObject(i));
            }
            LinearLayoutManager manager = new LinearLayoutManager(mActivity);
            countryRecyclerView.setLayoutManager(manager);
            CountryAdapter countryAdapter = new CountryAdapter(stateList);
            countryRecyclerView.setAdapter(countryAdapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.CountryViewHolder> {
        ArrayList<JSONObject> stateList;
        public CountryAdapter(ArrayList<JSONObject> stateList) {
            this.stateList = stateList;
        }
        @NonNull
        @Override
        public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View mView = LayoutInflater.from(mActivity).inflate(R.layout.country_row, null);
            return new CountryViewHolder(mView);
        }
        @Override
        public void onBindViewHolder(@NonNull final CountryViewHolder holder, int position) {
            try {
                if (stateList.get(position).has("state_name")) {
                    holder.countryName.setText(stateList.get(position).getString("state_name"));
                } else {
                    holder.countryName.setText(stateList.get(position).getString("city"));
                }
                holder.countryName.setTag(stateList.get(position).getString("id"));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent it = new Intent();
                        it.putExtra("id", holder.countryName.getTag().toString());
                        it.putExtra("name", holder.countryName.getText().toString());
                        setResult(RESULT_OK, it);
                        finish();
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        @Override
        public int getItemCount() {
            return stateList.size();
        }
        class CountryViewHolder extends RecyclerView.ViewHolder {
            TextView countryName;
            public CountryViewHolder(View itemView) {
                super(itemView);
                countryName = itemView.findViewById(R.id.countryName);
            }
        }
    }
}

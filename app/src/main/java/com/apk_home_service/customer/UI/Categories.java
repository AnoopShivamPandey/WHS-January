package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.Wallet.API_Config;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.controll.CircleTransform;
import com.apk_home_service.customer.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

public class Categories extends AppCompatActivity {
    View mView;
    TextView noRecordsText;
    RecyclerView orderList;
    Activity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_category);

        mActivity = this;
        noRecordsText =findViewById(R.id.noRecordsText);
        orderList =findViewById(R.id.orderList);
        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        orderList.setLayoutManager(manager);
        mAdapter = new CategoryListAdapter(availableCouponCodeList);
        orderList.setAdapter(mAdapter);
        showNoRecords(true);
        getCategoryList();
    }

    CategoryListAdapter mAdapter;

    private void showNoRecords(boolean show) {
        if (show) {
            noRecordsText.setVisibility(View.VISIBLE);
            orderList.setVisibility(View.GONE);
        } else {
            noRecordsText.setVisibility(View.GONE);
            orderList.setVisibility(View.VISIBLE);
        }
    }

    ArrayList<JSONObject> availableCouponCodeList = new ArrayList<>();
    public void getCategoryList() {
        try {
            String otp = new GlobalAppApis().getCategoryList();
            availableCouponCodeList.clear();
            ApiService client = API_Config.getApiClient_ByPost();
            Call<String> call = client.getCategoryList(otp);
            new ConnectToRetrofit(new RetrofitCallBackListenar() {
                @Override
                public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        JSONArray mArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < mArray.length(); i++) {
                            availableCouponCodeList.add(mArray.getJSONObject(i));
                        }
                        if (availableCouponCodeList.size() > 0) {
                            showNoRecords(false);
                            mAdapter.notifyDataSetChanged();
                        } else {
                            showNoRecords(true);
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
    public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.DateHolder> {
        ArrayList<JSONObject> menuItems = new ArrayList<>();
        public CategoryListAdapter(ArrayList<JSONObject> menuItems) {
            this.menuItems = menuItems;
        }
        @Override
        public CategoryListAdapter.DateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new CategoryListAdapter.DateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, null));
        }

        @Override
        public void onBindViewHolder(CategoryListAdapter.DateHolder holder, final int position) {
            try {
                holder.categoryName.setText(menuItems.get(position).getString("category_name"));
                Glide.with(mActivity).load(menuItems.get(position).getString("categoryImage"))
                        .transform(new CircleTransform(mActivity))
                        .into(holder.subCatPic);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent it = new Intent(mActivity, RestaurantList.class);
                        try {
                            it.putExtra("lat", Double.parseDouble(Constants.getSavedPreferences(mActivity, Constants.LATITUDE, "")));
                            it.putExtra("lon", Double.parseDouble(Constants.getSavedPreferences(mActivity, Constants.LONGITUDE, "")));
                            it.putExtra("id", menuItems.get(position).getString("id"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(it);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return menuItems.size();
        }

        class DateHolder extends RecyclerView.ViewHolder {
            TextView categoryName;
            ImageView subCatPic;
            public DateHolder(View itemView) {
                super(itemView);
                categoryName = itemView.findViewById(R.id.categoryName);
                subCatPic = itemView.findViewById(R.id.subCatPic);
            }
        }
    }
}


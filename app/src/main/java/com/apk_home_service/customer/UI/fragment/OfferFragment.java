package com.apk_home_service.customer.UI.fragment;


import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.HomeActivity;
import com.apk_home_service.customer.Wallet.API_Config;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.utill.CommonUtill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferFragment extends Fragment {
    Activity mActivity;
    private ImageView menuImg;

    public OfferFragment() {
        // Required empty public constructor
    }

    View mView;
    TextView noRecordsText;
    RecyclerView orderList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_offer, container, false);
        mActivity = getActivity();
        noRecordsText = mView.findViewById(R.id.noRecordsText);
        orderList = mView.findViewById(R.id.orderList);
        menuImg=mView.findViewById(R.id.menuImg);

        LinearLayoutManager manager = new LinearLayoutManager(mActivity);
        orderList.setLayoutManager(manager);
        mAdapter = new OfferListAdapter(availableCouponCodeList);
        orderList.setAdapter(mAdapter);
        showNoRecords(true);
        menuImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), HomeActivity.class);
                startActivity(intent);
            }
        });
//        getAvailableCoupons();
        return mView;
    }

    OfferListAdapter mAdapter;

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

    public void getAvailableCoupons() {
        try {
            availableCouponCodeList.clear();
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

    public class OfferListAdapter extends RecyclerView.Adapter<OfferListAdapter.DateHolder> {


        ArrayList<JSONObject> menuItems = new ArrayList<>();

        public OfferListAdapter(ArrayList<JSONObject> menuItems) {
            this.menuItems = menuItems;
        }

        @Override
        public OfferListAdapter.DateHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new OfferListAdapter.DateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_list_item, null));
        }

        @Override
        public void onBindViewHolder(OfferListAdapter.DateHolder holder, final int position) {
            try {
                holder.couponName.setText(menuItems.get(position).getString("coupan_key"));
                holder.couponDesc.setText(menuItems.get(position).getString("coupan_title"));
          holder.couponName.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View view) {
                  try {
                      CommonUtill.copyToClipboard(getActivity(),menuItems.get(position).getString("coupan_key"));
                  } catch (JSONException e) {
                          e.printStackTrace();
                  }
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

            TextView couponName, couponDesc;

            public DateHolder(View itemView) {
                super(itemView);
                couponName = (TextView) itemView.findViewById(R.id.couponName);
                couponDesc = (TextView) itemView.findViewById(R.id.couponDesc);
            }
        }
    }
}

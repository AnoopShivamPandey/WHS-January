package com.apk_home_service.customer.UI.fragment;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apk_home_service.AdapterForCategory;
import com.apk_home_service.Category_data_cat;
import com.apk_home_service.customer.UI.BottomNavigationActivity;
import com.apk_home_service.customer.UI.HomeActivity;
import com.bumptech.glide.Glide;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.RestaurantList;
import com.apk_home_service.customer.Wallet.API_Config;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.utill.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static com.apk_home_service.customer.Wallet.Urls.BASE_URL;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryFragment extends Fragment {
    View mView;
    TextView noRecordsText;
    RecyclerView orderList;
    Activity mActivity;
    private ImageView img;
    RecyclerView rv_productCategory;

    List<Category_data_cat> list_Category = new ArrayList<>();


    public CategoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mView = inflater.inflate(R.layout.fragment_category, container, false);
        mActivity = getActivity();
        noRecordsText = mView.findViewById(R.id.noRecordsText);
        rv_productCategory = (RecyclerView) mView.findViewById(R.id.orderList);

        orderList = mView.findViewById(R.id.orderList);
        img=mView.findViewById(R.id.img);
       img.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(mActivity, BottomNavigationActivity.class);
               startActivity(intent);
           }
       });

       getCategoryData();
        return mView;
    }

    void getCategoryData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"customer/category", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getBoolean("status")) {
                        String jsonInString = jsonObject.getString("data");
                        list_Category = Category_data_cat.createJsonInList(jsonInString);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
                        AdapterForCategory myAdapter = new AdapterForCategory(getActivity(), list_Category);
                        rv_productCategory.setLayoutManager(gridLayoutManager);
                        rv_productCategory.setAdapter(myAdapter);
                        rv_productCategory.setHasFixedSize(true);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

}

package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.controll.FoodSubCatAdapter;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.FoodCategory;
import com.apk_home_service.customer.modal.FoodSubCategory;
import com.apk_home_service.customer.modal.RequestModal;
import com.apk_home_service.customer.network.FPModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OfferSubcategoryActivity extends Activity {

    FoodCategory foodCategory;
    ProgressDialog pDialog;
    LinearLayout noRecordBg;
    RecyclerView subCatList;
    ArrayList<FoodSubCategory> foodArrayList = new ArrayList<>();
    FoodSubCatAdapter foodAdapter;

    FoodSubCatAdapter.FoodSubCatAdapterListener foodSubCatAdapterListener = new FoodSubCatAdapter.FoodSubCatAdapterListener() {
        @Override
        public void onSelect(FoodSubCategory foodMenuItem, int index) {
            Intent intent = new Intent();
            intent.putExtra("subCat", foodMenuItem);
            intent.putExtra("Cat", foodCategory);
            intent.putExtra("is_festive", foodCategory.isIs_promotional());
            setResult(RESULT_OK, intent);
            finish();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_subcategory);
        pDialog = CommonUtill.showProgressDialog(this, "Please wait...");
        foodCategory = (FoodCategory) getIntent().getSerializableExtra("foodCat");
        CommonUtill.printClassData(foodCategory);


        noRecordBg = (LinearLayout) findViewById(R.id.noRecordBg);

        subCatList = (RecyclerView) findViewById(R.id.subCatList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        subCatList.setLayoutManager(linearLayoutManager);
        foodAdapter = new FoodSubCatAdapter(this, foodArrayList);
        foodAdapter.setAdapterListener(foodSubCatAdapterListener);
        subCatList.setAdapter(foodAdapter);

        if (foodCategory.isIs_promotional().equalsIgnoreCase("yes")) {
            getOrderDetail();
        }


    }

    private void getOrderDetail() {
        RequestModal requestModal = new RequestModal();
        requestModal.setCategory_id(foodCategory.getId());

        CommonUtill.printClassData(requestModal);

        pDialog.show();
        new FPModel(OfferSubcategoryActivity.this, FPModel.CRAZYBILLA_API).fpApi.getCategoryDetail(PrefrenceHandler.getPreferences(OfferSubcategoryActivity.this).getAPI_TOKEN(), requestModal, new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                pDialog.dismiss();
                CommonUtill.print("getOrderDetail result ==" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {

                       Gson gson = new Gson();
                        JsonElement element = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("subcategories").toString(), JsonElement.class);

                        Type listType = new TypeToken<ArrayList<FoodSubCategory>>() {
                        }.getType();

                        foodArrayList = gson.fromJson(element, listType);
                        foodAdapter.refreshList(foodArrayList);
//
                        subCatList.setVisibility(View.VISIBLE);
                        noRecordBg.setVisibility(View.GONE);
//                        CommonUtill.showTost(getActivity(), jsonObject.getString("message"));
                    } else {
                        if (jsonObject.getString("status_code").equalsIgnoreCase("401")) {
                            PrefrenceHandler.getPreferences(OfferSubcategoryActivity.this).setLogout();
                            startActivity(new Intent(OfferSubcategoryActivity.this, MobileActivity.class));
                          finish();
                        }
//                        CommonUtill.showTost(getActivity(), jsonObject.getString("message"));
                        subCatList.setVisibility(View.GONE);
                        noRecordBg.setVisibility(View.VISIBLE);
//                        showMessage(jsonObject.getString("message"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();
                CommonUtill.print("getOrderDetail error ==" + error.getMessage());
            }
        });
    }


}

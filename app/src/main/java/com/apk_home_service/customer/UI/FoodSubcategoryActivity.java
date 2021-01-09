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

public class FoodSubcategoryActivity extends Activity {

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
            intent.putExtra("subCatId", foodMenuItem.getId());
            intent.putExtra("CatId", foodCategory.getId());
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
        getSubCategoryList();
    }


    private void getSubCategoryList() {

        RequestModal requestModal = new RequestModal();
        requestModal.setCategory_id(foodCategory.getId());
        requestModal.setType("array");

        CommonUtill.printClassData(requestModal);

        pDialog.show();
        new FPModel(this, FPModel.CRAZYBILLA_API).fpApi.getFoodSubCategory(PrefrenceHandler.getPreferences(this).getAPI_TOKEN(), requestModal, new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                pDialog.dismiss();
                CommonUtill.print("getSubCategoryList result ==" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        Gson gson = new Gson();
                        JsonElement element = gson.fromJson(jsonObject.getJSONArray("data").toString(), JsonElement.class);

                        Type listType = new TypeToken<ArrayList<FoodSubCategory>>() {
                        }.getType();

                        foodArrayList = gson.fromJson(element, listType);
                        foodAdapter.refreshList(foodArrayList);
//
                        subCatList.setVisibility(View.VISIBLE);
                        noRecordBg.setVisibility(View.GONE);
//
                        CommonUtill.showTost(FoodSubcategoryActivity.this, jsonObject.getString("message"));
                    } else {
                        if (jsonObject.getString("status_code").equalsIgnoreCase("401")) {
                            PrefrenceHandler.getPreferences(FoodSubcategoryActivity.this).setLogout();
                            startActivity(new Intent(FoodSubcategoryActivity.this, MobileActivity.class));
                            finish();
                        }
                        subCatList.setVisibility(View.GONE);
                        noRecordBg.setVisibility(View.VISIBLE);
                        CommonUtill.showTost(FoodSubcategoryActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    subCatList.setVisibility(View.GONE);
                    noRecordBg.setVisibility(View.VISIBLE);
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();
                subCatList.setVisibility(View.GONE);
                noRecordBg.setVisibility(View.VISIBLE);
                CommonUtill.print("getSubCategoryList error ==" + error.getMessage());
            }
        });
    }


}

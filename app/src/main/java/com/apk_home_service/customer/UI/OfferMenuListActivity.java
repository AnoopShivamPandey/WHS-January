package com.apk_home_service.customer.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.controll.FoodMenuAdapter;
import com.apk_home_service.customer.controll.OfferCatAdapter;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.CreateOrder;
import com.apk_home_service.customer.modal.FoodCategory;
import com.apk_home_service.customer.modal.FoodMenuItem;
import com.apk_home_service.customer.modal.FoodSubCategory;
import com.apk_home_service.customer.modal.RequestModal;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.network.FPModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class OfferMenuListActivity extends AppCompatActivity {

    final int SELECT_QUANTITY = 100;
    final int SELECT_ADDRESS = 110;
    //    final int SUB_CAT_REQUEST = 105;
    TextView amntTxt, qntyTxt;
    //    ImageView subCatPic;
    ProgressDialog pDialog;
    FoodCategory foodCategory;
    RecyclerView menuList, subCatList;
    ArrayList<FoodMenuItem> foodArrayList = new ArrayList<>();
    HashMap<String, CreateOrder.Product> selectedFoodMap = new HashMap<>();
    FoodMenuAdapter foodAdapter;
    float totalPrice = 0;
    UserData userData;
    ArrayList<FoodSubCategory> foodSubCatArrayList = new ArrayList<>();
    OfferCatAdapter offerAdapter;

    Handler itemCalculationHandler;

    Runnable calculatePrice = new Runnable() {
        @Override
        public void run() {
            float temp = 0;

            Set<String> set = selectedFoodMap.keySet();
            Iterator<String> iterator = set.iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                temp = temp + (Integer.parseInt(selectedFoodMap.get(key).product_quantity) * Float.parseFloat(selectedFoodMap.get(key).product_price));
            }


            totalPrice = temp;
            CommonUtill.print("totalPrice ==" + totalPrice);
//            amntTxt.setText(totalPrice + "");
            amntTxt.setText(String.format(Locale.US, "%.2f", totalPrice));
            qntyTxt.setText(selectedFoodMap.size() + "");
        }
    };


    FoodMenuAdapter.FoodMenuAdapterListener foodMenuAdapterListener = new FoodMenuAdapter.FoodMenuAdapterListener() {
        @Override
        public void onSelect(FoodMenuItem foodMenuItem, int index) {
            FoodMenuItem temItem = foodMenuItem;
            if (foodMenuItem.isSelect()) {
                temItem.setSelect(false);
                selectedFoodMap.remove(temItem.getProduct_id());
            } else {
                temItem.setSelect(true);
                CreateOrder.Product product = new CreateOrder.Product(temItem.getProduct_id(),
                        temItem.getName(),temItem.getPrice(), temItem.getUnit(), (temItem.getQnty() + ""));
                selectedFoodMap.put(temItem.getProduct_id(), product);
            }
            foodArrayList.set(index, temItem);
            foodAdapter.refreshList(foodArrayList, index);

            itemCalculationHandler.post(calculatePrice);

        }

        @Override
        public void onQuantityChange(FoodMenuItem foodMenuItem, int index, int qnty) {
            Intent intent = new Intent(OfferMenuListActivity.this, QuantitySelectionActivity.class);
            intent.putExtra("qnty", qnty);
            intent.putExtra("index", index);
            intent.putExtra("foodMenuItem", foodMenuItem);
            startActivityForResult(intent, SELECT_QUANTITY);
        }
    };

    OfferCatAdapter.FoodSubCatAdapterListener foodSubCatAdapterListener = new OfferCatAdapter.FoodSubCatAdapterListener() {
        @Override
        public void onSelect(FoodSubCategory foodMenuItem, int index) {
            getOfferFoodListList(foodMenuItem);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_menu_list);
        foodCategory = (FoodCategory) getIntent().getSerializableExtra("foodCat");
        pDialog = CommonUtill.showProgressDialog(this, "Please wait...");
        itemCalculationHandler = new Handler();

        try {
            userData = (UserData) PrefrenceHandler.getPreferences(OfferMenuListActivity.this).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        amntTxt = (TextView) findViewById(R.id.amntTxt);
        qntyTxt = (TextView) findViewById(R.id.qntyTxt);
//        subCatTxt = (TextView) findViewById(R.id.subCatTxt);
//        subCatPic = (ImageView) findViewById(R.id.subCatPic);
        amntTxt.setText(String.format("%.2f",totalPrice) + "");


        subCatList = (RecyclerView) findViewById(R.id.subCatList);
        LinearLayoutManager HlinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        subCatList.setLayoutManager(HlinearLayoutManager);
        offerAdapter = new OfferCatAdapter(this, foodSubCatArrayList);
        offerAdapter.setAdapterListener(foodSubCatAdapterListener);
        subCatList.setAdapter(offerAdapter);

        menuList = (RecyclerView) findViewById(R.id.menuList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        menuList.setLayoutManager(linearLayoutManager);
        foodAdapter = new FoodMenuAdapter(this, foodArrayList);
        foodAdapter.setAdapterListener(foodMenuAdapterListener);
        menuList.setAdapter(foodAdapter);


        findViewById(R.id.orderTxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFoodMap.size() > 0) {

                    Intent intent = new Intent(OfferMenuListActivity.this, ChangeOrderAddressActivity.class);
                    startActivityForResult(intent, SELECT_ADDRESS);

//                    createOrder();
//                    CommonUtill.showTost(TrollyMenuListActivity.this, "Coming soon.");
                } else {
                    CommonUtill.showTost(OfferMenuListActivity.this, "Please select food item.");
                }
            }
        });

//        findViewById(R.id.clickTxt).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(OfferMenuListActivity.this, OfferSubcategoryActivity.class);
//                intent.putExtra("foodCat", foodCategory);
//                startActivityForResult(intent, SUB_CAT_REQUEST);
//            }
//        });


        getOrderDetail();

    }


    private void getOrderDetail() {
        RequestModal requestModal = new RequestModal();
        requestModal.setCategory_id(foodCategory.getId());

        CommonUtill.printClassData(requestModal);

        pDialog.show();
        new FPModel(OfferMenuListActivity.this, FPModel.CRAZYBILLA_API).fpApi.getCategoryDetail(PrefrenceHandler.getPreferences(OfferMenuListActivity.this).getAPI_TOKEN(), requestModal, new Callback<String>() {
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

                        foodSubCatArrayList = gson.fromJson(element, listType);
                        offerAdapter.refreshList(foodSubCatArrayList);

                        getOfferFoodListList(foodSubCatArrayList.get(0));

//                        CommonUtill.showTost(getActivity(), jsonObject.getString("message"));
                    } else {
                        if (jsonObject.getString("status_code").equalsIgnoreCase("401")) {
                            PrefrenceHandler.getPreferences(OfferMenuListActivity.this).setLogout();
                            startActivity(new Intent(OfferMenuListActivity.this, MobileActivity.class));
                            finish();
                        }
//                        CommonUtill.showTost(getActivity(), jsonObject.getString("message"));
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

    private void getOfferFoodListList(final FoodSubCategory temItem) {
        RequestModal requestModal = new RequestModal();
        requestModal.setCategory_id(foodCategory.getId());
        requestModal.setSubcategory_id(temItem.getId());

        CommonUtill.printClassData(requestModal);

        pDialog.show();
        new FPModel(this, FPModel.CRAZYBILLA_API).fpApi.getFestiveProduct(PrefrenceHandler.getPreferences(this).getAPI_TOKEN(), requestModal, new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                pDialog.dismiss();
                CommonUtill.print("getOfferFoodListList result ==" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {

                        Gson gson = new Gson();
                        JsonElement element = gson.fromJson(jsonObject.getJSONArray("data").getJSONObject(0).getJSONArray("variants").toString(), JsonElement.class);

                        Type listType = new TypeToken<ArrayList<FoodMenuItem>>() {
                        }.getType();

                        foodArrayList = gson.fromJson(element, listType);
                        for (int i = 0; i < foodArrayList.size(); i++) {
                            if (selectedFoodMap.containsKey(foodArrayList.get(i).getProduct_id())) {
                                foodArrayList.get(i).setSelect(true);
                                foodArrayList.get(i).setQnty(Integer.parseInt(selectedFoodMap.get(foodArrayList.get(i).getProduct_id()).product_quantity));
                            } else {
                                foodArrayList.get(i).setSelect(false);
                                foodArrayList.get(i).setQnty(0);
                            }
                        }
                        foodAdapter.refreshList(foodArrayList);
                    } else {
                        if (jsonObject.getString("status_code").equalsIgnoreCase("401")) {
                            PrefrenceHandler.getPreferences(OfferMenuListActivity.this).setLogout();
                            startActivity(new Intent(OfferMenuListActivity.this, MobileActivity.class));
                            finish();
                        }
                        CommonUtill.showTost(OfferMenuListActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();
                CommonUtill.print("getOfferFoodListList error ==" + error.getMessage());
            }
        });

    }


    private void createOrder(final Intent data) {

        CreateOrder requestModal = new CreateOrder(userData.getUserId(),"-1", (totalPrice + ""), new ArrayList<CreateOrder.Product>(selectedFoodMap.values()));
        requestModal.setLatitude(getIntent().getDoubleExtra("mapLat", 0) + "");
        requestModal.setLongitude(getIntent().getDoubleExtra("mapLong", 0) + "");
        requestModal.setName(data.getStringExtra("name"));
        requestModal.setPin_code(data.getStringExtra("pin"));
        requestModal.setCountry(data.getStringExtra("country"));
        requestModal.setState(data.getStringExtra("state"));
        requestModal.setCity(data.getStringExtra("city"));
        requestModal.setStreet(data.getStringExtra("street"));
        requestModal.setMobile(data.getStringExtra("mobile"));
        requestModal.setEmail(data.getStringExtra("email"));


        CommonUtill.printClassData(requestModal);

        pDialog.show();
        new FPModel(this, FPModel.CRAZYBILLA_API).fpApi.createOrder(PrefrenceHandler.getPreferences(this).getAPI_TOKEN(), requestModal, new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                pDialog.dismiss();
                CommonUtill.print("createOrder result ==" + result);

//                {"message":"Order has been created successfully","status":true,"data":[{"id":"3","order_id":"BILLA13-153916878020","stall_id":"13","customer_id":"20","address_id":"1","total_price":"450","status":"new","gst":"0","order_time":"04:23 PM","delivery_time":"04:23 PM"}],"error_code":"","status_code":"200"}

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        JSONObject dataJson = jsonObject.getJSONObject("data");

                        Intent intent = new Intent(OfferMenuListActivity.this, OrderPreviewActivity.class);
                        intent.putExtra("isFestive", foodCategory.isIs_promotional());
                        intent.putExtra("orderId", dataJson.getString("order_id"));
                        intent.putExtra("Id", dataJson.getString("id"));
                        intent.putExtra("address", data.getStringExtra("address"));
                        intent.putExtra("amount", totalPrice);
                        intent.putExtra("gst", dataJson.getString("gst"));
                        intent.putExtra("cgst", dataJson.getString("cgst"));
                        intent.putExtra("mapLat", getIntent().getDoubleExtra("mapLat", 0));
                        intent.putExtra("mapLong", getIntent().getDoubleExtra("mapLong", 0));

                        startActivity(intent);
                        finish();

                        CommonUtill.showTost(OfferMenuListActivity.this, jsonObject.getString("message"));
                    } else {
                        CommonUtill.showTost(OfferMenuListActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();
                CommonUtill.print("createOrder error ==" + error.getMessage());
            }
        });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_QUANTITY && resultCode == RESULT_OK && data != null) {
            FoodMenuItem temItem = (FoodMenuItem) data.getSerializableExtra("foodMenuItem");
            int index = data.getIntExtra("index", 0);
            foodArrayList.set(index, temItem);
            foodAdapter.refreshList(foodArrayList, index);

            CreateOrder.Product product = new CreateOrder.Product(temItem.getProduct_id(),
                    temItem.getName(),temItem.getPrice(), temItem.getUnit(), (temItem.getQnty() + ""));

            selectedFoodMap.put(temItem.getProduct_id(), product);

            itemCalculationHandler.post(calculatePrice);
        }else if (requestCode == SELECT_ADDRESS && resultCode == RESULT_OK && data != null) {
            CommonUtill.print("SELECT_ADDRESS ==" + data.getStringExtra("address"));
//            addTxt.setText(data.getStringExtra("address"));

            createOrder(data);


        }
//
    }
}

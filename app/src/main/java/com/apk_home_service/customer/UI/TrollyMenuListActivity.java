package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.siyamed.shapeimageview.CircularImageView;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.custom.MessageDialog;
import com.apk_home_service.customer.Wallet.API_Config;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.controll.CircleTransform;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.CreateOrder;
import com.apk_home_service.customer.modal.FoodItemList;
import com.apk_home_service.customer.modal.FoodMenuItem;
import com.apk_home_service.customer.modal.RequestModal;
import com.apk_home_service.customer.modal.TrollyModel;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.network.FPModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.apk_home_service.customer.utill.Constants;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;
import com.travijuu.numberpicker.library.NumberPicker;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

public class TrollyMenuListActivity extends AppCompatActivity {

    final int SELECT_QUANTITY = 100;
    TextView trollyName, amntTxt, selectCount;
    CircularImageView trollyImg;
    ProgressDialog pDialog;
    TrollyModel trolly;
    RecyclerView menuList;
    ArrayList<FoodItemList> foodArrayList = new ArrayList<>();
    ArrayList<CreateOrder.Product> selectedFoodArrayList = new ArrayList<>();
    MyAdapter foodAdapter;
    float totalPrice = 0;
    private Intent intents;
    String res_id,res_img,res_name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trolly_menu_list);

        pDialog = CommonUtill.showProgressDialog(this, "Please wait...");
        getMinimumValue();

        try {
            userData = (UserData) PrefrenceHandler.getPreferences(TrollyMenuListActivity.this).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        trollyImg = findViewById(R.id.trollyImg);
        trollyName = (TextView) findViewById(R.id.trollyName);
        amntTxt = (TextView) findViewById(R.id.amntTxt);
        selectCount = (TextView) findViewById(R.id.selectCount);

//        amntTxt.setText(totalPrice + "");

        amntTxt.setText(String.format(Locale.US, "%.2f", totalPrice));

        menuList = (RecyclerView) findViewById(R.id.menuList);
        foodAdapter = new MyAdapter(TrollyMenuListActivity.this, foodArrayList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        menuList.setLayoutManager(linearLayoutManager);
//        foodAdapter = new MyAdapter(this, foodArrayList);
        menuList.setAdapter(foodAdapter);

        intents = getIntent();
        res_id=intents.getStringExtra("rest_id");
        res_name=intents.getStringExtra("rest_name");
        res_img=intents.getStringExtra("rest_img");

        trollyName.setText(res_name);
            Glide.with(this)
                    .load(res_img)
                    .error(R.drawable.logo)
                    .crossFade()
                    .centerCrop()
                    .transform(new CircleTransform(this))
                    .into(trollyImg);


        findViewById(R.id.orderTxt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (foodAdapter.isFoodSelected()) {
                    try {
                        double value = Double.parseDouble(amntTxt.getText().toString().replace(getResources().getString(R.string.rupee), ""));
                        if (value < minValue) {
                            Toast.makeText(TrollyMenuListActivity.this, "Order cannot be less than " + getResources().getString(R.string.rupee) + minValue, Toast.LENGTH_SHORT).show();
                        } else {
                            createOrder();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        createOrder();
                    }
//                    new MessageDialog(TrollyMenuListActivity.this, confirmationDialogListener, MessageDialog.confirmTask.NONE,
//                            "We will update you soon for our new direct to home food service in your city.", getResources().getString(R.string.app_name), "OK", "OK", true, false);
//                    CommonUtill.showTost(TrollyMenuListActivity.this, "Coming soon.");
                } else

                {
                    CommonUtill.showTost(TrollyMenuListActivity.this, "Please select data.");
                }
            }
        });

        getMenuListing();


    }

    String resultJSON = "";
    double minValue = 99;

    void getMinimumValue() {
        try {
            JSONObject mObject = new JSONObject(com.apk_home_service.customer.data.Constants.getGeneralSettings(this));
            minValue = Double.parseDouble(mObject.getJSONObject("data").getJSONObject("order_setting").getString("minimum_order"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void getMenuListing() {
        try {
          String otp = new GlobalAppApis().nearbyRestaurants(res_id);
        //    String otp = new GlobalAppApis().nearbyRestaurants(trolly.getId());
            ApiService client = API_Config.getApiClient_ByPost();
            Call<String> call = client.getDishList(otp);
            new ConnectToRetrofit(new RetrofitCallBackListenar() {
                @Override
                public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        resultJSON = result;
                        if (jsonObject.getBoolean("status")) {
                            Gson gson = new Gson();
                            JsonElement element = gson.fromJson(jsonObject.getJSONObject("data").getJSONArray("Dishes").toString(), JsonElement.class);

                            Type listType = new TypeToken<ArrayList<FoodItemList>>() {
                            }.getType();

                            foodArrayList = gson.fromJson(element, listType);
                            foodAdapter.notifyDataSetChanged(foodArrayList);
//
//
//                        if (!TextUtils.isEmpty(jsonObject.getString("message"))) {
//                            CommonUtill.showTost(TrollyMenuListActivity.this, jsonObject.getString("message"));
//                        }
                        } else {
                            if (jsonObject.getString("status_code").equalsIgnoreCase("401")) {
                                PrefrenceHandler.getPreferences(TrollyMenuListActivity.this).setLogout();
                                startActivity(new Intent(TrollyMenuListActivity.this, MobileActivity.class));
                                finish();
                            }

                            new MessageDialog(TrollyMenuListActivity.this, new MessageDialog.ConfirmationDialogListener() {
                                @Override
                                public void onYesButtonClicked(MessageDialog.confirmTask task) {
                                    TrollyMenuListActivity.this.finish();
                                }

                                @Override
                                public void onNoButtonClicked(MessageDialog.confirmTask task) {

                                }
                            }, MessageDialog.confirmTask.NONE,
                                    jsonObject.getString("message"), TrollyMenuListActivity.this.getResources().getString(R.string.app_name), "No", "OK", false, true);


//                        CommonUtill.showTost(TrollyMenuListActivity.this, jsonObject.getString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }, TrollyMenuListActivity.this, call, "", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getFoodListList() {
        RequestModal requestModal = new RequestModal();
        requestModal.setTrolly_id(trolly.getId());

        CommonUtill.printClassData(requestModal);

        pDialog.show();
        new FPModel(this, FPModel.CRAZYBILLA_API).fpApi.getTrollyFoodList(requestModal, new Callback<String>() {
            @Override
            public void success(String result, Response response) {
                pDialog.dismiss();
                CommonUtill.print("getFoodListList result ==" + result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        Gson gson = new Gson();
                        JsonElement element = gson.fromJson(jsonObject.getJSONArray("data").toString(), JsonElement.class);

                        Type listType = new TypeToken<ArrayList<FoodItemList>>() {
                        }.getType();

                        foodArrayList = gson.fromJson(element, listType);
//
//
//                        if (!TextUtils.isEmpty(jsonObject.getString("message"))) {
//                            CommonUtill.showTost(TrollyMenuListActivity.this, jsonObject.getString("message"));
//                        }
                    } else {
                        if (jsonObject.getString("status_code").equalsIgnoreCase("401")) {
                            PrefrenceHandler.getPreferences(TrollyMenuListActivity.this).setLogout();
                            startActivity(new Intent(TrollyMenuListActivity.this, MobileActivity.class));
                            finish();
                        }

                        new MessageDialog(TrollyMenuListActivity.this, new MessageDialog.ConfirmationDialogListener() {
                            @Override
                            public void onYesButtonClicked(MessageDialog.confirmTask task) {
                                TrollyMenuListActivity.this.finish();
                            }

                            @Override
                            public void onNoButtonClicked(MessageDialog.confirmTask task) {

                            }
                        }, MessageDialog.confirmTask.NONE,
                                jsonObject.getString("message"), TrollyMenuListActivity.this.getResources().getString(R.string.app_name), "No", "OK", false, true);


//                        CommonUtill.showTost(TrollyMenuListActivity.this, jsonObject.getString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                pDialog.dismiss();
                CommonUtill.print("getFoodListList error ==" + error.getMessage());
            }
        });

    }


    private void getOfferFoodListList() {
        RequestModal requestModal = new RequestModal();
        requestModal.setCategory_id(getIntent().getStringExtra("CatId"));
        requestModal.setSubcategory_id(getIntent().getStringExtra("subCatId"));

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

//                        if (!TextUtils.isEmpty(jsonObject.getString("message"))) {
//                            CommonUtill.showTost(TrollyMenuListActivity.this, jsonObject.getString("message"));
//                        }
                    } else {
                        if (jsonObject.getString("status_code").equalsIgnoreCase("401")) {
                            PrefrenceHandler.getPreferences(TrollyMenuListActivity.this).setLogout();
                            startActivity(new Intent(TrollyMenuListActivity.this, MobileActivity.class));
                            finish();
                        }
                        CommonUtill.showTost(TrollyMenuListActivity.this, jsonObject.getString("message"));
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

    UserData userData;


//    JSONObject createRequestJSON() {
//        try {
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void createOrder() {

        if (Constants.isUserLoggedIn(this)) {
            Intent intent = new Intent(TrollyMenuListActivity.this, OrderPreviewActivity.class);
            intent.putExtra("rest_id", res_id);
            intent.putExtra("rest_name", res_name);
            intent.putExtra("rest_img", res_img);
            intent.putExtra("resultJSON", resultJSON);
            intent.putExtra("mapLat", getIntent().getDoubleExtra("mapLat", 0));
            intent.putExtra("mapLong", getIntent().getDoubleExtra("mapLong", 0));
            intent.putExtra("foodArrayList", foodArrayList);
            startActivity(intent);
        } else {
            Intent intent = new Intent(TrollyMenuListActivity.this, MobileActivity.class);
            intent.putExtra("trolly", res_id);
            intent.putExtra("resultJSON", resultJSON);
            intent.putExtra("mapLat", getIntent().getDoubleExtra("mapLat", 0));
            intent.putExtra("mapLong", getIntent().getDoubleExtra("mapLong", 0));
            intent.putExtra("foodArrayList", foodArrayList);
            startActivity(intent);
        }
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        Activity context;
        ArrayList<FoodItemList> foodArrayList = new ArrayList<>();

        public MyAdapter(Activity context, ArrayList<FoodItemList> foodArrayList) {
            this.context = context;
            this.foodArrayList = foodArrayList;
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new DateHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_list_item_, null));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//            (DateHolder) holder).onBind(position);
            DateHolder mHolder = (DateHolder) holder;
            FoodItemList mItem = foodArrayList.get(position);
            mHolder.itemTxt.setText(mItem.getDishName());
            mHolder.des.setText(mItem.getDes());
            mHolder.priceTxt.setText(getResources().getString(R.string.rupee) + mItem.getPrice());
        }

        @Override
        public int getItemCount() {
            return foodArrayList.size();
        }

        public void notifyDataSetChanged(ArrayList<FoodItemList> foodArrayList) {
            this.foodArrayList = foodArrayList;
            System.out.println("==foodArrayList Size " + foodArrayList.size());
            notifyDataSetChanged();
        }

        void calculateBill() {
            try {
                double totalBill = 0;
                for (int i = 0; i < foodArrayList.size(); i++) {
                    FoodItemList mItem = foodArrayList.get(i);
                    System.out.println(totalBill + "==" + mItem.getDishName() + "\t" + mItem.getQuantity() + "\t" + mItem.getIsSelected());
                    totalBill = totalBill + Double.parseDouble(mItem.getPrice()) * mItem.getQuantity();
                }
                amntTxt.setText(getResources().getString(R.string.rupee) + String.format("%.2f", totalBill));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        boolean isFoodSelected() {
            for (int i = 0; i < foodArrayList.size(); i++) {
                FoodItemList mItem = foodArrayList.get(i);
                if (mItem.getIsSelected()) {
                    return true;
                }
            }
            return false;
        }

        class DateHolder extends RecyclerView.ViewHolder {

            TextView itemTxt, priceTxt, qntyTxt,des;
            RelativeLayout qntyBg;

            public DateHolder(View itemView) {


                super(itemView);
                des=(TextView)itemView.findViewById(R.id.des);
                itemTxt = (TextView) itemView.findViewById(R.id.itemTxt);
                priceTxt = (TextView) itemView.findViewById(R.id.priceTxt);
                qntyTxt = (TextView) itemView.findViewById(R.id.qntyTxt);
                qntyBg = (RelativeLayout) itemView.findViewById(R.id.qntyBg);
                NumberPicker numberPicker = (NumberPicker) itemView.findViewById(R.id.number_picker);
                numberPicker.setActionEnabled(ActionEnum.INCREMENT, true);
                numberPicker.setActionEnabled(ActionEnum.DECREMENT, true);
                numberPicker.setMax(25);
                numberPicker.setMin(0);
                numberPicker.setUnit(1);
                numberPicker.setValue(0);
                numberPicker.setValueChangedListener(new ValueChangedListener() {
                    @Override
                    public void valueChanged(int value, ActionEnum action) {
                        FoodItemList mItem = foodArrayList.get(getAdapterPosition());
                        if (value > 0) {
                            mItem.setIsSelected(true);
                            mItem.setQuantity(value);
                        } else {
                            mItem.setIsSelected(false);
                            mItem.setQuantity(0);

                        }
                        calculateBill();
                    }
                });
            }


        }
    }
}

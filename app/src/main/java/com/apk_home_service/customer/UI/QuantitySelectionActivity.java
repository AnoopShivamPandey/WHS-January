package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.apk_home_service.customer.R;
import com.apk_home_service.customer.controll.QuantityAdapter;
import com.apk_home_service.customer.modal.FoodMenuItem;

public class QuantitySelectionActivity extends Activity {

    RecyclerView quantList;
    QuantityAdapter foodAdapter;
    FoodMenuItem foodMenuItem;

    QuantityAdapter.QuantityAdapterListener quantityAdapterListener = new QuantityAdapter.QuantityAdapterListener() {
        @Override
        public void onSelect(int qnty) {
            foodMenuItem.setQnty(qnty);
            Intent intent = new Intent();
            intent.putExtra("foodMenuItem", foodMenuItem);
            intent.putExtra("index", getIntent().getIntExtra("index", 0));
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quantity_selection);

        foodMenuItem = (FoodMenuItem) getIntent().getSerializableExtra("foodMenuItem");

        quantList = (RecyclerView) findViewById(R.id.quantList);
        GridLayoutManager linearLayoutManager = new GridLayoutManager(this, 5);
        quantList.setLayoutManager(linearLayoutManager);
        foodAdapter = new QuantityAdapter(this, (getIntent().getIntExtra("qnty", 2)));
        foodAdapter.setAdapterListener(quantityAdapterListener);
        quantList.setAdapter(foodAdapter);
    }
}

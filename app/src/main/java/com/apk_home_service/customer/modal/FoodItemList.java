package com.apk_home_service.customer.modal;

/**
 * Created by pc on 28-12-2018.
 */


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodItemList implements Serializable {
    @SerializedName("description")
    @Expose
    private String des;


    @SerializedName("restaurant_id")
    @Expose
    private String restaurantId;
    @SerializedName("dish_id")
    @Expose
    private String dishId;
    @SerializedName("dish_name")
    @Expose
    private String dishName;
    @SerializedName("dish_type")
    @Expose
    private String dishType;
    @SerializedName("sale_price")
    @Expose
    private String price;
    @SerializedName("price")
    @Expose
    private String salePrice;
    @SerializedName("discount_percent")
    @Expose
    private String discountPercent;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("dish_image")
    @Expose
    private String dishImage;

    boolean isSelected = false;
    int quantity = 0;

    public boolean getIsSelected() {
        return isSelected;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }


    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }




    public String getDishId() {
        return dishId;
    }

    public void setDishId(String dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public String getDishType() {
        return dishType;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    public String getDiscountPercent() {
        return discountPercent;
    }

    public void setDiscountPercent(String discountPercent) {
        this.discountPercent = discountPercent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDishImage() {
        return dishImage;
    }

    public void setDishImage(String dishImage) {
        this.dishImage = dishImage;
    }

}
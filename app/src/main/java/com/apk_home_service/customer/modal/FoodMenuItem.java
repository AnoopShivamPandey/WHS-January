package com.apk_home_service.customer.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FoodMenuItem implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("product_id")
    @Expose
    public String product_id;

    @SerializedName("stall_id")
    @Expose
    public String stall_id;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("description")
    @Expose
    public String description;

    @SerializedName("price")
    @Expose
    public String price;

    @SerializedName("discount_price")
    @Expose
    public String discount_price;

    @SerializedName("discount_percent")
    @Expose
    public String discount_percent;

    @SerializedName("unit")
    @Expose
    public String unit;

    @SerializedName("quantity")
    @Expose
    public String quantity;

    @SerializedName("availability")
    @Expose
    public String availability;


    @SerializedName("image")
    @Expose
    public String image;

    @SerializedName("isSelect")
    @Expose
    public boolean isSelect;

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getQnty() {
        return qnty;
    }

    public void setQnty(int qnty) {
        this.qnty = qnty;
    }

    @SerializedName("qnty")
    @Expose
    public int qnty;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStall_id() {
        return stall_id;
    }

    public void setStall_id(String stall_id) {
        this.stall_id = stall_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscount_price() {
        return discount_price;
    }

    public void setDiscount_price(String discount_price) {
        this.discount_price = discount_price;
    }

    public String getDiscount_percent() {
        return discount_percent;
    }

    public void setDiscount_percent(String discount_percent) {
        this.discount_percent = discount_percent;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    @SerializedName("created_at")
    @Expose
    public String created_at;


}
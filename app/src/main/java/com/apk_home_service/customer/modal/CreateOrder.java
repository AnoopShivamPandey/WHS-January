package com.apk_home_service.customer.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CreateOrder implements Serializable {

    @SerializedName("user_id")
    @Expose
    public String user_id;

    @SerializedName("name")
    @Expose
    public String name;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    @SerializedName("order_id")
    @Expose
    public String order_id;

    @SerializedName("pin_code")
    @Expose
    public String pin_code;

    @SerializedName("country")
    @Expose
    public String country;

    @SerializedName("state")
    @Expose
    public String state;

    @SerializedName("city")
    @Expose
    public String city;

    @SerializedName("street")
    @Expose
    public String street;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPin_code() {
        return pin_code;
    }

    public void setPin_code(String pin_code) {
        this.pin_code = pin_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("mobile")
    @Expose
    public String mobile;

    @SerializedName("email")
    @Expose
    public String email;

    @SerializedName("trolly_id")
    @Expose
    public String trolly_id;

    @SerializedName("latitude")
    @Expose
    public String latitude;

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @SerializedName("longitude")
    @Expose
    public String longitude;

    @SerializedName("price")
    @Expose
    public String price;

    public CreateOrder(String user_id, String trolly_id, String price, ArrayList<Product> products) {
        this.user_id = user_id;
        this.trolly_id = trolly_id;
        this.price = price;
        this.products = products;
    }

    public CreateOrder() {

    }

    @SerializedName("products")
    @Expose
    public ArrayList<Product> products;


    public static class Product {
        @SerializedName("product_id")
        @Expose
        public String product_id;

        @SerializedName("product_name")
        @Expose
        public String product_name;

        @SerializedName("product_price")
        @Expose
        public String product_price;

        @SerializedName("product_unit")
        @Expose
        public String product_unit;

        public Product(String product_id, String product_name, String product_price, String product_unit, String product_quantity) {
            this.product_id = product_id;
            this.product_name = product_name;
            this.product_price = product_price;
            this.product_unit = product_unit;
            this.product_quantity = product_quantity;
        }

        @SerializedName("product_quantity")
        @Expose
        public String product_quantity;
    }

}
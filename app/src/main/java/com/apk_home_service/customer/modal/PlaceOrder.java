package com.apk_home_service.customer.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PlaceOrder implements Serializable {

    @SerializedName("orderId")
    @Expose
    public String orderId;

    @SerializedName("userId")
    @Expose
    public String userId;

    @SerializedName("address")
    @Expose
    public String address;

    @SerializedName("paymentMode")
    @Expose
    public String paymentMode;

    public PlaceOrder(String orderId, String userId, String address, String paymentMode, String deliveryDate) {
        this.orderId = orderId;
        this.userId = userId;
        this.address = address;
        this.paymentMode = paymentMode;
        this.deliveryDate = deliveryDate;
    }

    @SerializedName("deliveryDate")
    @Expose
    public String deliveryDate;




}
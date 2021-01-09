package com.apk_home_service.customer.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderItem implements Serializable{

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("order_id")
    @Expose
    public String order_id;

    @SerializedName("stall_id")
    @Expose
    public String stall_id;

    @SerializedName("payment_mode")
    @Expose
    public String payment_mode;

    @SerializedName("order_time")
    @Expose
    public String order_time;

    @SerializedName("delivery_time")
    @Expose
    public String delivery_time;

    @SerializedName("total_price")
    @Expose
    public String total_price;

    @SerializedName("status")
    @Expose
    public String status;

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getStall_id() {
        return stall_id;
    }

    public void setStall_id(String stall_id) {
        this.stall_id = stall_id;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

    public String getOrder_time() {
        return order_time;
    }

    public void setOrder_time(String order_time) {
        this.order_time = order_time;
    }

    public String getDelivery_time() {
        return delivery_time;
    }

    public void setDelivery_time(String delivery_time) {
        this.delivery_time = delivery_time;
    }

    public String getTotal_price() {
        return total_price;
    }

    public void setTotal_price(String total_price) {
        this.total_price = total_price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public String getStall() {
//        return stall;
//    }
//
//    public void setStall(String stall) {
//        this.stall = stall;
//    }

//    @SerializedName("stall")
//    @Expose
//    public String stall;


}
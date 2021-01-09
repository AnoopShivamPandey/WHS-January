package com.apk_home_service.customer.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Rajnish on 9/2/2018.
 */

public class RequestModal {


    public Object getOrderId() {
        return orderId;
    }

    public void setOrderId(Object orderId) {
        this.orderId = orderId;
    }

    @SerializedName("orderId")
    @Expose
    public Object orderId;

    public Object getPage_no() {
        return page_no;
    }

    public void setPage_no(Object page_no) {
        this.page_no = page_no;
    }

    @SerializedName("page_no")
    @Expose
    public Object page_no;


    @SerializedName("mobile_no")
    @Expose
    public Object PhoneNo;

    public Object getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(Object phoneNo) {
        PhoneNo = phoneNo;
    }

    public Object getOTP() {
        return OTP;
    }

    public void setOTP(Object OTP) {
        this.OTP = OTP;
    }

    @SerializedName("otp_text")
    @Expose
    public Object OTP;


    @SerializedName("name")
    @Expose
    public Object name;
    @SerializedName("restaraunt_id")
    @Expose
    public Object restaraunt_id;
    @SerializedName("email")
    @Expose
    public Object email;
    @SerializedName("mobile")
    @Expose
    public Object mobile;
    @SerializedName("password")
    @Expose
    public Object password;
    @SerializedName("device_id")
    @Expose
    public Object device_id;
    @SerializedName("firebase_token")
    @Expose
    public Object firebase_token;

    @SerializedName("fcm_token")
    @Expose
    public String fcm_token;
    @SerializedName("city")
    @Expose
    public Object city;
    @SerializedName("state")
    @Expose
    public Object state;
    @SerializedName("country")
    @Expose
    public Object country;

    @SerializedName("street")
    @Expose
    public Object street;
    @SerializedName("pin_code")
    @Expose
    public Object pin_code;
    @SerializedName("latitude")
    @Expose
    public Object latitude;
    @SerializedName("longitude")
    @Expose
    public Object longitude;

    public Object getName() {
        return name;
    }

    public void setName(Object name) {
        this.name = name;
    }
 public Object getrestaraunt_id() {
        return restaraunt_id;
    }

    public void setrestaraunt_id(Object restaraunt_id) {
        this.restaraunt_id = restaraunt_id;
    }

    public Object getEmail() {
        return email;
    }

    public void setEmail(Object email) {
        this.email = email;
    }

    public Object getfcm_token() {
        return fcm_token;
    }

    public void setfcm_token(String fcm_token) {
        this.fcm_token = fcm_token;
    }

    public Object getMobile() {
        return mobile;
    }

    public void setMobile(Object mobile) {
        this.mobile = mobile;
    }

    public Object getPassword() {
        return password;
    }

    public void setPassword(Object password) {
        this.password = password;
    }

    public Object getDevice_id() {
        return device_id;
    }

    public void setDevice_id(Object device_id) {
        this.device_id = device_id;
    }

    public Object getFirebase_token() {
        return firebase_token;
    }

    public void setFirebase_token(Object firebase_token) {
        this.firebase_token = firebase_token;
    }

    public Object getCity() {
        return city;
    }

    public void setCity(Object city) {
        this.city = city;
    }

    public Object getState() {
        return state;
    }

    public void setState(Object state) {
        this.state = state;
    }

    public Object getCountry() {
        return country;
    }

    public void setCountry(Object country) {
        this.country = country;
    }

    public Object getStreet() {
        return street;
    }

    public void setStreet(Object street) {
        this.street = street;
    }

    public Object getPin_code() {
        return pin_code;
    }

    public void setPin_code(Object pin_code) {
        this.pin_code = pin_code;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongitude() {
        return longitude;
    }

    public void setLongitude(Object longitude) {
        this.longitude = longitude;
    }

    public Object getProfile_picture() {
        return profile_picture;
    }

    public void setProfile_picture(Object profile_picture) {
        this.profile_picture = profile_picture;
    }

    @SerializedName("profile_picture")
    @Expose
    public Object profile_picture;


    @SerializedName("category_id")
    @Expose
    public Object category_id;




    @SerializedName("subcategory_id")
    @Expose
    public Object subcategory_id;

    public Object getCategory_id() {
        return category_id;
    }

    public void setCategory_id(Object category_id) {
        this.category_id = category_id;
    }

    public Object getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(Object subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public Object getCollection_id() {
        return collection_id;
    }

    public void setCollection_id(Object collection_id) {
        this.collection_id = collection_id;
    }

    @SerializedName("collection_id")
    @Expose
    public Object collection_id;

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    @SerializedName("type")
    @Expose
    public Object type;

    public Object getTrolly_id() {
        return trolly_id;
    }

    public void setTrolly_id(Object trolly_id) {
        this.trolly_id = trolly_id;
    }

    @SerializedName("trolly_id")
    @Expose
    public Object trolly_id;

    public Object getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Object customerId) {
        this.customerId = customerId;
    }

    @SerializedName("customerId")
    @Expose
    public Object customerId;

    @SerializedName("user_id")
    @Expose
    public Object user_id;

    public Object getUser_id() {
        return user_id;
    }

    public void setUser_id(Object user_id) {
        this.user_id = user_id;
    }

    public Object getRating() {
        return rating;
    }

    public void setRating(Object rating) {
        this.rating = rating;
    }

    @SerializedName("rating")
    @Expose
    public Object rating;

}

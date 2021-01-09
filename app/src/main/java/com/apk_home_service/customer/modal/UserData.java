package com.apk_home_service.customer.modal;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserData implements Serializable {

    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("full_name")
    @Expose
    public String fullName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("mobile")
    @Expose
    public String mobile;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("device_id")
    @Expose
    public String deviceId;
    @SerializedName("api_token")
    @Expose
    public String apiToken;
    @SerializedName("fcm_token")
    @Expose
    public String fcmToken;
    @SerializedName("otp")
    @Expose
    public String otp;
    @SerializedName("otp_sent_at")
    @Expose
    public String otpSentAt;
    @SerializedName("referral_code")
    @Expose
    public Object referralCode;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("city_name")
    @Expose
    public String cityName;
    @SerializedName("state_name")
    @Expose
    public String stateName;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("pin_code")
    @Expose
    public String pinCode;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("latitude")
    @Expose
    public Object latitude;
    @SerializedName("longitude")
    @Expose
    public Object longitude;
    @SerializedName("profile_image")
    @Expose
    public String profileImage;
    @SerializedName("qrcode")
    @Expose
    public String qrcode;

    public String getqrcode() {
        return qrcode;
    }

    public void setqrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getUserId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailId() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNo() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void setApiToken(String apiToken) {
        this.apiToken = apiToken;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtpSentAt() {
        return otpSentAt;
    }

    public void setOtpSentAt(String otpSentAt) {
        this.otpSentAt = otpSentAt;
    }

    public Object getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(Object referralCode) {
        this.referralCode = referralCode;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPin_code() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getStreet() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getUserPic() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


}
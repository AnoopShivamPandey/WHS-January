package com.apk_home_service.customer.Wallet;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {
    String TAG = "ApiService";

    @Headers("Content-Type: application/json")
    @POST("customer/wallet")
    Call<String> getWalletDetails(
            @Query("api_token") String token,
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/redeem-coupon")
    Call<String> updateWallet(
            @Query("api_token") String token,
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("stall/update-profile-status")
    Call<String> updateProfileStatus(
            @Query("api_token") String token,
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("stall/logout")
    Call<String> logoutUser(
            @Query("api_token") String token,
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("stall/accept-reject-order")
    Call<String> acceptrejectOrder(
            @Query("api_token") String token,
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("stall/update_position")
    Call<String> updateUserLocation(
            @Query("api_token") String token,
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("stall/tender-stall-list")
    Call<String> getStallList(
            @Query("api_token") String token,
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("stall/assigned-product-list")
    Call<String> getInventoryList(
            @Query("api_token") String token,
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("stall/update-stall-products")
    Call<String> putUpdatedProducts(
            @Query("api_token") String token,
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/orderDetails")
    Call<String> getBookingDetails(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/contactus")
    Call<String> getContactUsDetails(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/wallet-statement")
    Call<String> getTransactionList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/track-order")
    Call<String> trackOrder(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/cancelOrder")
    Call<String> cancelUserOrder(
            @Body String body);


    @Headers("Content-Type: application/json")
    @POST("customer/available-coupons")
    Call<String> availableCoupons(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/category-list")
    Call<String> getCategoryList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/apply-coupon")
    Call<String> applyCoupon(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/doctorList")
    Call<String> getRestaurantList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/dishList")
    Call<String> getDishList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/orders")
    Call<String> createOrder(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/orderlist")
    Call<String> getBookingList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/stateList")
    Call<String> getStateList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/cityList")
    Call<String> getCityList(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/general-settings")
    Call<String> getGeneralSettings(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/scan-pay")
    Call<String> transferMoney(
            @Body String body);

    @Headers("Content-Type: application/json")
    @POST("customer/banner")
    Call<String> getBanners(
            @Body String body);

}

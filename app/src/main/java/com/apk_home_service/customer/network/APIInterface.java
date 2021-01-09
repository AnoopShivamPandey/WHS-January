package com.apk_home_service.customer.network;

import com.apk_home_service.customer.modal.CreateOrder;
import com.apk_home_service.customer.modal.LandingModal;
import com.apk_home_service.customer.modal.PlaceOrder;
import com.apk_home_service.customer.modal.RequestModal;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

/**
 * Created by Rajnish on 6/27/2017.
 */

public interface APIInterface {

    @POST("/customer/category-list")
    void getFoodCategory(@Body LandingModal data, Callback<String> callback);

    @POST("/customer/send-otp")
    void SendOTP(@Body RequestModal data, Callback<String> callback);

    @POST("/customer/validate-otp")
    void ValidateOTP(@Body RequestModal data, Callback<String> callback);

//    @POST("/customer/signup")
//    void Signup(@Body RequestModal data, Callback<String> callback);

    @Multipart
    @POST("/customer/signup")
    void Signup(@PartMap Map<String, String> data, @PartMap Map<String, TypedFile> Files, Callback<String> callback);

    @POST("/customer/search-trolly")
    void searchTrolly(@Query("api_token") String apiTocken, @Body RequestModal data, Callback<String> callback);

    @POST("/customer/sub-category")
    void getFoodSubCategory(@Query("api_token") String apiTocken, @Body RequestModal data, Callback<String> callback);

    @POST("/customer/dishList")
    void getTrollyFoodList( @Body RequestModal data, Callback<String> callback);

    @POST("/customer/create-order")
    void createOrder(@Query("api_token") String apiTocken, @Body CreateOrder data, Callback<String> callback);

    @POST("/customer/place-order")
    void placeOrder(@Query("api_token") String apiTocken, @Body PlaceOrder data, Callback<String> callback);

    @POST("/customer/order-list")
    void getOrderList(@Query("api_token") String apiTocken, @Body RequestModal data, Callback<String> callback);

    @POST("/customer/order-detail")
    void getOrderDetail(@Query("api_token") String apiTocken, @Body RequestModal data, Callback<String> callback);

    @POST("/customer/trolly-location")
    void getTrollyLOcation(@Query("api_token") String apiTocken, @Body RequestModal data, Callback<String> callback);


    @POST("/customer/submit-rating")
    void setRating(@Query("api_token") String apiTocken, @Body RequestModal data, Callback<String> callback);


    @Multipart
    @POST("/customer/updateProfile")
    void updateProfile(@PartMap Map<String, String> data, @PartMap Map<String, TypedFile> Files, Callback<String> callback);

    @GET("/maps/api/directions/json")
    void getGooglePath(@Query("origin") String origin,
                       @Query("destination") String destination,
                       @Query("key") String key,
                       @Query("sensor") boolean sensor,
                       @Query("mode") String mode,
                       @Query("alternatives") boolean alternatives,
                       Callback<String> callback);


    @POST("/customer/category-detail")
    void getCategoryDetail(@Query("api_token") String apiTocken, @Body RequestModal data, Callback<String> callback);

    @POST("/customer/festive-product")
    void getFestiveProduct(@Query("api_token") String apiTocken, @Body RequestModal data, Callback<String> callback);

    @POST("/customer/update-shipping")
    void getUpdateShipingAddress(@Query("api_token") String apiTocken, @Body CreateOrder data, Callback<String> callback);


}

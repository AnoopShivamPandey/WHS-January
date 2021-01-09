package com.commonutility.ModelClass;

import com.commonutility.CommonUtill;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Rajnish on 9/15/2017.
 */

public class RatingData {

    @SerializedName("ServiceType")
    @Expose
    private String ServiceType;

    @SerializedName("ServiceID")
    @Expose
    private String ServiceID;

    @SerializedName("RateToUserID")
    @Expose
    private String RateToUserID;

    @SerializedName("RateByUserID")
    @Expose
    private String RateByUserID;

    @SerializedName("Rate")
    @Expose
    private String Rate;

    @SerializedName("Review")
    @Expose
    private String Review;


    public RatingData(String serviceType, String serviceID, String rateToUserID, String rateByUserID, String rate, String review) {
        ServiceType = serviceType;
        ServiceID = serviceID;
        RateToUserID = rateToUserID;
        RateByUserID = rateByUserID;
        Rate = rate;
        Review = review;
    }


}


package com.commonutility.ModelClass;

import com.commonutility.CommonUtill;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class BookingDetailData {

    @SerializedName("BookingID")
    @Expose
    private String UserId;


    public BookingDetailData(String UserId) {
        this.UserId = UserId;
    }

}

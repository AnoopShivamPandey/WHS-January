
package com.commonutility.ModelClass;

import com.commonutility.CommonUtill;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class ForgotPassData {

    @SerializedName("UserId")
    @Expose
    private String UserId;


    public ForgotPassData(String UserId) {
        this.UserId = UserId;
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserId", UserId);
            CommonUtill.print("ForgotPassData ==" + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}

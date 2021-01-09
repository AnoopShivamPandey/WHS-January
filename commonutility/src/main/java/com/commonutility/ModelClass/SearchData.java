
package com.commonutility.ModelClass;

import com.commonutility.CommonUtill;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class SearchData {

    @SerializedName("UserID")
    @Expose
    private String UserID;

    @SerializedName("PageID")
    @Expose
    private String PageID;

    @SerializedName("Search")
    @Expose
    private String Search;


    public SearchData(String UserID, String PageID, String Search) {
        this.UserID = UserID;
        this.PageID = PageID;
        this.Search = Search;
    }


    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("UserID", UserID);
            jsonObject.put("PageID", PageID);
            jsonObject.put("Search", Search);
            CommonUtill.print("SearchData ==" + jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return super.toString();
    }
}


package com.commonutility.ModelClass;

import com.commonutility.CommonUtill;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginData {

//"type":"customer",
//        "roleID":"2"

    @SerializedName("UserId")
    @Expose
    private String UserId;

    @SerializedName("UserPassword")
    @Expose
    private String UserPassword;

    @SerializedName("DeviceID")
    @Expose
    private String DeviceID;

    @SerializedName("DeviceType")
    @Expose
    private String DeviceType;

    @SerializedName("FCMkey")
    @Expose
    private String FCMkey;

    @SerializedName("type")
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRoleID() {
        return roleID;
    }

    public void setRoleID(String roleID) {
        this.roleID = roleID;
    }

    @SerializedName("roleID")
    @Expose
    private String roleID;


    public LoginData(String Email, String Password, String DeviceID, String DeviceType, String FCMkey) {
        this.UserId = Email;
        this.UserPassword = Password;
        this.DeviceID = DeviceID;
        this.DeviceType = DeviceType;
        this.FCMkey = FCMkey;
    }

}

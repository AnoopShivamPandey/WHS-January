
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginStatusData {


    @SerializedName("UserID")
    @Expose
    private String UserId;

    @SerializedName("DeviceID")
    @Expose
    private String DeviceID;

    @SerializedName("DeviceType")
    @Expose
    private String DeviceType;

    @SerializedName("FCMKey")
    @Expose
    private String FCMkey;

    @SerializedName("Status")
    @Expose
    private boolean Status;

    public LoginStatusData(String userId, String deviceID, String deviceType, String FCMkey, boolean status, String roleID) {
        UserId = userId;
        DeviceID = deviceID;
        DeviceType = deviceType;
        this.FCMkey = FCMkey;
        Status = status;
        this.roleID = roleID;
    }

    @SerializedName("RoleID")
    @Expose
    private String roleID;

}

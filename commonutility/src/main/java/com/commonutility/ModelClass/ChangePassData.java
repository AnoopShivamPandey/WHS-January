
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassData {

    @SerializedName("UserID")
    @Expose
    private String UserID;

    @SerializedName("CurrentPassword")
    @Expose
    private String CurrentPassword;

    public ChangePassData(String userID, String currentPassword, String newPassword) {
        UserID = userID;
        CurrentPassword = currentPassword;
        NewPassword = newPassword;
    }

    @SerializedName("NewPassword")
    @Expose
    private String NewPassword;




}

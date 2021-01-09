
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotifyListData {

    @SerializedName("UserID")
    @Expose
    private String ReceiverID;

    public NotifyListData(String salonID, String pageno) {
        ReceiverID = salonID;
        Pageno = pageno;
    }

    @SerializedName("Pageno")
    @Expose
    private String Pageno;

}

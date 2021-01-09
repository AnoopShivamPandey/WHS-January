
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ServiceListData {

    @SerializedName("SalonID")
    @Expose
    private String SalonID;

    public ServiceListData(String salonID, String pageno) {
        SalonID = salonID;
        Pageno = pageno;
    }

    @SerializedName("Pageno")
    @Expose
    private String Pageno;

}

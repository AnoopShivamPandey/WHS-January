
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetAllServiceData implements Serializable{

    @SerializedName("ServiceID")
    @Expose
    private String ServiceID;

    @SerializedName("SalonID")
    @Expose
    private String SalonID;

    public GetAllServiceData(String serviceID, String salonID, String pageno) {
        ServiceID = serviceID;
        SalonID = salonID;
        Pageno = pageno;
    }

    @SerializedName("Pageno")
    @Expose
    private String Pageno;


}

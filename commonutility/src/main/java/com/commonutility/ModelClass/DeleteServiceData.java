
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteServiceData {

    @SerializedName("SalonID")
    @Expose
    private String SalonID;

    @SerializedName("ServiceID")
    @Expose
    private String ServiceID;

    @SerializedName("Price")
    @Expose
    private String Price;

    public void setPrice(String price) {
        Price = price;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    @SerializedName("Duration")
    @Expose
    private String Duration;

    public DeleteServiceData(String salonID, String serviceID, String createdBy) {
        SalonID = salonID;
        ServiceID = serviceID;
        CreatedBy = createdBy;
    }

    @SerializedName("CreatedBy")
    @Expose
    private String CreatedBy;

}

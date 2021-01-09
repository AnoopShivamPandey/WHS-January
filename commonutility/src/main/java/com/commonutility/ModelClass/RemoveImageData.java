
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RemoveImageData {

//"type":"customer",
//        "roleID":"2"


    @SerializedName("SalonID")
    @Expose
    private String SalonID;

    @SerializedName("ImageID")
    @Expose
    private String ImageID;


    public RemoveImageData(String salonID, String imageID) {
        SalonID = salonID;
        ImageID = imageID;
    }


}

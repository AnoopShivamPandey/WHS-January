
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AppointmentListData {

//"type":"customer",
//        "roleID":"2"


    @SerializedName("SalonID")
    @Expose
    private String SalonID;

    @SerializedName("ArtistID")
    @Expose
    private String ArtistID;

    @SerializedName("Date")
    @Expose
    private String Date;

    public AppointmentListData(String salonID, String artistID, String date, String pageno) {
        SalonID = salonID;
        ArtistID = artistID;
        Date = date;
        this.pageno = pageno;
    }

    @SerializedName("pageno")
    @Expose
    private String pageno;


}

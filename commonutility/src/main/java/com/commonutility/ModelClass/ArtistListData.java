
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArtistListData {

//"type":"customer",
//        "roleID":"2"


    @SerializedName("SalonID")
    @Expose
    private String SalonID;

    @SerializedName("SearchKey")
    @Expose
    private String SearchKey;


    public ArtistListData(String salonID, String pageno,String searchKey) {
        SalonID = salonID;
        this.pageno = pageno;
        this.SearchKey = searchKey;
    }

    @SerializedName("pageno")
    @Expose
    private String pageno;


}

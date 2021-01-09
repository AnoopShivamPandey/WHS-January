
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoadMessageData {

    @SerializedName("SenderID")
    @Expose
    private String SenderID;

    @SerializedName("ReceiverID")
    @Expose
    private String ReceiverID;

    @SerializedName("CAtegoryID")
    @Expose
    private String CAtegoryID;

    @SerializedName("BookingID")
    @Expose
    private String BookingID;


    public LoadMessageData(String SenderID, String ReceiverID, String CAtegoryID, String bookingID) {
        this.SenderID = SenderID;
        this.ReceiverID = ReceiverID;
        this.CAtegoryID = CAtegoryID;
        this.BookingID = bookingID;
    }

}

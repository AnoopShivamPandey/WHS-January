
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CancelBookingData {

    @SerializedName("BookingId")
    @Expose
    private String BookingId;

    @SerializedName("CreatedBy")
    @Expose
    private String CreatedBy;

    public CancelBookingData(String bookingId, String createdBy, String cancelComment) {
        BookingId = bookingId;
        CreatedBy = createdBy;
        CancelComment = cancelComment;
    }

    @SerializedName("CancelComment")
    @Expose
    private String CancelComment;

}

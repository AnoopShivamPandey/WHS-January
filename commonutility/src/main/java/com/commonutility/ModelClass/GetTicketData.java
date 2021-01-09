
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTicketData {

    @SerializedName("TicketID")
    @Expose
    private String TicketID;


    public GetTicketData(String ticketID) {
        TicketID = ticketID;
    }

}

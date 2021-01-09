
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetSalesData {


    @SerializedName("pageno")
    @Expose
    private String pageno;

    @SerializedName("CustomerName")
    @Expose
    private String CustomerName;

    @SerializedName("Salonid")
    @Expose
    private String Salonid="";

    public GetSalesData(String pageno, String customerName, String salonid, String date) {
        this.pageno = pageno;
        CustomerName = customerName;
        Salonid = salonid;
        Date = date;
    }

    @SerializedName("Date")
    @Expose
    private String Date;

}

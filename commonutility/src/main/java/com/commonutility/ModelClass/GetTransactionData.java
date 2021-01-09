
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTransactionData {


    @SerializedName("pageno")
    @Expose
    private String pageno;

    @SerializedName("customerid")
    @Expose
    private String customerid;

    @SerializedName("Salonid")
    @Expose
    private String Salonid="";

    @SerializedName("StartDate")
    @Expose
    private String StartDate;

    public GetTransactionData(String pageno,String salonid, String customerid, String startDate, String endDate) {
        this.pageno = pageno;
        this.customerid = customerid;
        this.Salonid = salonid;
        StartDate = startDate;
        EndDate = endDate;
    }

    @SerializedName("EndDate")
    @Expose
    private String EndDate;

}

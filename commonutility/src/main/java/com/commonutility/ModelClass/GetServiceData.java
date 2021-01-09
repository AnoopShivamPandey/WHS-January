
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GetServiceData implements Serializable{

    @SerializedName("ServiceIds")
    @Expose
    private String ServiceIds;

    @SerializedName("SalonName")
    @Expose
    private String SalonName;

    @SerializedName("ArtistName")
    @Expose
    private String ArtistName;

    @SerializedName("Page")
    @Expose
    private String Page;

    public GetServiceData(String serviceIds, String salonName, String artistName, String page) {
        ServiceIds = serviceIds;
        SalonName = salonName;
        ArtistName = artistName;
        Page = page;
    }

    public void setServiceIds(String serviceIds) {
        ServiceIds = serviceIds;
    }
}

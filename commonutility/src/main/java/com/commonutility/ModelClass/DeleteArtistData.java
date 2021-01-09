
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteArtistData {

    @SerializedName("SalonID")
    @Expose
    private String SalonID;

    @SerializedName("ArtistID")
    @Expose
    private String ArtistID;

    public DeleteArtistData(String salonID, String artistID) {
        SalonID = salonID;
        ArtistID = artistID;
    }

}

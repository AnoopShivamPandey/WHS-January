
package com.commonutility.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateScheduleData implements Serializable {


    @SerializedName("SalonID")
    @Expose
    private String SalonID;

    @SerializedName("CreatedBy")
    @Expose
    private String CreatedBy;


    @SerializedName("MonOpenTime")
    @Expose
    private String MonOpenTime;

    @SerializedName("MonCloseTime")
    @Expose
    private String MonCloseTime;

    @SerializedName("IsMonOff")
    @Expose
    private Boolean IsMonOff;


    @SerializedName("TuesOpenTime")
    @Expose
    private String TuesOpenTime;

    @SerializedName("TuesCloseTime")
    @Expose
    private String TuesCloseTime;

    @SerializedName("IsTuesOff")
    @Expose
    private Boolean IsTuesOff;


    @SerializedName("SunOpenTime")
    @Expose
    private String SunOpenTime;

    @SerializedName("SunCloseTime")
    @Expose
    private String SunCloseTime;

    @SerializedName("IsSunOff")
    @Expose
    private Boolean IsSunOff;


    @SerializedName("SatOpenTime")
    @Expose
    private String SatOpenTime;

    @SerializedName("SatCloseTime")
    @Expose
    private String SatCloseTime;

    @SerializedName("IsSatOff")
    @Expose
    private Boolean IsSatOff;


    @SerializedName("FriOpenTime")
    @Expose
    private String FriOpenTime;

    @SerializedName("FriCloseTime")
    @Expose
    private String FriCloseTime;

    @SerializedName("IsFriOff")
    @Expose
    private Boolean IsFriOff;


    @SerializedName("ThursOpenTime")
    @Expose
    private String ThursOpenTime;

    @SerializedName("ThursCloseTime")
    @Expose
    private String ThursCloseTime;

    @SerializedName("IsThursOff")
    @Expose
    private Boolean IsThursOff;

    @SerializedName("WedOpenTime")
    @Expose
    private String WedOpenTime;

    @SerializedName("WedCloseTime")
    @Expose
    private String WedCloseTime;

    @SerializedName("IsWedOff")
    @Expose
    private Boolean sWedOff;

    public UpdateScheduleData(String salonID, String createdBy, String monOpenTime,
                              String monCloseTime, Boolean isMonOff,
                              String tuesOpenTime, String tuesCloseTime,
                              Boolean isTuesOff, String sunOpenTime,
                              String sunCloseTime, Boolean isSunOff,
                              String satOpenTime, String satCloseTime,
                              Boolean isSatOff, String friOpenTime,
                              String friCloseTime, Boolean isFriOff,
                              String thursOpenTime, String thursCloseTime,
                              Boolean isThursOff, String wedOpenTime,
                              String wedCloseTime, Boolean sWedOff) {
        SalonID = salonID;
        CreatedBy = createdBy;
        MonOpenTime = monOpenTime;
        MonCloseTime = monCloseTime;
        IsMonOff = isMonOff;
        TuesOpenTime = tuesOpenTime;
        TuesCloseTime = tuesCloseTime;
        IsTuesOff = isTuesOff;
        SunOpenTime = sunOpenTime;
        SunCloseTime = sunCloseTime;
        IsSunOff = isSunOff;
        SatOpenTime = satOpenTime;
        SatCloseTime = satCloseTime;
        IsSatOff = isSatOff;
        FriOpenTime = friOpenTime;
        FriCloseTime = friCloseTime;
        IsFriOff = isFriOff;
        ThursOpenTime = thursOpenTime;
        ThursCloseTime = thursCloseTime;
        IsThursOff = isThursOff;
        WedOpenTime = wedOpenTime;
        WedCloseTime = wedCloseTime;
        this.sWedOff = sWedOff;
    }



}

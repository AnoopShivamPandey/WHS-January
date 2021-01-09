package com.commonutility.ModelClass;

import android.content.Context;

import com.commonutility.CommonUtill;
import com.commonutility.Database.PrefrenceHandler;

/**
 * Created by Rajnish on 9/28/2017.
 */

public class DeviceInformation {
    public String getDeviceType() {
        return deviceType;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getFcmRegId() {
        return fcmRegId;
    }


    String deviceType = "";
    String deviceId = "";
    String fcmRegId = "";
    private static DeviceInformation deviceInfo = null;

    private DeviceInformation(Context context) {
        deviceType = "Android";
        deviceId = CommonUtill.getDeviceId(context);
        fcmRegId = PrefrenceHandler.getStringValue("fcm_key", context);
    }

    public static DeviceInformation getDeviceInfo(Context context) {
        if (deviceInfo == null) {
            deviceInfo = new DeviceInformation(context);
        }
        return deviceInfo;
    }

    public void setFcmId(String fcmId) {
        fcmRegId = fcmId;
    }
}

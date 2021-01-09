package com.apk_home_service.customer.Wallet;

import org.json.JSONException;
import org.json.JSONObject;

public class GlobalAppApis {

    public static String getUserID(String user_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();

    }

    String walletDeatils(String coupon_code, String user_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("coupon_code", coupon_code);
            jsonObject.put("user_id", user_id);
//            jsonObject.put("mobile_no", mobno);
//            jsonObject.put("fcm_token", Constants.getFirebaseToken(mContext));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
    public String applyCoupon(String coupon_code, String user_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("coupan_id", coupon_code);
            jsonObject.put("user_id", user_id);
//            jsonObject.put("mobile_no", mobno);
//            jsonObject.put("fcm_token", Constants.getFirebaseToken(mContext));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
  public String getBookingList(String user_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", user_id);
//            jsonObject.put("mobile_no", mobno);
//            jsonObject.put("fcm_token", Constants.getFirebaseToken(mContext));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String getBookingDetails(String order_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
public String getTransactionData(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_id", id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String getContactUsDetails(String order_id, String message) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", order_id);
            jsonObject.put("message", message);
            jsonObject.put("subject", "1");
            jsonObject.put("name", "my treat");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String cancelBookingDetails(String order_id,String msg) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("order_id", order_id);
            jsonObject.put("cancel_reason", msg);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
    public String cityList(String city_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("state_id", city_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
 public String getRestaurantList(String restaurant_id,double latitude, double longitude,
                                 String city_id, String category_id) {
        JSONObject jsonObject = new JSONObject();
        try {

            jsonObject.put("restaraunt_id", restaurant_id);
            jsonObject.put("latitude", latitude);
            jsonObject.put("longitude", longitude);
            jsonObject.put("city_id", city_id);
            jsonObject.put("category_id", category_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
    public String nearbyRestaurants(String restaurant_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("restaraunt_id", restaurant_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    } public String getCategoryList() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("category_id", "");
            jsonObject.put("parent_id", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String getLatLong(String stall_id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("stall_id", stall_id);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public String transferDetails(String senderCode, String receiverCode,String amount) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sender_qrcode", senderCode);
            jsonObject.put("receiver_qrcode", receiverCode);
            jsonObject.put("amount", amount);
            jsonObject.put("remark", "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
    public String getBanners(String id) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("banner_city", id==null?"":id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}

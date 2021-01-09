package com.apk_home_service.customer.network;

/**
 * Created by RAHUL RAJ SINGH ( rahul.singh@teregro.com ) on 2016-11-23.
 */

public class NoConnectivityException extends RuntimeException {

    protected final String reason;


    public NoConnectivityException(String message) {
        super(message);
        this.reason = message;
    }

    @Override
    public String getMessage() {
        return reason;
    }

    public String getReason() {
        return reason;
    }

}
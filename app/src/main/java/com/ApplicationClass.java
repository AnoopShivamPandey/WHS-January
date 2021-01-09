package com;

import android.app.Application;

import com.apk_home_service.Payu.AppEnvironment;

/**
 * Created by pc on 18-02-2019.
 */


public class ApplicationClass extends Application {
    AppEnvironment appEnvironment;

    @Override
    public void onCreate() {
        super.onCreate();
        appEnvironment = AppEnvironment.SANDBOX;
    }
    public AppEnvironment getAppEnvironment() {
        return appEnvironment;
    }

    public void setAppEnvironment(AppEnvironment appEnvironment) {
        this.appEnvironment = appEnvironment;
    }

}







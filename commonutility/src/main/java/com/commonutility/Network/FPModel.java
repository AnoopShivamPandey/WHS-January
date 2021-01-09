package com.commonutility.Network;

import android.content.Context;
import android.os.Build;


import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by Tomasz Szafran ( tomek@appsvisio.com ) on 2015-10-20.
 */
public class FPModel {

    public APIInterface fpApi;
    private RestAdapter restAdapter;
//    private UserObject userObject = null;
//    public static String talla_dotnet_api_host = "http://203.115.105.210:1244/";
    public static String talla_dotnet_api_host = "http://tallaapi.gotalla.com/";
//    public static String talla_dotnet_api_host = "http://180.151.226.22:1244/";
//    public static String talla_api_host = "http://203.115.105.210:7007/";
    public static String talla_api_host = "http://gotalla.com/";
//    public static String talla_api_host = "http://180.151.226.21:801/";

//    public FPModel(Context context) {
//        changeServerAddress(context, host);
//    }

    public FPModel(Context context, String hostAddress) {
        changeServerAddress(context, hostAddress);
    }


    public void changeServerAddress(Context context, String hostAddress) {


        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okClient = new OkHttpClient();
//        OkHttpClient okClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        okClient.setConnectTimeout(60, TimeUnit.SECONDS);
        okClient.setReadTimeout(60, TimeUnit.SECONDS);
        okClient.setWriteTimeout(60, TimeUnit.SECONDS);
        restAdapter = new RestAdapter.Builder()
                .setEndpoint(hostAddress)
//                .setLogLevel(BaseApplication.isDebug(context) ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setConverter(new SuccessResponseObject())
               /* .setRequestInterceptor(request -> {
//                    userObject = SPHelper.getUserSession(context);
//                    request.addHeader("API_VERSION", AppCore.getInstance().getAppInfo().apiVersion);
//                    if ((context != null && userObject != null && userObject.access != null) && isTokenActual(SPA.getUserData(context))) { // TODO check token is actual
//                        request.addHeader("TOKEN-ID", userObject.tokenId + "");
//                        request.addHeader("ACCESS-TOKEN", userObject.access);
//                    }
                    request.addHeader("APP_VERSION", AppCore.getInstance().getAppInfo().appVersion);
                    request.addHeader("APP_VERSION_CODE", AppCore.getInstance().getAppInfo().appVersionCode + "");
                    request.addHeader("DEVICE_PLATFORM", "android");
                    request.addHeader("DEVICE_OS", Build.VERSION.SDK_INT + "(" + Build.VERSION.RELEASE + ")");
                    request.addHeader("DEVICE_MODEL", Build.MANUFACTURER + " " + Build.MODEL);
                    request.addHeader("APP_TYPE", AppCore.getInstance().getAppInfo().appType);
                   // request.addHeader("connection", "close");  //by rahul

                })*/
                //.setClient(new OkClient(okClient))  //by RAHUL RAJ
                .setClient(new ConnectivityAwareUrlClient(context, new OkClient(okClient)))
                .setLogLevel(RestAdapter.LogLevel.HEADERS_AND_ARGS)
                .build();
        fpApi = restAdapter.create(APIInterface.class);

    }

//    public void changeServerAddress(Context context) {
//        changeServerAddress(context, ServerChooser.getHostAddress(context));
//    }
}

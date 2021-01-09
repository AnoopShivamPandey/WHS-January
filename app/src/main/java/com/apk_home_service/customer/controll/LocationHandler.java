//package com.apk_home_service.customer.controll;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.pm.PackageManager;
//import android.location.Address;
//import android.location.Geocoder;
//import android.location.Location;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.ActivityCompat;
//
//import com.apk_home_service.customer.utill.CommonUtill;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Locale;
//
///**
// * Created by Admin on 2/1/2018.
// */
//
//public class LocationHandler implements GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener {
//
//    Activity act;
//    LocationHandlerListener locationHandlerListener;
//
//    private GoogleApiClient mGoogleApiClient;
//    private LocationRequest mLocationRequest;
//
//    public LocationHandler(Activity act, LocationHandlerListener locationHandlerListener) {
//        this.act = act;
//        this.locationHandlerListener = locationHandlerListener;
//
//        mGoogleApiClient = new GoogleApiClient.Builder(act)
//                .addApi(LocationServices.API).
//                        addConnectionCallbacks(this).
//                        addOnConnectionFailedListener(this)
//                .build();
//
//        CommonUtill.print("LocationHandler ==");
//
//    }
//
//    public void initLocation() {
//        CommonUtill.print("initLocation ==");
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        CommonUtill.print("onConnected ==");
//        mLocationRequest = LocationRequest.create();
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setInterval(1000); // Update location every second
//
//
//        if (ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(act, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//
////        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, act);
//        Location loc = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
//        CommonUtill.print("onConnected Location =="+loc);
//        locationHandlerListener.onReceivedLocation(loc);
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//
//    public String getAddress(double latitude, double longitude) {
//        StringBuilder result = new StringBuilder();
//        try {
//            Geocoder geocoder = new Geocoder(act, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
//           CommonUtill.print("getAddress =="+addresses);
//            if (addresses.size() > 0) {
//                Address address = addresses.get(0);
//                result.append(address.getAddressLine(0));
////                result.append(address.getLocality()).append("\n");
////                result.append(address.getCountryName());
//                CommonUtill.print("getAddress result =="+result);
//            }
//        } catch (IOException e) {
//            CommonUtill.print("IOException errr =="+e.getMessage());
//        }
//
//        return result.toString();
//    }
//
//
//    public interface LocationHandlerListener {
//        public void onReceivedLocation(Location location);
//    }
//
//
//
//}

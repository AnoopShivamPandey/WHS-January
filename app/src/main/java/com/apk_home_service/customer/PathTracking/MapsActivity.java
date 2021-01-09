//package com.apk_home_service.customer.PathTracking;
//
//import android.Manifest;
//import android.app.Activity;
//import android.content.Context;
//import android.content.pm.PackageManager;
//import android.graphics.Color;
//import android.location.Address;
//import android.location.Criteria;
//import android.location.Geocoder;
//import android.location.Location;
//import android.location.LocationManager;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.content.ContextCompat;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.apk_home_service.customer.R;
//import com.apk_home_service.customer.Wallet.API_Config;
//import com.apk_home_service.customer.Wallet.ApiService;
//import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
//import com.apk_home_service.customer.Wallet.GlobalAppApis;
//import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
//import com.apk_home_service.customer.data.PrefrenceHandler;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.location.LocationListener;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationServices;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.BitmapDescriptorFactory;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//import com.google.android.gms.maps.model.PolylineOptions;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Locale;
//
//import retrofit2.Call;
//
//public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
//        GoogleApiClient.ConnectionCallbacks,
//        GoogleApiClient.OnConnectionFailedListener,
//        LocationListener {
//
//    private GoogleMap mMap;
//    ArrayList markerPoints = new ArrayList();
//    Activity mActivity;
//    String stall_id = "";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);
//        mActivity = this;
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        if (getIntent().hasExtra("stall_id")) {
//            stall_id = getIntent().getStringExtra("stall_id");
//        }
//        h = new Handler();
//        getStallLocation();
//    }
//
//    Handler h;
//    Runnable r = new Runnable() {
//        @Override
//        public void run() {
//            getStallLocation();
//        }
//    };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if(checkLocationPermission())
//                buildGoogleApiClient();
//        }
//        else
//        {
//            buildGoogleApiClient();
//
//        }
//    }
//
//
//    double userLatitude = 0, userLongitude = 0;
//    public boolean checkLocationPermission() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_LOCATION);
//            }
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
//    GoogleApiClient mGoogleApiClient;
//    Location mLastLocation;
//    Marker mCurrLocationMarker;
//    LocationRequest mLocationRequest;
//    @Override
//    public void onRequestPermissionsResult(int requestCode,
//                                           String permissions[], int[] grantResults) {
//        switch (requestCode) {
//            case MY_PERMISSIONS_REQUEST_LOCATION: {
//                if (grantResults.length > 0
//                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    if (ContextCompat.checkSelfPermission(this,
//                            Manifest.permission.ACCESS_FINE_LOCATION)
//                            == PackageManager.PERMISSION_GRANTED) {
//                        if (mGoogleApiClient == null) {
//                            buildGoogleApiClient();
//                        }
//                        mMap.setMyLocationEnabled(true);
//                    }
//                } else {
//                    Toast.makeText(this, "permission denied",
//                            Toast.LENGTH_LONG).show();
//                }
//                return;
//            }
//        }
//    }
//    protected synchronized void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        mGoogleApiClient.connect();
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(1000);
//        mLocationRequest.setFastestInterval(1000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                == PackageManager.PERMISSION_GRANTED) {
//            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
//                    mLocationRequest, this);
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//        if (mCurrLocationMarker != null) {
//            mCurrLocationMarker.remove();
//        }
////Showing Current Location Marker on Map
//        userLatitude = location.getLatitude();
//        userLongitude = location.getLongitude();
//        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//        MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.position(latLng);
//        LocationManager locationManager = (LocationManager)
//                getSystemService(Context.LOCATION_SERVICE);
//        String provider = locationManager.getBestProvider(new Criteria(), true);
//        if (ActivityCompat.checkSelfPermission(this,
//                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
//                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                        != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
//        Location locations = locationManager.getLastKnownLocation(provider);
//        List<String> providerList = locationManager.getAllProviders();
//        if (null != locations && null != providerList && providerList.size() > 0) {
//            double longitude = locations.getLongitude();
//            double latitude = locations.getLatitude();
//            Geocoder geocoder = new Geocoder(getApplicationContext(),
//                    Locale.getDefault());
//            try {
//                List<Address> listAddresses = geocoder.getFromLocation(latitude,
//                        longitude, 1);
//                if (null != listAddresses && listAddresses.size() > 0) {
//                    String state = listAddresses.get(0).getAdminArea();
//                    String country = listAddresses.get(0).getCountryName();
//                    String subLocality = listAddresses.get(0).getSubLocality();
//                    markerOptions.title("" + latLng + "," + subLocality + "," + state
//                            + "," + country);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
//        mCurrLocationMarker = mMap.addMarker(markerOptions);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
//        if (mGoogleApiClient != null) {
//            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,
//                    this);
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        h.removeCallbacks(r);
//    }
//
//    void getStallLocation() {
//        try {
//            String otp = new GlobalAppApis().getLatLong(stall_id);
//
//            ApiService client = API_Config.getApiClient_ByPost();
//            Call<String> call = client.getStallLatLong(PrefrenceHandler.getPreferences(this).getAPI_TOKEN(), otp);
//            new ConnectToRetrofit(new RetrofitCallBackListenar() {
//                @Override
//                public void RetrofitCallBackListenar(String result, String action) throws JSONException {
//                    JSONObject jsonObject = new JSONObject(result);
//
//                    if (jsonObject.getBoolean("status")) {
//                        h.postDelayed(r, 15 * 1000);
//             LatLng origin = new LatLng(userLatitude,userLongitude);
//             LatLng dest = new LatLng(Double.parseDouble(jsonObject.getJSONObject("data").getString("latitude")),
//                     Double.parseDouble(jsonObject.getJSONObject("data").getString("longitude")));
//                        String url = getDirectionsUrl(origin, dest);
//
//                        DownloadTask downloadTask = new DownloadTask();
//
//                        // Start downloading json data from Google Directions API
//                        downloadTask.execute(url);
//
//                      /*  if (jsonObject.has("data") && jsonObject.getJSONObject("data").length() > 0) {
//                            Toast.makeText(mActivity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
////                            finish();
//                            onResume();
//                        } else {
//                            Toast.makeText(mActivity, "Please try again.", Toast.LENGTH_SHORT).show();
//
//                        }*/
//                    } else {
//                    }
//                }
//
//            }, mActivity, call, "", false);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        mMap = googleMap;
//        LatLng sydney = new LatLng(-34, 151);
//        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 16));
//
//        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//                if (markerPoints.size() > 1) {
//                    markerPoints.clear();
//                    mMap.clear();
//                }
//
//                // Adding new item to the ArrayList
////                markerPoints.add(latLng);
////
////                // Creating MarkerOptions
////                MarkerOptions options = new MarkerOptions();
////
////                // Setting the position of the marker
////                options.position(latLng);
////
////                if (markerPoints.size() == 1) {
////                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
////                } else if (markerPoints.size() == 2) {
////                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
////                }
////
////                // Add new marker to the Google Map Android API V2
////                mMap.addMarker(options);
//
//                // Checks, whether start and end locations are captured
//                if (markerPoints.size() >= 2) {
//                    LatLng origin = (LatLng) markerPoints.get(0);
//                    LatLng dest = (LatLng) markerPoints.get(1);
//
//                    // Getting URL to the Google Directions API
//                }
//
//            }
//        });
//
//    }
//
//    private class DownloadTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected String doInBackground(String... url) {
//
//            String data = "";
//
//            try {
//                data = downloadUrl(url[0]);
//            } catch (Exception e) {
//                Log.d("Background Task", e.toString());
//            }
//            return data;
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//
//            ParserTask parserTask = new ParserTask();
//
//
//            parserTask.execute(result);
//
//        }
//    }
//
//
//    /**
//     * A class to parse the Google Places in JSON format
//     */
//    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
//
//        // Parsing the data in non-ui thread
//        @Override
//        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {
//
//            JSONObject jObject;
//            List<List<HashMap<String, String>>> routes = null;
//
//            try {
//                jObject = new JSONObject(jsonData[0]);
//                DirectionsJSONParser parser = new DirectionsJSONParser();
//
//                routes = parser.parse(jObject);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return routes;
//        }
//
//        @Override
//        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
//            ArrayList points = null;
//            PolylineOptions lineOptions = null;
//            MarkerOptions markerOptions = new MarkerOptions();
//
//            for (int i = 0; i < result.size(); i++) {
//                points = new ArrayList();
//                lineOptions = new PolylineOptions();
//
//                List<HashMap<String, String>> path = result.get(i);
//
//                for (int j = 0; j < path.size(); j++) {
//                    HashMap<String, String> point = path.get(j);
//
//                    double lat = Double.parseDouble(point.get("lat"));
//                    double lng = Double.parseDouble(point.get("lng"));
//                    LatLng position = new LatLng(lat, lng);
//
//                    points.add(position);
//                }
//
//                lineOptions.addAll(points);
//                lineOptions.width(12);
//                lineOptions.color(Color.RED);
//                lineOptions.geodesic(true);
//
//            }
//
//// Drawing polyline in the Google Map for the i-th route
//            mMap.addPolyline(lineOptions);
//        }
//    }
//
//    private String getDirectionsUrl(LatLng origin, LatLng dest) {
//
//        // Origin of route
//        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
//
//        // Destination of route
//        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
//
//        // Sensor enabled
//        String sensor = "sensor=false";
//        String mode = "mode=driving";
//        // Building the parameters to the web service
//        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + mode;
//
//        // Output format
//        String output = "json";
//
//        // Building the url to the web service
//        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_server_key_latest);
//
//
//        return url;
//    }
//
//    /**
//     * A method to download json data from url
//     */
//    private String downloadUrl(String strUrl) throws IOException {
//        String data = "";
//        InputStream iStream = null;
//        HttpURLConnection urlConnection = null;
//        try {
//            URL url = new URL(strUrl);
//
//            urlConnection = (HttpURLConnection) url.openConnection();
//
//            urlConnection.connect();
//
//            iStream = urlConnection.getInputStream();
//
//            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
//
//            StringBuffer sb = new StringBuffer();
//
//            String line = "";
//            while ((line = br.readLine()) != null) {
//                sb.append(line);
//            }
//
//            data = sb.toString();
//
//            br.close();
//
//        } catch (Exception e) {
//            Log.d("Exception", e.toString());
//        } finally {
//            iStream.close();
//            urlConnection.disconnect();
//        }
//        return data;
//    }
//}

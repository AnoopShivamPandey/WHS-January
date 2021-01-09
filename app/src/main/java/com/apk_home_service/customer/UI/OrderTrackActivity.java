package com.apk_home_service.customer.UI;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.apk_home_service.customer.PathTracking.DirectionsJSONParser;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.fragment.TouchableMapFragment;
import com.apk_home_service.customer.Wallet.API_Config;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.data.ConstantData;
import com.apk_home_service.customer.data.LocationService;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.network.FPModel;
import com.apk_home_service.customer.utill.CancelOrderDialog;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.Constants;
import com.apk_home_service.customer.utill.MapUtill;
import com.apk_home_service.customer.utill.PermissionUtill;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;

public class OrderTrackActivity extends AppCompatActivity {

    TouchableMapFragment supportMapTouchFragment;
    GoogleMap googleMap;
    View touchView;
    Boolean mMapIsTouched = true;
    ProgressDialog pDialog;
    TextView trollyName, priceTxt, statusTxt, cancleTxt;
    ImageView trollyImg, rateImg;
    double cLat, cLong;
    RatingBar ratingStar;
    String trollyId;
    String totalPrice;
    UserData userData;
    String trollyMobile;
    String orderStatus = "";
    final int FROM_CALL = 102;
    JSONArray orderArray;
    long timeslot = 10000;
    Activity mActivity;
    Handler pathHandle = new Handler();
    Runnable pathCreate = new Runnable() {
        @Override
        public void run() {
            getOrderTracker();
            pathHandle.postDelayed(this, timeslot);
        }
    };


    OnMapReadyCallback mapReadyCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap mgoogleMap) {
            googleMap = mgoogleMap;
            prepareMap();
            prepareTouch();
            pathHandle.post(pathCreate);

            cLat = Double.parseDouble(Constants.getSavedPreferences(mActivity, Constants.LATITUDE, "0"));
            cLong = Double.parseDouble(Constants.getSavedPreferences(mActivity, Constants.LONGITUDE, "0"));

            zoomOnLocation(new LatLng(cLat, cLong));

            pathHandle.post(pathCreate);

//


//            getOrderDetail();
        }
    };
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    public boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startService(new Intent(mActivity, LocationService.class));
                }
            }, 1000);
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startService(new Intent(mActivity, LocationService.class));
                            }
                        }, 1000);
                    }
                } else {
                    Toast.makeText(this, "permission denied",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }
            case FROM_CALL: {
                if (!PermissionUtill.hasPermissions(this, permissions)) {
                    if (PermissionUtill.hasRationalPermissions(this, permissions)) {
                        PermissionUtill.openDialog(mActivity);
                    } else {
                        Toast.makeText(this, "Please go to setting and enable permissions.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    doCall();
                }
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_track);
        mActivity = this;
        pDialog = CommonUtill.showProgressDialog(this, "Please wait...");
        touchView = findViewById(R.id.touchView);
        try {
            userData = (UserData) PrefrenceHandler.getPreferences(mActivity).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        cLat = Double.parseDouble(Constants.getSavedPreferences(mActivity, Constants.LATITUDE, "0"));
        cLong = Double.parseDouble(Constants.getSavedPreferences(mActivity, Constants.LONGITUDE, "0"));

        trollyImg = (ImageView) findViewById(R.id.trollyImg);
        rateImg = (ImageView) findViewById(R.id.rateImg);
        ratingStar = (RatingBar) findViewById(R.id.ratingStar);
        trollyName = (TextView) findViewById(R.id.trollyName);
        priceTxt = (TextView) findViewById(R.id.priceTxt);
        statusTxt = (TextView) findViewById(R.id.statusTxt);
        cancleTxt = (TextView) findViewById(R.id.cancleTxt);


        statusTxt.setText("Your order is "+getIntent().getStringExtra("status"));
        if (MapUtill.checkGooglePlayServices(this)) {
            supportMapTouchFragment = (TouchableMapFragment) getSupportFragmentManager().findFragmentById(R.id.trackmap);
            supportMapTouchFragment.getMapAsync(mapReadyCallback);
        }

//        ratingStar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
//            @Override
//            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
//                rateTrolly(rating);
//            }
//        });

        findViewById(R.id.callBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtill.requestPermission(mActivity, PermissionUtill.callPhonePermission, FROM_CALL)) {
                    doCall();
                }
            }
        });

        findViewById(R.id.cancelBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (orderStatus.equalsIgnoreCase(ConstantData.ORDER_COMPLETED)) {
//                    startActivity(new Intent(mActivity, RatingActivity.class).putExtra("trollyID",trollyId));
                } else {
                    new CancelOrderDialog(mActivity, new CancelOrderDialog.ConfirmationDialogListener() {
                        @Override
                        public void onYesButtonClicked(CancelOrderDialog.confirmTask task, String msg) {
                            cancelOrder(msg);
                        }

                        @Override
                        public void onNoButtonClicked(CancelOrderDialog.confirmTask task) {

                        }
                    }, CancelOrderDialog.confirmTask.NONE);
                }

            }
        });


        findViewById(R.id.trackBg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showOrderDetail();
            }
        });


    }

    private void openCancelDialog() {

    }

    private void showOrderDetail() {
//        startActivity(new Intent(this, OrderListDetailsActivity.class).putExtra("order", orderArray.toString()).putExtra("price", totalPrice));
    }


    private void prepareMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        googleMap.getUiSettings().setTiltGesturesEnabled(true);
        googleMap.setBuildingsEnabled(true);

        googleMap.getUiSettings().setZoomGesturesEnabled(true);
        googleMap.getUiSettings().setScrollGesturesEnabled(true);
        googleMap.getUiSettings().setRotateGesturesEnabled(false);
        googleMap.getUiSettings().setMyLocationButtonEnabled(false);

//        googleMap.setOnCameraChangeListener(cameraChangeListener);
//        googleMap.setOnMarkerClickListener(markerClickListener);

    }

    private void prepareTouch() {
        touchView.setOnTouchListener(new View.OnTouchListener() {

            final View mMapView = supportMapTouchFragment.getView();
            private float scaleFactor = 1f;


            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                mMapIsTouched = true;
                CommonUtill.print("onTouch===" + mMapIsTouched);
                if (simpleGestureDetector.onTouchEvent(motionEvent)) { // Double tap
                    googleMap.animateCamera(CameraUpdateFactory.zoomIn()); // Fixed zoom in
                } else if (motionEvent.getPointerCount() == 1) { // Single tap
                    mMapView.dispatchTouchEvent(motionEvent); // Propagate the event to the map (Pan)
                } else if (scaleGestureDetector.onTouchEvent(motionEvent)) { // Pinch zoom
                    googleMap.moveCamera(CameraUpdateFactory.zoomBy( // Zoom the map without panning it
                            (googleMap.getCameraPosition().zoom * scaleFactor
                                    - googleMap.getCameraPosition().zoom) / 5));
                }


                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    mMapIsTouched = false;
                }


                return true;
            }


            // Gesture detector to manage double tap gestures
            private GestureDetector simpleGestureDetector = new GestureDetector(
                    mActivity, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    return true;
                }

            });

            // Gesture detector to manage scale gestures
            private ScaleGestureDetector scaleGestureDetector = new ScaleGestureDetector(
                    mActivity, new ScaleGestureDetector.SimpleOnScaleGestureListener() {
                @Override
                public boolean onScale(ScaleGestureDetector detector) {
                    scaleFactor = detector.getScaleFactor();
                    return true;
                }
            });

        });

    }


    private void cancelOrder(String msg) {
//        RequestModal requestModal = new RequestModal();
//        requestModal.setOrderId(orderItem.getId());
//        requestModal.setReason(msg);
//        requestModal.setStatus("cancel");
//
//
//        CommonUtill.printClassData(requestModal);
//
//        pDialog.show();
//        new FPModel(this, FPModel.CRAZYBILLA_API).fpApi.cancelOrder(PrefrenceHandler.getPreferences(this).getAPI_TOKEN(), requestModal, new Callback<String>() {
//            @Override
//            public void success(String result, Response response) {
//                pDialog.dismiss();
//                CommonUtill.print("cancelOrder result ==" + result);
//                try {
//
//                    JSONObject jsonObject = new JSONObject(result);
//                    if (jsonObject.getBoolean("status")) {
//                        CommonUtill.print("cancelOrder status==true");
//                        showMessage(jsonObject.optString("message"));
//                    } else {
//                        if (jsonObject.getString("status_code").equalsIgnoreCase("401")) {
//                            PrefrenceHandler.getPreferences(mActivity).setLogout();
//                            startActivity(new Intent(mActivity, MobileActivity.class));
//                            finish();
//                        }
//                        CommonUtill.showTost(mActivity, jsonObject.getString("message"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                pDialog.dismiss();
//                CommonUtill.print("searchTrolly error ==" + error.getMessage());
//            }
//        });
    }


    public void getOrderTracker() {
        String otp = new GlobalAppApis().getBookingDetails(getIntent().getStringExtra("orderId"));
        ApiService client = API_Config.getApiClient_ByPost();
        Call<String> call = client.trackOrder(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
                    if (jsonObject.has("data") && jsonObject.getJSONObject("data").length() > 0) {
                        double tLat = Double.parseDouble(jsonObject.getJSONObject("data").getJSONObject("deliveryBoy").getString("latitude"));
                        double tLong = Double.parseDouble(jsonObject.getJSONObject("data").getJSONObject("deliveryBoy").getString("longitude"));
                        drawPolylinOnMap(new LatLng(tLat, tLong), new LatLng(cLat, cLong));
                        drawTrollyOnMap(new LatLng(tLat, tLong), new LatLng(cLat, cLong));
                        zoomOnLocation(new LatLng(cLat, cLong));

//                        setData(jsonObject.getJSONObject("data"));
                    } else {

                    }
                } else {
                }
            }

        }, mActivity, call, "", false);
    }


    private void zoomOnLocation(LatLng loc) {
        if (!zoom)
            return;
        try {
            LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
            builder1.include(loc);

            LatLngBounds bounds = builder1.build();

            int padding = 0; // offset from edges of the map in pixels
            CameraUpdate cu1 = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            CameraUpdate cu2 = CameraUpdateFactory.newLatLngZoom(loc, 13);

            googleMap.moveCamera(cu1);
            googleMap.animateCamera(cu1);
            googleMap.animateCamera(cu2);
            zoom = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

//        LatLngBounds.Builder builder1 = new LatLngBounds.Builder();
//
//        builder1.include(trollyLoc);
//        builder1.include(customerLoc);
//
//        LatLngBounds bounds = builder1.build();
//
//        int padding = 0; // offset from edges of the map in pixels
//        CameraUpdate cu1 = CameraUpdateFactory.newLatLngBounds(bounds, padding);
//        CameraUpdate cu2 = CameraUpdateFactory.newLatLngZoom(trollyLoc, 10);
//
//        googleMap.moveCamera(cu1);
//        googleMap.animateCamera(cu1);
//        googleMap.animateCamera(cu2);

    }

    private void drawTrollyOnMap(LatLng curreLatLng, LatLng endPoint) {
        if (googleMap != null) {
            googleMap.clear();
            final View mCustomMarkerView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.trolly_marker, null);

//            MarkerOptions nearDriverMarker = new MarkerOptions().position(curreLatLng);
//            nearDriverMarker.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, R.drawable.cart)));
//            googleMap.addMarker(nearDriverMarker);
//
//
//            MarkerOptions nearDriverMarker1 = new MarkerOptions().position(endPoint);
//            nearDriverMarker1.icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(mCustomMarkerView, R.drawable.map_pin)));
//            googleMap.addMarker(nearDriverMarker1);


            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(curreLatLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.cart));
            markerOptions.getPosition();
            googleMap.addMarker(markerOptions);


        }
    }


    private Bitmap getMarkerBitmapFromView(View view, int bitmap) {

        ImageView mMarkerImageView = (ImageView) view.findViewById(R.id.catImg);
        mMarkerImageView.setImageResource(bitmap);
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }


    private void drawPolylinOnMap(LatLng curreLatLng, LatLng endPoint) {

        String origin = curreLatLng.longitude + "," + curreLatLng.latitude;
        String destination = endPoint.latitude + "," + endPoint.longitude;

//        pDialog.show();
        new FPModel(this, FPModel.GOOGLE_API).fpApi.getGooglePath(origin, destination,
                "AIzaSyBU9GUpsDspR2Ye7YZ5tHqI4PcBqn695Tg", false, "driving", true, new Callback<String>() {
                    @Override
                    public void success(String result, Response response) {
//                pDialog.dismiss();
//                CommonUtill.print("drawPolylinOnMap result ==" + result);

                        try {
                            JSONObject jObject = new JSONObject(result);
                            DirectionsJSONParser parser = new DirectionsJSONParser();

                            List<List<HashMap<String, String>>> routes = parser.parse(jObject);

                            ArrayList points = null;
                            PolylineOptions lineOptions = null;
                            MarkerOptions markerOptions = new MarkerOptions();

                            CommonUtill.print("drawPolylinOnMap routes ==" + routes.size());
                            CommonUtill.print("drawPolylinOnMap routes ==" + routes);

                            for (int i = 0; i < routes.size(); i++) {
                                points = new ArrayList();
                                lineOptions = new PolylineOptions();

                                List<HashMap<String, String>> path = routes.get(i);

                                CommonUtill.print("drawPolylinOnMap path ==" + path.size());
                                CommonUtill.print("drawPolylinOnMap path ==" + path);

                                for (int j = 0; j < path.size(); j++) {
                                    HashMap<String, String> point = path.get(j);

                                    double lat = Double.parseDouble(point.get("lat"));
                                    double lng = Double.parseDouble(point.get("lng"));
                                    LatLng position = new LatLng(lat, lng);

                                    points.add(position);
                                }

                                lineOptions.addAll(points);
                                lineOptions.width(15);
                                lineOptions.color(getResources().getColor(R.color.colorGreen));
                                lineOptions.geodesic(true);

                            }

                            if (lineOptions != null)
                                googleMap.addPolyline(lineOptions);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void failure(RetrofitError error) {
//                pDialog.dismiss();
                        CommonUtill.print("drawPolylinOnMap error ==" + error.getMessage());
                    }
                });

    }

    private void doCall() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (!TextUtils.isEmpty(trollyMobile)) {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:" + trollyMobile));
            startActivity(intent);
        } else {
            CommonUtill.showTost(mActivity, "Mobile number not available.");
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("LOCATION_ACTION"));
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, new IntentFilter("LOCATION_ACTION1"));


    }

    BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };

    boolean zoom = true;

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);

        pathHandle.removeCallbacks(pathCreate);
    }
}

package com.apk_home_service.customer.UI.fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.apk_home_service.AdapterForBanner;
import com.apk_home_service.AdapterForCategoryHome;
import com.apk_home_service.BannerData;
import com.apk_home_service.CategoryData;
import com.apk_home_service.Data_List;
import com.bumptech.glide.Glide;
import com.apk_home_service.TransactionHistory;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.Categories;
import com.apk_home_service.customer.UI.MobileActivity;
import com.apk_home_service.customer.UI.OfferMenuListActivity;
import com.apk_home_service.customer.UI.ReachUs;
import com.apk_home_service.customer.UI.RestaurantList;
import com.apk_home_service.customer.UI.custom.MessageDialog;
import com.apk_home_service.customer.UI.custom.OfferDialog;
import com.apk_home_service.customer.Wallet.API_Config;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.MoneyTransferActivity;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.Wallet.UserWallet;
import com.apk_home_service.customer.controll.FoodCategoryAdapter;
import com.apk_home_service.customer.controll.FragmentListener;
import com.apk_home_service.customer.controll.PlaceArrayAdapter;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.FoodCategory;
import com.apk_home_service.customer.modal.LandingModal;
import com.apk_home_service.customer.modal.TrollyModel;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.network.FPModel;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.Constants;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit2.Call;


import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.apk_home_service.customer.Wallet.API_Config.getApiClient_ByPost;
import static com.apk_home_service.customer.Wallet.Urls.BASE_URL;

/**
 * Created by Rajnish on 6/30/2017.
 */
public class TrollySearchFragment extends Fragment implements View.OnClickListener, LocationListener {
    final int SELECT_ADDRESS = 110;
    boolean isMapMove = false;
    View mainView, touchView;
    boolean isTrollyClicked = false;
    FragmentListener fragmentListener;
    //    LocationHandler locationHandler;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    Boolean isOffer = false;
    AutoCompleteTextView searchLocation;
    //    LatLng centerOfMap;
    double mapLat, mapLong,a_lat,a_long;
    String catStr = "", subCatStr = "";
    UserData userData;
    ArrayList<FoodCategory> categoryArrayList = new ArrayList<>();
    //    ArrayList<Trolly> trollyArrayList = new ArrayList<>();
    ArrayList<TrollyModel> trollyArrayList = new ArrayList<>();
    FoodCategoryAdapter foodCategoryAdapter;
    ProgressDialog pDialog;
    final int SUB_CAT_REQUEST = 105;
    private SearchView searchView;
    List<BannerData> bannerData = new ArrayList<>();
    String bannerUrl = "banner";
    ViewPager viewPager_banner;
    private static int currentPage = 0;
    CirclePageIndicator indicator;
    TextView norec;
    EditText search_et;
    ImageView search_btn;
    boolean isLast = false;
    String key;
    boolean btn_search_click = false;
    private ArrayList<Data_List> anoop_dataArray;
    private String cat_id,category_name,sub_category_name,sub_id,sub_category_image;
    private TextView cat_name_tv;
    private RecyclerView rv_Item_category;
    List<CategoryData> categoryData = new ArrayList<>();

    LocationManager locationManager;

    private PlaceArrayAdapter mPlaceArrayAdapter;
    private static final LatLngBounds BOUNDS_MOUNTAIN_VIEW = new LatLngBounds(
            new LatLng(37.398160, -122.180831), new LatLng(37.430610, -121.972090));


    FoodCategoryAdapter.FoodCategoryAdapterListener foodCategoryAdapterListener = new FoodCategoryAdapter.FoodCategoryAdapterListener() {
        @Override
        public void onSelect(FoodCategory foodCategory, int index) {
            CommonUtill.printClassData(foodCategory);
            CommonUtill.print("isIs_promotional ==" + foodCategory.isIs_promotional());
            if (foodCategory.isIs_promotional() != null && foodCategory.isIs_promotional().equalsIgnoreCase("yes")) {
                catStr = foodCategory.getId();
                subCatStr = "";
                showOfferDialog(foodCategory);
            } else {
                isOffer = false;
                catStr = foodCategory.getId();
                subCatStr = "";
                searchTrolly();
            }

        }
    };

    private void showOfferDialog(final FoodCategory foodCategory) {
        new OfferDialog(getActivity(), new OfferDialog.ConfirmationDialogListener() {
            @Override
            public void onYesButtonClicked(OfferDialog.confirmTask task) {
//                getOrderDetail();
                Intent intent = new Intent(getActivity(), OfferMenuListActivity.class);
                intent.putExtra("foodCat", foodCategory);
                intent.putExtra("mapLat", mapLat);
                intent.putExtra("mapLong", mapLong);
                startActivity(intent);
            }
            @Override
            public void onNoButtonClicked(OfferDialog.confirmTask task) {
            }
        }, OfferDialog.confirmTask.NONE, "Order now", true);

    }


    ImageView openScanner, openNotification, openHelp;
    LinearLayout dynamic_ll;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        pDialog = CommonUtill.showProgressDialog(getActivity(), "Please wait...");
        mainView = inflater.inflate(R.layout.trolly_search_fragment, container, false);
        openScanner = mainView.findViewById(R.id.openScanner);
        openNotification = mainView.findViewById(R.id.openNotification);
        openHelp = mainView.findViewById(R.id.openHelp);
        viewPager_banner = (ViewPager) mainView.findViewById(R.id.viewPager_banner);
        indicator = (CirclePageIndicator) mainView.findViewById(R.id.indicator);
        dynamic_ll=(LinearLayout)mainView.findViewById(R.id.dynamic_ll);
        norec=mainView.findViewById(R.id.norec);
        search_et=mainView.findViewById(R.id.search_et);
        search_btn=mainView.findViewById(R.id.search_btn);
        anoop_dataArray = new ArrayList<Data_List>();

        openScanner.setOnClickListener(this);
        openNotification.setOnClickListener(this);
        openHelp.setOnClickListener(this);
        try {
            userData = (UserData) PrefrenceHandler.getPreferences(getActivity()).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        getBanner();
        getData();
        getNearbyRestaurants();
        getLocation();

//        searchView=(SearchView)mainView.findViewById(R.id.searchView);
//      //  View v = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                getData(query);
//                Toast.makeText(getContext(), "a"+query, Toast.LENGTH_LONG).show();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Toast.makeText(getContext(), "b"+newText, Toast.LENGTH_LONG).show();
//                return false;
//            }
//        });


        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (search_et.getText().toString().trim().equalsIgnoreCase("")) {
                    Toast.makeText(getActivity(), "Please enter text...", Toast.LENGTH_SHORT).show();
                } else {
                    key = search_et.getText().toString().trim();
                    getData();
                    btn_search_click = true;
                 //   search_et.setText("");
               //    dynamic_ll.removeAllViews();

                }
            }
        });


        return mainView;
    }


    public void setAllHeaders() {
        getNearbyRestaurants();
    }

    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            final PlaceArrayAdapter.PlaceAutocomplete item = mPlaceArrayAdapter.getItem(position);
            final String placeId = String.valueOf(item.placeId);
            Log.i("Trolly", "Selected: " + item.description);
//            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
//                    .getPlaceById(mGoogleApiClient, placeId);
//            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
            Log.i("Trolly", "Fetching details for ID: " + item.placeId);
        }
    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
            = new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(PlaceBuffer places) {
            if (!places.getStatus().isSuccess()) {
                Log.e("Trolly", "Place query did not complete. Error: " +
                        places.getStatus().toString());
                return;
            }
            // Selecting the first object buffer.
            final Place place = places.get(0);
            CharSequence attributions = places.getAttributions();

            mapLat = place.getLatLng().latitude;
            mapLong = place.getLatLng().longitude;
            searchTrolly();
//            mNameView.setText(Html.fromHtml(place.getAddress() + ""));

        }
    };


    @Override
    public void onResume() {
        super.onResume();
        CommonUtill.hideSoftKeyboard(getActivity());
    }
    String walletBalance = "0";
    private void searchTrolly() {
    }

    private void checkForOffer() {
        for (int i = 1; i < categoryArrayList.size(); i++) {
            if (categoryArrayList.get(i).isIs_promotional().equalsIgnoreCase("yes")) {
                catStr = categoryArrayList.get(i).getId();
                subCatStr = "";
                isOffer = true;
                showOfferDialog(categoryArrayList.get(i));
                break;
            }
        }
    }

    private void showMessage(String message) {

        new MessageDialog(getActivity(), new MessageDialog.ConfirmationDialogListener() {
            @Override
            public void onYesButtonClicked(MessageDialog.confirmTask task) {
//                getActivity().finish();
//                checkForOffer();
            }

            @Override
            public void onNoButtonClicked(MessageDialog.confirmTask task) {
//                checkForOffer();
            }
        }, MessageDialog.confirmTask.NONE,
                message, getActivity().getResources().getString(R.string.app_name), "No", "OK", false, true);

    }


    private void drawTrollyOnMap() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getBanners();
    }

    /**
     * @param view   is custom marker layout which we will convert into bitmap.
     * @param bitmap is the image which you want to show in marker.
     * @return
     */
    private Bitmap getMarkerBitmapFromView(View view, Bitmap bitmap) {

        ImageView mMarkerImageView = (ImageView) view.findViewById(R.id.markerPic);
        mMarkerImageView.setImageBitmap(bitmap);
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == getActivity().RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getActivity(), data);
                CommonUtill.print("Place ==" + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getActivity(), data);
                // TODO: Handle the error.
                CommonUtill.print("Status ==" + status.getStatusMessage());

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                // The user canceled the operation.
            }
        } else if (requestCode == SUB_CAT_REQUEST && resultCode == getActivity().RESULT_OK) {

            catStr = data.getStringExtra("CatId");
            subCatStr = data.getStringExtra("subCatId");
            searchTrolly();


        } else if (requestCode == SELECT_ADDRESS && resultCode == RESULT_OK && data != null) {
            CommonUtill.print("SELECT_ADDRESS ==" + data.getStringExtra("address"));
//            addTxt.setText(data.getStringExtra("address"));
            CommonUtill.printClassData(data.getSerializableExtra("foodCat"));
            Intent intent = new Intent(getActivity(), OfferMenuListActivity.class);
            intent.putExtra("foodCat", data.getSerializableExtra("foodCat"));
            intent.putExtra("address", data.getStringExtra("address"));
            intent.putExtra("name", data.getStringExtra("name"));
            intent.putExtra("mobile", data.getStringExtra("mobile"));
            intent.putExtra("email", data.getStringExtra("email"));
            intent.putExtra("street", data.getStringExtra("street"));
            intent.putExtra("city", data.getStringExtra("city"));
            intent.putExtra("state", data.getStringExtra("state"));
            intent.putExtra("country", data.getStringExtra("country"));
            intent.putExtra("pin", data.getStringExtra("pin"));
            intent.putExtra("mapLat", mapLat);
            intent.putExtra("mapLong", mapLong);
            startActivity(intent);


        }


    }

    void getNearbyRestaurants() {
        try {

            String otp = new GlobalAppApis().nearbyRestaurants("");
            ApiService client = API_Config.getApiClient_ByPost();
            Call<String> call = client.getRestaurantList(otp);
            new ConnectToRetrofit(new RetrofitCallBackListenar() {
                @Override
                public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                    JSONObject jsonObject = new JSONObject(result);
                    if (jsonObject.getBoolean("status")) {
                        if (jsonObject.has("data") && jsonObject.getJSONArray("data").length() > 0) {
                            try {
                                if (jsonObject.getBoolean("status")) {

                                    Gson gson = new Gson();
                                    JsonElement element = gson.fromJson(jsonObject.getJSONArray("data").toString(), JsonElement.class);

                                    Type listType = new TypeToken<ArrayList<TrollyModel>>() {
                                    }.getType();

                                    trollyArrayList = gson.fromJson(element, listType);
                                    drawTrollyOnMap();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please try again.", Toast.LENGTH_SHORT).show();

                        }
                    } else {
                    }
                }

            }, getActivity(), call, "", true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.openScanner:
                if (Constants.isUserLoggedIn(getActivity())) {
                    startActivity(new Intent(getActivity(), MoneyTransferActivity.class)
                            .putExtra("walletBalance", walletBalance)
                    );
                } else
                    startActivity(new Intent(getActivity(), MobileActivity.class));
                break;
            case R.id.openNotification:
                Toast.makeText(getActivity(), "Coming Soon..!!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.openHelp:
                CommonUtill.openBrowser(getActivity(), "http://sunriseinfraventure.com/whs/");
                break;
            case R.id.passbookLL:
                if (Constants.isUserLoggedIn(getActivity()))
                    startActivity(new Intent(getActivity(), TransactionHistory.class));
                else
                    startActivity(new Intent(getActivity(), MobileActivity.class));
                break;
//            case R.id.walletLL:
//                if (Constants.isUserLoggedIn(getActivity()))
//                    startActivity(new Intent(getActivity(), UserWallet.class));
//                else
//                    startActivity(new Intent(getActivity(), MobileActivity.class));
//                break;  case R.id.whatsappLL:
//                openWhatsappContact();
//                break;
//            case R.id.contactLL:
//                startActivity(new Intent(getActivity(), ReachUs.class));
//                break;
//            case R.id.rechargeLayout:
//            case R.id.dthLayout:
//            case R.id.electricLayout:
//            case R.id.moreLayout:
//                Toast.makeText(getActivity(), "Coming Soon..!!", Toast.LENGTH_SHORT).show();
//                break;
        }
    }

    String whatsappNumber = "";
    String whatsappMsg = "";

    void openWhatsappContact() {
        try {
            PackageManager packageManager = getActivity().getPackageManager();
            Intent i = new Intent(Intent.ACTION_VIEW);

            try {
                String url = "https://api.whatsapp.com/send?phone=" + whatsappNumber
                        + "&text=" + URLEncoder.encode(whatsappMsg, "UTF-8");
                i.setPackage("com.whatsapp");
                i.setData(Uri.parse(url));
                if (i.resolveActivity(packageManager) != null) {
                    getActivity().startActivity(i);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void getBanner() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.GET,BASE_URL+"customer/"+"banner-list", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("status").equalsIgnoreCase("True")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        String JsonInString = jsonObject1.getString("banner");
                        bannerData = BannerData.createJsonInList(JsonInString);
                    }
                    viewPager_banner.setAdapter(new AdapterForBanner(getActivity(), bannerData));
                    indicator.setViewPager(viewPager_banner);
                    final float density = getResources().getDisplayMetrics().density;
//Set circle indicator radius
                    indicator.setRadius(2 * density);
                    // Auto start of viewpager
                    final Handler handler = new Handler();
                    final Runnable Update = new Runnable() {
                        public void run() {
                            if (currentPage == bannerData.size()) {
                                currentPage = 0;
                            }
                            viewPager_banner.setCurrentItem(currentPage++, true);
                        }
                    };
                    Timer swipeTimer = new Timer();
                    swipeTimer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            handler.post(Update);
                        }
                    }, 5000, 3000);
// Pager listener over indicator
                    indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageSelected(int position) {
                            currentPage = position;
                        }
                        @Override
                        public void onPageScrolled(int pos, float arg1, int arg2) {
                        }
                        @Override
                        public void onPageScrollStateChanged(int pos) {
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //  Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
            }
        }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "Please check your Internet Connection! try again...", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }



    public void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.show();
        progressDialog.setMessage("Loading");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, BASE_URL+"customer/"+"subcategory_mainpage", new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (btn_search_click) {
                        anoop_dataArray.clear();
                        dynamic_ll.removeAllViews();
                        btn_search_click = false;
                    }
                    if (jsonObject.getBoolean("status")) {
                        JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                        if (jsonObject1 != null) {
                            String listInString = jsonObject1.getString("category");
                            //   Log.d("res","res"+listInString);
                            if (jsonObject1.has("category")) {
                                JSONArray my_noti_Array = jsonObject1.getJSONArray("category");
                                for (int i = 0; i < my_noti_Array.length(); i++) {
                                    JSONObject jsonObject11 = my_noti_Array.getJSONObject(i);
                                    cat_id = jsonObject11.getString("id");
                                    category_name = jsonObject11.getString("category_name");
                                    String category_image = jsonObject11.getString("category_image");
                                    String subcat = jsonObject11.getString("subcategory");
                                    categoryData = CategoryData.createJsonInList(subcat);
                                    if (jsonObject11.has("subcategory")) {
                                        JSONArray subcat_Array = jsonObject11.getJSONArray("subcategory");
                                        for (int j = 0; j < subcat_Array.length(); j++) {
                                            JSONObject subcat_jsonObject = subcat_Array.getJSONObject(j);
                                            sub_id = subcat_jsonObject.getString("id");
                                            sub_category_name = subcat_jsonObject.getString("category_name");
                                            sub_category_image = subcat_jsonObject.getString("category_image");
                                        }

                                        Data_List noti_screen_lists = new Data_List(cat_id, category_name, sub_id, sub_category_name, sub_category_image);
                                        anoop_dataArray.add(noti_screen_lists);
                                        getDataList(i);
                                    }
                                }
                            }
                        }
                        else {
                            norec.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                // Toast.makeText(ActivityProduct.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                if (btn_search_click) {
                    params.put("search_keyword", key);
                    params.put("user_id",userData.getUserId());
                } else {
                    params.put("user_id", userData.getUserId());
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    //Latest_add_Screen function to set data in data array(dataarray1)layout
    private void getDataList(final int i) {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
        View convertView = inflater.inflate(R.layout.dynamic_dashboard, null);
        dynamic_ll.addView(convertView);
        cat_name_tv=(TextView)convertView.findViewById(R.id.cat_name_tv);
        RelativeLayout add_rl=(RelativeLayout)convertView.findViewById(R.id.add_rl);
        rv_Item_category=(RecyclerView)convertView.findViewById(R.id.rv_Item_category);

      //  GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
        AdapterForCategoryHome myAdapter = new AdapterForCategoryHome(getActivity(), categoryData);
        rv_Item_category.setLayoutManager(gridLayoutManager);
        rv_Item_category.setAdapter(myAdapter);
        rv_Item_category.setHasFixedSize(true);
        cat_name_tv.setText(anoop_dataArray.get(i).getCategory_name());

        cat_name_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(),RestaurantList.class);
                intent.putExtra("latitude", a_lat);
                intent.putExtra("longitude", a_long);
                intent.putExtra("cat_name",anoop_dataArray.get(i).getCategory_name());
                intent.putExtra("id", anoop_dataArray.get(i).getCat_id());
                intent.putExtra("from", "cat");
                //intent.putExtra("fromWhere", anoop_dataArray.get(i).getCategory_name());
                startActivity(intent);
            }
        });

        if (anoop_dataArray.get(i).getCategory_name().equalsIgnoreCase("Fruits")){
            add_rl.setVisibility(View.VISIBLE);
        }
        View view = new View(getActivity());
        LinearLayout.LayoutParams params_view = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 10);
        view.setLayoutParams(params_view);
        dynamic_ll.addView(view);
    }





    void getLocation() {
        try {
            locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        a_lat=location.getLatitude();
        a_long=location.getLongitude();
       // Toast.makeText(getActivity(),""+a_lat+a_long,Toast.LENGTH_LONG).show();
      //  locationText.setText("Current Location: " + location.getLatitude() + ", " + location.getLongitude());
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(getContext(), "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }
}


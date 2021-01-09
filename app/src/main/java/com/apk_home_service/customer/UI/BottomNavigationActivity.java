package com.apk_home_service.customer.UI;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.custom.MessageDialog;
import com.apk_home_service.customer.UI.fragment.CategoryFragment;
import com.apk_home_service.customer.UI.fragment.OfferFragment;
import com.apk_home_service.customer.UI.fragment.OrderListFragment;
import com.apk_home_service.customer.UI.fragment.SettingsFragment;
import com.apk_home_service.customer.UI.fragment.TrollySearchFragment;
import com.apk_home_service.customer.Wallet.ApiService;
import com.apk_home_service.customer.Wallet.ConnectToRetrofit;
import com.apk_home_service.customer.Wallet.GlobalAppApis;
import com.apk_home_service.customer.Wallet.RetrofitCallBackListenar;
import com.apk_home_service.customer.data.Constants;
import com.apk_home_service.customer.data.LocationService;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.utill.CommonUtill;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;

import static com.apk_home_service.customer.Wallet.API_Config.getApiClient_ByPost;

public class BottomNavigationActivity extends AppCompatActivity {
    public AHBottomNavigationViewPager viewPager;
    public AHBottomNavigation bottomNavigation;
    private ArrayList<AHBottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    TrollySearchFragment trollySearchFragment;// = new LandingFragment();
    CategoryFragment categoryFragment;// = new NotificationFragment();
    OrderListFragment orderListFragment;// = new ProfileFragment();
    SettingsFragment settingsFragment;// = new SettingsFragment();
    OfferFragment offerFragment;// = new WishListFragment();
    ViewPagerAdapter adapter;
    private Fragment currentFragment;
    Activity mActivity;
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
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        mActivity = this;
        initUI();
        checkIfOrderBooked();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkLocationPermission();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startService(new Intent(mActivity, LocationService.class));
                }
            }, 100);
        }
    }


    public void getGeneralSettings() {
        ApiService client = getApiClient_ByPost();
        String otp = "";
        String userId = "";
        try {
            UserData userData;
            userData = (UserData) PrefrenceHandler.getPreferences(mActivity).getObjectValue(new UserData());
            userId = userData.getUserId();
            otp = GlobalAppApis.getUserID(userId);
        } catch (Exception e) {
            e.printStackTrace();
            otp = "";
        }

        Call<String> call = client.getGeneralSettings(otp);
        new ConnectToRetrofit(new RetrofitCallBackListenar() {
            @Override
            public void RetrofitCallBackListenar(String result, String action) throws JSONException {
                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.getBoolean("status")) {
//                    Constants.savePreferences(mActivity,"qrcode",jsonObject.getJSONObject("data").getString("qrcode"));
//                    Constants.savePreferences(mActivity,"qimage",jsonObject.getJSONObject("data").getString("qrcode_image"));
                    Constants.storeGeneralSettings(mActivity, jsonObject);
                } else {

                }

            }
        }, mActivity, call, "", false);


    }

    void checkIfOrderBooked() {
        try {
            if (getIntent().hasExtra("order_id")) {
                startActivity(new Intent(this, BookingDetails.class)
                        .putExtra("order_id", getIntent().getStringExtra("order_id")));

            } else {
//                showPopup();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    void showPopup() {
        try {
            JSONObject mObject = new JSONObject(com.apk_home_service.customer.data.Constants.getGeneralSettings(mActivity));
            if (mObject.getJSONObject("data").has("welcome_msg") && mObject.getJSONObject("data").getJSONObject("welcome_msg").getString("want_to_show").equalsIgnoreCase("yes")) {
                new MessageDialog(mActivity, new MessageDialog.ConfirmationDialogListener() {
                    @Override
                    public void onYesButtonClicked(MessageDialog.confirmTask task) {
                    }

                    @Override
                    public void onNoButtonClicked(MessageDialog.confirmTask task) {

                    }
                }, MessageDialog.confirmTask.NONE,
                        mObject.getJSONObject("data").getJSONObject("welcome_msg").getString("welcome_msg"), mActivity.getResources().getString(R.string.app_name), "", "Close", false, true);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getGeneralSettings();
        CommonUtill.hideSoftKeyboard(mActivity);

    }

    private void initUI() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        }

        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPager = findViewById(R.id.view_pager);


        AHBottomNavigationItem item1 = new AHBottomNavigationItem(R.string.tab_1, R.drawable.order_black_24dp, R.color.colorBlack);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(R.string.tab_2, R.drawable.ic_local_offer_black_24dp, R.color.colorBlack);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Home", R.drawable.ic_home_black_24dp , R.color.colorBlack);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(R.string.tab_4, R.drawable.ic_view_list_black_24dp, R.color.colorBlack);
        AHBottomNavigationItem item5 = new AHBottomNavigationItem(R.string.tab_5, R.drawable.ic_settings_black_24dp, R.color.colorBlack);

        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);
        bottomNavigationItems.add(item4);
        bottomNavigationItems.add(item5);
        bottomNavigation.addItems(bottomNavigationItems);
        AHBottomNavigation.TitleState titleState = AHBottomNavigation.TitleState.valueOf("ALWAYS_SHOW");
        bottomNavigation.setTitleState(titleState);
        bottomNavigation.setTranslucentNavigationEnabled(true);
        //   bottomNavigation.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.footer));
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#46a049"));
        bottomNavigation.setAccentColor(Color.parseColor("#EB6326"));
        bottomNavigation.setInactiveColor(Color.parseColor("#FFFFFF"));
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                viewPager.setCurrentItem(position, false);
                if (position == 0) {
                    try {
//                        orderListFragment.showProfile();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (position == 1) {
                    try {
                        if(Constants.isUserLoggedIn(mActivity))
                        offerFragment.getAvailableCoupons();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (position == 3) {
                    try {

//                        categoryFragment.loadContent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

else if (position == 2) {
                    try {
//trollySearchFragment.setAllHeaders();
//trollySearchFragment.getBanners();
//                        categoryFragment.loadContent();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                return true;
            }
        });


        viewPager.setOffscreenPageLimit(4);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        currentFragment = adapter.getCurrentFragment();
        showOrHideBottomNavigation(true);
        bottomNavigation.setCurrentItem(2);
    }

    public void showOrHideBottomNavigation(boolean show) {
        if (show) {
            bottomNavigation.restoreBottomNavigation(true);
        } else {
            bottomNavigation.hideBottomNavigation(true);
        }
    }

    public class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private ViewPagerAdapter adapter;
//        private ArrayList<Fragment> fragments = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);

//            fragments.clear();
//            fragments.add(orderListFragment);
//            fragments.add(offerFragment);
//            fragments.add(trollySearchFragment);
//            fragments.add(categoryFragment);
//            fragments.add(settingsFragment);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (orderListFragment == null)
                        orderListFragment = new OrderListFragment();
                    return orderListFragment;
                case 1:
                    if (offerFragment == null)
                        offerFragment = new OfferFragment();
                    return offerFragment;
                case 2:
                    if (trollySearchFragment == null)
                        trollySearchFragment = new TrollySearchFragment();
                    return trollySearchFragment;
                case 3:
                    if (categoryFragment == null)
                        categoryFragment = new CategoryFragment();
                    return categoryFragment;
                case 4:
                    if (settingsFragment == null)
                        settingsFragment = new SettingsFragment();
                    return settingsFragment;
            }
            settingsFragment = new SettingsFragment();
            return settingsFragment;

        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            if (getCurrentFragment() != object) {
                currentFragment = ((Fragment) object);
            }
            super.setPrimaryItem(container, position, object);
        }

        /**
         * Get the current fragment
         */
        public Fragment getCurrentFragment() {
            return currentFragment;
        }
    }


    private void showMessage(String message) {

        new MessageDialog(mActivity, new MessageDialog.ConfirmationDialogListener() {
            @Override
            public void onYesButtonClicked(MessageDialog.confirmTask task) {
                finish();
            }

            @Override
            public void onNoButtonClicked(MessageDialog.confirmTask task) {

            }
        }, MessageDialog.confirmTask.NONE,
                message, mActivity.getResources().getString(R.string.app_name), "No", "Yes", true, true);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        showMessage("Do you want to exit?");
    }
}

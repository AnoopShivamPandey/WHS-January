package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.custom.MessageDialog;
import com.apk_home_service.customer.UI.fragment.OrderListFragment;
import com.apk_home_service.customer.UI.fragment.TrollySearchFragment;
import com.apk_home_service.customer.UI.fragment.UserProfileFragment;
import com.apk_home_service.customer.controll.CircleTransform;
import com.apk_home_service.customer.controll.FragmentListener;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.Constants;

import org.json.JSONObject;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawer;
    boolean isDrawerOpen;
    UserData userData;
    Activity mActivity;
    LinearLayout qrButtonLL;
    FragmentListener fragmentListener = new FragmentListener() {
        @Override
        public void addFragment(Fragment newFragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, newFragment).addToBackStack(newFragment.getClass().getName()).commit();
        }

        @Override
        public void popFragment() {
            getSupportFragmentManager().popBackStack();
        }
        @Override
        public void popFragment(String tag) {
        }
        @Override
        public void removeFragment(String tag) {
        }

        @Override
        public void fragmentCallBack(Bundle callData, String tag, callType callType) {
            if (tag.equalsIgnoreCase(TrollySearchFragment.class.getName())) {
                manageTrollySearchFragment(callData, callType);
            } else if (tag.equalsIgnoreCase(UserProfileFragment.class.getName())) {
                manageUserProfileFragment(callData, callType);
            } else if (tag.equalsIgnoreCase(OrderListFragment.class.getName())) {
                manageOrderListFragment(callData, callType);
            }
        }
    };
    TextView signoutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        imageView = (ImageView) findViewById(R.id.userPic);
        qrButtonLL = (LinearLayout) findViewById(R.id.qrButtonLL);
        mActivity = this;
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        signoutText = findViewById(R.id.signoutText);
        try {
            userData = (UserData) PrefrenceHandler.getPreferences(this).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        showPopup();
        setDrawer();
        setClicking();
        CommonUtill.print("API_TOKEN ==" + PrefrenceHandler.getPreferences(this).getAPI_TOKEN());

        qrButtonLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, QRScreen.class));
                handleDrawer();
            }
        });
        TrollySearchFragment trollySearchFragment = new TrollySearchFragment();
//        trollySearchFragment.setFragmentListener(fragmentListener);
        fragmentListener.addFragment(trollySearchFragment);
        checkIfOrderBooked();
    }


    void checkIfOrderBooked() {
        try {
            if (getIntent().hasExtra("order_id")) {
                startActivity(new Intent(this, BookingDetails.class)
                        .putExtra("order_id", getIntent().getStringExtra("order_id")));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (Constants.isUserLoggedIn(mActivity))
            hideDrawerDetails(false);
        else
            hideDrawerDetails(true);

    }

    void hideDrawerDetails(boolean hide) {
        if (hide) {
            findViewById(R.id.profile).setVisibility(View.GONE);
            findViewById(R.id.booking).setVisibility(View.GONE);
            findViewById(R.id.bookingView).setVisibility(View.GONE);
            findViewById(R.id.qrButtonLL).setVisibility(View.GONE);
            findViewById(R.id.qrView).setVisibility(View.GONE);
            signoutText.setText("Login");
            Glide.with(this)
                    .load(R.drawable.logo)
//                    .error(R.drawable.user_image)
                    .crossFade()
                    .centerCrop()
                    .transform(new CircleTransform(this))
                    .into(imageView);

        } else {
            findViewById(R.id.profile).setVisibility(View.VISIBLE);
            findViewById(R.id.booking).setVisibility(View.VISIBLE);
            findViewById(R.id.bookingView).setVisibility(View.VISIBLE);
            findViewById(R.id.qrButtonLL).setVisibility(View.VISIBLE);
            findViewById(R.id.qrView).setVisibility(View.VISIBLE);
            signoutText.setText("Signout");

        }
    }

    ImageView imageView;

    private void handleDrawerProfile() {
        TextView nameTxt = (TextView) findViewById(R.id.nameTxt);
        TextView emailTxt = (TextView) findViewById(R.id.emailTxt);

        try {
            userData = (UserData) PrefrenceHandler.getPreferences(this).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Glide.with(this)
                    .load(userData.getUserPic())
                    .error(R.drawable.logo)
                    .crossFade()
                    .centerCrop()
                    .transform(new CircleTransform(this))
                    .into(imageView);
            nameTxt.setText(userData.getUserName());
            emailTxt.setText(userData.getEmailId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setClicking() {
        findViewById(R.id.dashboard).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDrawer();
                TrollySearchFragment trollySearchFragment = new TrollySearchFragment();
//                trollySearchFragment.setFragmentListener(fragmentListener);
                fragmentListener.addFragment(trollySearchFragment);
            }
        });
        findViewById(R.id.booking).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDrawer();
                OrderListFragment trollySearchFragment = new OrderListFragment();
                trollySearchFragment.setFragmentListener(fragmentListener);
                fragmentListener.addFragment(trollySearchFragment);
            }
        });
        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDrawer();
                UserProfileFragment userProfileFragment = new UserProfileFragment();
                userProfileFragment.setFragmentListener(fragmentListener);
                fragmentListener.addFragment(userProfileFragment);
            }
        });
//        findViewById(R.id.setting).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
        findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtill.shareApp(mActivity, "Hey check out a new Home Service app", "com.apk_home_service.customer");
            }
        });
        findViewById(R.id.rateUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtill.downloadApp(mActivity, "com.apk_home_service.customer");
            }
        });
//        findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CommonUtill.openBrowser(mActivity, "http://office.a2hosted.com/myTreat/public/help");
//            }
//        });
        findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Constants.isUserLoggedIn(mActivity)) {
                    Intent intent = new Intent(mActivity, MobileActivity.class);
                    startActivity(intent);
                } else {
                    new MessageDialog(mActivity, new MessageDialog.ConfirmationDialogListener() {
                        @Override
                        public void onYesButtonClicked(MessageDialog.confirmTask task) {
                            PrefrenceHandler.getPreferences(mActivity).setLogout();
                            Constants.savePreferences(mActivity, Constants.USER_ID, "");
                            startActivity(new Intent(mActivity, MobileActivity.class));
                            finish();
                        }
                        @Override
                        public void onNoButtonClicked(MessageDialog.confirmTask task) {
                        }
                    }, MessageDialog.confirmTask.NONE,
                            "Do you want to exit?", mActivity.getResources().getString(R.string.app_name), "No", "Yes", true, true);
                }
            }

        });
    }


    void showPopup() {
        try {
            JSONObject mObject = new JSONObject(com.apk_home_service.customer.data.Constants.getGeneralSettings(mActivity));
            if (mObject.getJSONObject("data").has("welcome_msg") && mObject.getJSONObject("data").getJSONObject("welcome_msg").getString("want_to_show").equalsIgnoreCase("yes")) {
                new MessageDialog(mActivity, new MessageDialog.ConfirmationDialogListener() {
                    @Override
                    public void onYesButtonClicked(MessageDialog.confirmTask task) {
                        finish();
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

    private void setDrawer() {

        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this, drawer,
                null, R.string.app_name, R.string.app_name) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
                isDrawerOpen = false;
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
                handleDrawerProfile();
                isDrawerOpen = true;
            }
        };
        drawer.setDrawerListener(mDrawerToggle);
    }


    private void manageTrollySearchFragment(Bundle callData, FragmentListener.callType callType) {
        if (callType == FragmentListener.callType.MENU_CLICK) {
            handleDrawer();
        }

    }

    private void manageUserProfileFragment(Bundle callData, FragmentListener.callType callType) {
        if (callType == FragmentListener.callType.MENU_CLICK) {
            handleDrawer();
        }
    }

    private void manageOrderListFragment(Bundle callData, FragmentListener.callType callType) {
        if (callType == FragmentListener.callType.MENU_CLICK) {
            handleDrawer();
        }
    }


    private void handleDrawer() {

        if (isDrawerOpen) {
            closeDrawer();
        } else {
            openDrawer();
        }
    }


    public void closeDrawer() {
        CommonUtill.print("closeDrawer ==");

        drawer.closeDrawer(Gravity.LEFT);
    }

    public void openDrawer() {
        CommonUtill.print("closeDrawer ==");

        drawer.openDrawer(Gravity.LEFT);
    }

    @Override
    public void onBackPressed() {
        showMessage("Do you want to exit?");

//        if (isDrawerOpen) {
//            closeDrawer();
//        } else {
////            finish();
//            if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
//                fragmentListener.popFragment();
//            } else {
//                showMessage("Do you want to exit?");
//            }
//        }
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


}

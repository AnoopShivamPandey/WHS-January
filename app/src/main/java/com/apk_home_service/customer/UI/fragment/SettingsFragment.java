package com.apk_home_service.customer.UI.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apk_home_service.ViewAppoitmentList;
import com.bumptech.glide.Glide;
import com.apk_home_service.TransactionHistory;
import com.apk_home_service.customer.R;
import com.apk_home_service.customer.UI.MobileActivity;
import com.apk_home_service.customer.UI.QRScreen;
import com.apk_home_service.customer.UI.ReachUs;
import com.apk_home_service.customer.UI.UserProfile;
import com.apk_home_service.customer.UI.WebViewActivity;
import com.apk_home_service.customer.UI.custom.MessageDialog;
import com.apk_home_service.customer.controll.CircleTransform;
import com.apk_home_service.customer.data.PrefrenceHandler;
import com.apk_home_service.customer.modal.UserData;
import com.apk_home_service.customer.utill.CommonUtill;
import com.apk_home_service.customer.utill.Constants;

import org.json.JSONObject;

import java.net.URLEncoder;

import static com.apk_home_service.customer.Wallet.Urls.BASE_URL;


public class SettingsFragment extends Fragment {

    private LinearLayout appoitment;

    public SettingsFragment() {
        // Required empty public constructor
    }

    Activity mActivity;
    View mView;
    TextView signoutText;
    ImageView userPic;
    LinearLayout contactUs, passbookLL;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = getActivity();
        mView = inflater.inflate(R.layout.fragment_settings, container, false);
        signoutText = mView.findViewById(R.id.signoutText);
       // contactOnWhatsapp = mView.findViewById(R.id.contactOnWhatsapp);
        contactUs = mView.findViewById(R.id.contactUs);
        passbookLL = mView.findViewById(R.id.passbookLL);
        userPic = mView.findViewById(R.id.userPic);
        appoitment=mView.findViewById(R.id.appoitment);

        appoitment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, ViewAppoitmentList.class));

            }
        });

//        contactOnWhatsapp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openWhatsappContact();
//            }
//        });
        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, ReachUs.class));
            }
        }); passbookLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, TransactionHistory.class));
            }
        });
        setData();
        setClick();
        return mView;
    }

    String whatsappNumber = "";
    String whatsappMsg = "";

    void setData() {
        try {
            JSONObject mJsonObject = new JSONObject(com.apk_home_service.customer.data.Constants.getGeneralSettings(mActivity));
            whatsappNumber = mJsonObject.getJSONObject("data").getJSONObject("contact").getString("whatsapp");
            whatsappMsg = mJsonObject.getJSONObject("data").getJSONObject("contact").getString("message");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.isUserLoggedIn(mActivity))
            hideDrawerDetails(false);
        else
            hideDrawerDetails(true);
    }

    void hideDrawerDetails(boolean hide) {
        if (hide) {
            mView.findViewById(R.id.profile).setVisibility(View.GONE);
            mView.findViewById(R.id.qrButtonLL).setVisibility(View.GONE);
            mView.findViewById(R.id.qrView).setVisibility(View.GONE);
            mView.findViewById(R.id.share).setVisibility(View.GONE);
            mView.findViewById(R.id.shareView).setVisibility(View.GONE);
            mView.findViewById(R.id.passbookLL).setVisibility(View.GONE);
            mView.findViewById(R.id.passbookView).setVisibility(View.GONE);
            signoutText.setText("Login");
            Glide.with(this)
                    .load(R.drawable.doctor)
//                    .error(R.drawable.user_image)
                    .crossFade()
                    .centerCrop()
                    .transform(new CircleTransform(mActivity))
                    .into(userPic);

        } else {
            mView.findViewById(R.id.profile).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.qrButtonLL).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.qrView).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.share).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.shareView).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.passbookLL).setVisibility(View.VISIBLE);
            mView.findViewById(R.id.passbookView).setVisibility(View.VISIBLE);
            signoutText.setText("Signout");
            handleDrawerProfile();

        }
    }

    UserData userData;

    private void handleDrawerProfile() {
        TextView nameTxt = (TextView) mView.findViewById(R.id.nameTxt);
        TextView emailTxt = (TextView) mView.findViewById(R.id.emailTxt);

        try {
            userData = (UserData) PrefrenceHandler.getPreferences(mActivity).getObjectValue(new UserData());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        try {
            Glide.with(this)
                    .load(userData.getUserPic())
                    .error(R.drawable.logo)
                    .crossFade()
                    .centerCrop()
                    .transform(new CircleTransform(getActivity()))
                    .into(userPic);
            nameTxt.setText(userData.getUserName());
            emailTxt.setText(userData.getEmailId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void setClick() {
        mView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtill.shareApp(mActivity, "Hey check out a new BiziBeri app with my referral code (" + userData.getReferralCode() + ") and get welcome amount", "com.apk_home_service.customer");
            }
        });   mView.findViewById(R.id.privacyPolicyUrl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
startActivity(new Intent(mActivity,WebViewActivity.class));
            }
        });
        mView.findViewById(R.id.rateUs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtill.downloadApp(mActivity, "com.apk_home_service.customer");
            }
        });
        mView.findViewById(R.id.help).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtill.openBrowser(mActivity, "http://biziberi.com/help");
            }
        });
        mView.findViewById(R.id.qrButtonLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, QRScreen.class));
            }
        });

        mView.findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mActivity, UserProfile.class));
            }
        });
        mView.findViewById(R.id.signout).setOnClickListener(new View.OnClickListener() {
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
                            mActivity.finish();
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

//    void openWhatsappContact() {
//        try {
//            PackageManager packageManager = mActivity.getPackageManager();
//            Intent i = new Intent(Intent.ACTION_VIEW);
//
//            try {
//                String url = "https://api.whatsapp.com/send?phone=" + whatsappNumber
//                        + "&text=" + URLEncoder.encode(whatsappMsg, "UTF-8");
//                i.setPackage("com.whatsapp");
//                i.setData(Uri.parse(url));
//                if (i.resolveActivity(packageManager) != null) {
//                    mActivity.startActivity(i);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}

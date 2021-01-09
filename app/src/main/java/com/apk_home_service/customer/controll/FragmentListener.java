package com.apk_home_service.customer.controll;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Rajnish on 9/6/2017.
 */

public interface FragmentListener {
    public enum callType {NONE,MENU_CLICK};

    public void addFragment(Fragment newFragment);

    public void popFragment();

    public void popFragment(String tag);

    public void removeFragment(String tag);

    public void fragmentCallBack(Bundle callData, String tag, callType callType);
}

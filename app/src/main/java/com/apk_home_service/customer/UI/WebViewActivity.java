package com.apk_home_service.customer.UI;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.apk_home_service.customer.R;

public class WebViewActivity extends Activity {

    private WebView webView;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_);

        webView = (WebView) findViewById(R.id.webView1);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://sunriseinfraventure.com/serviceprovider/public/privacy-policy");
    }
}
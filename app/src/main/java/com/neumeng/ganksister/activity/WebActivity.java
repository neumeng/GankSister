package com.neumeng.ganksister.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.neumeng.ganksister.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebActivity extends AppCompatActivity {
    private static final String ARG_URL = "URL";
    private String mUrl;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.webView)
    WebView mWebView;
    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.putExtra(ARG_URL, url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        parseIntent(getIntent());
        initActionBar();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAppCacheEnabled(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setSupportZoom(true);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());

        mWebView.loadUrl(mUrl);
    }

    private void initActionBar() {
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }
    }

    private void parseIntent(Intent intent) {
        mUrl = intent.getStringExtra(ARG_URL);
    }
//    private class ChromeClient extends WebChromeClient {
//
//        @Override public void onProgressChanged(WebView view, int newProgress) {
//            super.onProgressChanged(view, newProgress);
////            mProgressbar.setProgress(newProgress);
////            if (newProgress == 100) {
////                mProgressbar.setVisibility(View.GONE);
////            } else {
////                mProgressbar.setVisibility(View.VISIBLE);
////            }
//        }
//
//
////        @Override public void onReceivedTitle(WebView view, String title) {
////            super.onReceivedTitle(view, title);
////            setTitle(title);
////        }
//    }
//
//    private class LoveClient extends WebViewClient {
//
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            if (url != null) view.loadUrl(url);
//            return true;
//        }
//    }
}

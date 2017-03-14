package com.doyouevenplank.android.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;
import com.doyouevenplank.android.app.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OpenSourceAttributionsActivity extends DoYouEvenPlankActivity {

    @BindView(R.id.web_view) WebView mWebView;

    public static void start(Context caller) {
        Intent intent = new Intent(caller, OpenSourceAttributionsActivity.class);
        caller.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_source_attributions);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(R.string.open_source_attributions);

        mWebView.getSettings().setJavaScriptEnabled(true); // shares need Javascript enabled to load
        mWebView.getSettings().setBuiltInZoomControls(true); // why the hell not
        mWebView.loadUrl(Config.OPEN_SOURCE_ATTRIBUTIONS_SHARE_LINK);
    }

}

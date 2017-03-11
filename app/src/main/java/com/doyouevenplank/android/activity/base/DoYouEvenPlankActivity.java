package com.doyouevenplank.android.activity.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.doyouevenplank.android.util.NetworkUtils;

/**
 * All activities should extend from this. Encapsulates:
 * - AppCompatActivity
 * - Throwing up RequiresNetworkActivity if no network
 */

public class DoYouEvenPlankActivity extends AppCompatActivity {

    private NetworkStatusBroadcastReceiver mNetworkStatusBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNetworkStatusBroadcastReceiver = new NetworkStatusBroadcastReceiver();
    }

    @Override
    protected void onStart() {
        super.onStart();

        registerReceiver(mNetworkStatusBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(mNetworkStatusBroadcastReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mNetworkStatusBroadcastReceiver = null;
    }

    public class NetworkStatusBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            checkConnection(context);
        }

        private void checkConnection(Context context) {
            if (NetworkUtils.hasNetwork(context)) {
                if (DoYouEvenPlankActivity.this instanceof RequiresNetworkActivity) {
                    // if we have network and the UI blocker activity is currently up, we'll get rid of it
                    finish();
                }
            } else {
                if (!(DoYouEvenPlankActivity.this instanceof RequiresNetworkActivity)) {
                    // if we don't have network and the UI blocker activity isn't up, we need to start it
                    RequiresNetworkActivity.start(DoYouEvenPlankActivity.this);
                }
            }
        }

    }

}

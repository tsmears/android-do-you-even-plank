package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;
import com.doyouevenplank.android.app.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends DoYouEvenPlankActivity {

    // after this amount of time, we'll assume the network request failed, and update the text accordingly
    private static final int SET_TIMEOUT_TEXT_RUNNABLE_MILLIS = 5000;
    private static final int CHECK_IF_READY_POLL_FREQUENCY_MILLIS = 500;

    @BindView(R.id.welcome_screen_error_message) TextView mErrorMessage;

    private Handler mHandler;
    private Runnable mCheckIfReadyRunnable;
    private Runnable mShowRequestTimedOutErrorMessageRunnable;

    public static void start(Context caller) {
        Intent intent = new Intent(caller, WelcomeActivity.class);
        caller.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        mErrorMessage.setVisibility(View.GONE);

        mHandler = new Handler();
        mCheckIfReadyRunnable = new Runnable() {
            @Override
            public void run() {
                if (SessionManager.getInstance().isReady()) {
                    PickDurationActivity.start(WelcomeActivity.this);
                    finish();
                } else {
                    mHandler.postDelayed(mCheckIfReadyRunnable, CHECK_IF_READY_POLL_FREQUENCY_MILLIS);
                }
            }
        };
        mShowRequestTimedOutErrorMessageRunnable = new Runnable() {
            @Override
            public void run() {
                mErrorMessage.setVisibility(View.VISIBLE);
            }
        };

        mHandler.post(mCheckIfReadyRunnable);
        mHandler.postDelayed(mShowRequestTimedOutErrorMessageRunnable, SET_TIMEOUT_TEXT_RUNNABLE_MILLIS);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mHandler.removeCallbacks(mCheckIfReadyRunnable);
        mHandler.removeCallbacks(mShowRequestTimedOutErrorMessageRunnable);
    }

}

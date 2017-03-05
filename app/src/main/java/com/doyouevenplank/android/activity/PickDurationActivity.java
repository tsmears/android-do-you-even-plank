package com.doyouevenplank.android.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;
import com.doyouevenplank.android.app.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickDurationActivity extends DoYouEvenPlankActivity {

    @BindView(R.id.temp_text_view) TextView mTempTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_duration);
        ButterKnife.bind(this);

        mTempTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoActivity.start(PickDurationActivity.this);
            }
        });

        // request an instance of SessionManager, just to kick off the network request
        SessionManager.getInstance();
    }

}

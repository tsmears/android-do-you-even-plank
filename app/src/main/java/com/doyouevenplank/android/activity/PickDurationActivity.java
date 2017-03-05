package com.doyouevenplank.android.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;
import com.doyouevenplank.android.app.SessionManager;
import com.doyouevenplank.android.component.PickDurationAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickDurationActivity extends DoYouEvenPlankActivity {

    @BindView(R.id.pick_duration_recyclerview) RecyclerView mPickDurationRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private PickDurationAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_duration);
        ButterKnife.bind(this);

        // request an instance of SessionManager, just to kick off the network request
        SessionManager.getInstance();

        mPickDurationRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPickDurationRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PickDurationAdapter(this);
        mPickDurationRecyclerView.setAdapter(mAdapter);
    }

}

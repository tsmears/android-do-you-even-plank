package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;
import com.doyouevenplank.android.component.PickDurationAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickDurationActivity extends DoYouEvenPlankActivity {

    @BindView(R.id.pick_duration_recyclerview) RecyclerView mPickDurationRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private PickDurationAdapter mAdapter;

    public static void start(Context caller) {
        Intent intent = new Intent(caller, PickDurationActivity.class);
        caller.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_duration);
        ButterKnife.bind(this);

        mPickDurationRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPickDurationRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PickDurationAdapter(this);
        mPickDurationRecyclerView.setAdapter(mAdapter);
    }

}

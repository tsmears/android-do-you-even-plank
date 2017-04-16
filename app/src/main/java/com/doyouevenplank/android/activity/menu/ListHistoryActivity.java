package com.doyouevenplank.android.activity.menu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;
import com.doyouevenplank.android.component.HistoryAdapter;
import com.doyouevenplank.android.db.HistoryDbAccessor;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListHistoryActivity extends DoYouEvenPlankActivity {

    @BindView(R.id.history_recycler_view) RecyclerView mHistoryRecyclerView;

    private RecyclerView.LayoutManager mLayoutManager;
    private HistoryAdapter mAdapter;

    public static void start(Context caller) {
        Intent intent = new Intent(caller, ListHistoryActivity.class);
        caller.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_history);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle(R.string.history);

        int orientation = LinearLayoutManager.VERTICAL;
        mHistoryRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, orientation, false);
        mHistoryRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, orientation);
        mHistoryRecyclerView.addItemDecoration(dividerItemDecoration);

        Cursor historyItemsCursor = HistoryDbAccessor.getInstance(this).getHistoryItems();
        mAdapter = new HistoryAdapter(this, historyItemsCursor);
        mHistoryRecyclerView.setAdapter(mAdapter);
    }

}

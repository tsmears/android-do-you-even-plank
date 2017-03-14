package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;
import com.doyouevenplank.android.component.PickDurationAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PickDurationActivity extends DoYouEvenPlankActivity {

    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.navigation) NavigationView mNavigationView;
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

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();

                switch (menuItem.getItemId()) {
                    case R.id.menu_history: // TODO
                        break;
                    case R.id.menu_support:
                        SupportActivity.start(PickDurationActivity.this);
                        break;
                    case R.id.menu_open_source_attributions: // TODO
                        break;
                    case R.id.menu_about: // TODO
                        break;
                }
                return true;
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mPickDurationRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPickDurationRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new PickDurationAdapter(this);
        mPickDurationRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // force the hamburger to open or close the menu http://stackoverflow.com/a/30833113/665632
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    mDrawerLayout.closeDrawers();
                } else {
                    mDrawerLayout.openDrawer(GravityCompat.START);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

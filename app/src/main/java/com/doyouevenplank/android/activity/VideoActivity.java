package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.YouTubeFailureRecoveryActivity;
import com.doyouevenplank.android.app.Config;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class VideoActivity extends YouTubeFailureRecoveryActivity {

    private YouTubePlayerFragment mYouTubePlayerFragment;

    public static void start(Context caller) {
        Intent intent = new Intent(caller, VideoActivity.class);
        caller.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mYouTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        mYouTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.loadVideo("nCgQDjiotG0");
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }

}

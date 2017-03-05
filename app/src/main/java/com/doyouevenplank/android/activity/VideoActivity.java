package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.YouTubeFailureRecoveryActivity;
import com.doyouevenplank.android.app.Config;
import com.doyouevenplank.android.model.Video;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class VideoActivity extends YouTubeFailureRecoveryActivity {

    private static final String EXTRA_VIDEO_ID = "extra_video_json";
    private static final String EXTRA_VIDEO_START_TIME_SECONDS = "extra_video_start_time";
    private static final String EXTRA_VIDEO_END_TIME_SECONDS = "extra_video_end_time";

    private YouTubePlayerFragment mYouTubePlayerFragment;

    private String mVideoId;
    private int mVideoStartTimeSeconds;
    private int mVideoEndTimeSeconds;

    public static void start(Context caller, Video video) {
        Intent intent = new Intent(caller, VideoActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, video.videoId);
        intent.putExtra(EXTRA_VIDEO_START_TIME_SECONDS, video.startTimeSeconds);
        intent.putExtra(EXTRA_VIDEO_END_TIME_SECONDS, video.endTimeSeconds);
        caller.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mVideoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);
        mVideoStartTimeSeconds = getIntent().getIntExtra(EXTRA_VIDEO_START_TIME_SECONDS, -1);
        mVideoEndTimeSeconds = getIntent().getIntExtra(EXTRA_VIDEO_END_TIME_SECONDS, -1);

        if (mVideoStartTimeSeconds == -1 || mVideoEndTimeSeconds == -1) {
            finish();
            return;
        }

        mYouTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        mYouTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.loadVideo(mVideoId, mVideoStartTimeSeconds * 1000);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }

}

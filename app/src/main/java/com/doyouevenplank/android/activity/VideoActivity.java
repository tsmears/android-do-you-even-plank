package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.YouTubeFailureRecoveryActivity;
import com.doyouevenplank.android.app.Config;
import com.doyouevenplank.android.model.Video;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

public class VideoActivity extends YouTubeFailureRecoveryActivity {

    private static final String EXTRA_VIDEO_ID = "extra_video_json";
    private static final String EXTRA_VIDEO_START_TIME_SECONDS = "extra_video_start_time";
    private static final String EXTRA_VIDEO_DURATION_SECONDS = "extra_video_duration_seconds";

    private YouTubePlayerFragment mYouTubePlayerFragment;

    private Handler mHandler;
    private Runnable mFinishPlankingRunnable;

    private String mVideoId;
    private int mVideoStartTimeSeconds;
    private int mVideoDurationSeconds;

    public static void start(Context caller, Video video) {
        Intent intent = new Intent(caller, VideoActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, video.videoId);
        intent.putExtra(EXTRA_VIDEO_START_TIME_SECONDS, video.startTimeSeconds);
        intent.putExtra(EXTRA_VIDEO_DURATION_SECONDS, video.durationSeconds);
        caller.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        mVideoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);
        mVideoStartTimeSeconds = getIntent().getIntExtra(EXTRA_VIDEO_START_TIME_SECONDS, -1);
        mVideoDurationSeconds = getIntent().getIntExtra(EXTRA_VIDEO_DURATION_SECONDS, -1);

        if (mVideoStartTimeSeconds == -1 || mVideoDurationSeconds == -1) {
            finish();
            return;
        }

        mHandler = new Handler();

        mYouTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        mYouTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, final YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.loadVideo(mVideoId, mVideoStartTimeSeconds * 1000);

            mFinishPlankingRunnable = new Runnable() {
                @Override
                public void run() {
                    player.pause();
                    VideoActivity.this.finish();
                }
            };

            mHandler.postDelayed(mFinishPlankingRunnable, mVideoDurationSeconds * 1000);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mFinishPlankingRunnable != null) {
            mHandler.removeCallbacks(mFinishPlankingRunnable);
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }

}

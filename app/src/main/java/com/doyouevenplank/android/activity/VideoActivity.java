package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.YouTubeFailureRecoveryActivity;
import com.doyouevenplank.android.app.Config;
import com.doyouevenplank.android.db.HistoryDbAccessor;
import com.doyouevenplank.android.model.Video;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;

import java.util.Date;

public class VideoActivity extends YouTubeFailureRecoveryActivity {

    private static final String EXTRA_VIDEO_ID = "extra_video_json";
    private static final String EXTRA_VIDEO_START_TIME_SECONDS = "extra_video_start_time";
    private static final String EXTRA_VIDEO_DURATION_SECONDS = "extra_video_duration_seconds";

    private YouTubePlayerFragment mYouTubePlayerFragment;
    private YouTubePlayer mPlayer;

    private Handler mHandler;
    private Runnable mFinishPlankingRunnable;

    private String mVideoId;
    private int mVideoStartTimeSeconds;
    private int mVideoDurationSeconds;

    public static void start(Context caller, Video video) {
        start(caller, video.videoId, video.startTimeSeconds, video.durationSeconds);
    }

    public static void start(Context caller, String videoId, int startTimeSeconds, int durationSeconds) {
        Intent intent = new Intent(caller, VideoActivity.class);
        intent.putExtra(EXTRA_VIDEO_ID, videoId);
        intent.putExtra(EXTRA_VIDEO_START_TIME_SECONDS, startTimeSeconds);
        intent.putExtra(EXTRA_VIDEO_DURATION_SECONDS, durationSeconds);
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
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        mPlayer = player;
        if (!wasRestored) {
            player.loadVideo(mVideoId, mVideoStartTimeSeconds * 1000);

            mFinishPlankingRunnable = new Runnable() {
                @Override
                public void run() {
                    mPlayer.pause();
                    VideoActivity.this.finish();

                    // insert the video into the history db
                    HistoryDbAccessor.getInstance(VideoActivity.this).insertHistoryItem(mVideoId, new Date(), mVideoDurationSeconds);
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

        if (mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }

}

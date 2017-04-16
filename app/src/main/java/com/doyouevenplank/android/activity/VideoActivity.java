package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.YouTubeFailureRecoveryActivity;
import com.doyouevenplank.android.app.Config;
import com.doyouevenplank.android.app.SharedPreferencesManager;
import com.doyouevenplank.android.component.VideoActivitySoundPoolPlayer;
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
    private TextView mCountdownTextView;
    private ProgressBar mProgressBar;

    private YouTubePlayer mVideoPlayer;
    private VideoActivitySoundPoolPlayer mSoundPlayer;

    private Handler mHandler;
    private int mCountdownInt;
    private Runnable mCountdownRunnable;
    private Runnable mBeginRunnable;
    private Runnable mPlayVideoRunnable;
    private Runnable mFinishPlankingRunnable;
    private int mVideoProgressSeconds;
    private Runnable mUpdateProgressBarRunnable;

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

        mSoundPlayer = new VideoActivitySoundPoolPlayer(this);

        mVideoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);
        mVideoStartTimeSeconds = getIntent().getIntExtra(EXTRA_VIDEO_START_TIME_SECONDS, -1);
        mVideoDurationSeconds = getIntent().getIntExtra(EXTRA_VIDEO_DURATION_SECONDS, -1);

        if (mVideoStartTimeSeconds == -1 || mVideoDurationSeconds == -1) {
            finish();
            return;
        }

        mCountdownTextView = (TextView) findViewById(R.id.countdown_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        int progressBarVisibility = SharedPreferencesManager.getInstance(this).getShouldShowProgressBarInVideoActivity() ? View.VISIBLE : View.GONE;
        mProgressBar.setVisibility(progressBarVisibility);

        mYouTubePlayerFragment = (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
        mYouTubePlayerFragment.initialize(Config.YOUTUBE_API_KEY, this);

        mHandler = new Handler();
        mCountdownInt = 3;
        mCountdownRunnable = new Runnable() {
            @Override
            public void run() {
                mSoundPlayer.playSoundFromResource(R.raw.ready);
                mCountdownTextView.setText("" + mCountdownInt);
                mCountdownInt -= 1;

                // start loading the video in the background, but paused, so that we can start playing it immediately once the countdown is finished
                mVideoPlayer.loadVideo(mVideoId, mVideoStartTimeSeconds * 1000);
                mVideoPlayer.pause();
            }
        };
        mBeginRunnable = new Runnable() {
            @Override
            public void run() {
                mSoundPlayer.playSoundFromResource(R.raw.go);
                mCountdownTextView.setText(R.string.video_countdown_begin_message);
            }
        };
        mPlayVideoRunnable = new Runnable() {
            @Override
            public void run() {
                mCountdownTextView.setVisibility(View.GONE);
                mVideoPlayer.play();

                mVideoProgressSeconds = 0;
                mProgressBar.setMax(mVideoDurationSeconds);
                mHandler.post(mUpdateProgressBarRunnable);
                mHandler.postDelayed(mFinishPlankingRunnable, mVideoDurationSeconds * 1000);
            }
        };
        mFinishPlankingRunnable = new Runnable() {
            @Override
            public void run() {
                mVideoPlayer.pause();
                VideoActivity.this.finish();

                // insert the video into the history db
                HistoryDbAccessor.getInstance(VideoActivity.this).insertHistoryItem(mVideoId, new Date(), mVideoDurationSeconds);
            }
        };
        mUpdateProgressBarRunnable = new Runnable() {
            @Override
            public void run() {
                mProgressBar.setProgress(mVideoProgressSeconds);
                mVideoProgressSeconds += 1;
                mHandler.postDelayed(mUpdateProgressBarRunnable, 1000); // update the progress bar every second
            }
        };
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        mVideoPlayer = player;
        if (!wasRestored) {
            mCountdownTextView.setVisibility(View.VISIBLE);
            mHandler.post(mCountdownRunnable); // 3
            mHandler.postDelayed(mCountdownRunnable, 1000); // 2
            mHandler.postDelayed(mCountdownRunnable, 2000); // 1
            mHandler.postDelayed(mBeginRunnable, 3000); // plank!
            mHandler.postDelayed(mPlayVideoRunnable, 3500); // actually begin playing the video
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Runnable[] runnablesThatNeedToBeRemoved = new Runnable[]{
                mCountdownRunnable,
                mBeginRunnable,
                mPlayVideoRunnable,
                mFinishPlankingRunnable,
                mUpdateProgressBarRunnable,
        };
        for (Runnable runnable : runnablesThatNeedToBeRemoved) {
            if (runnable != null) {
                mHandler.removeCallbacks(runnable);
            }
        }

        if (mVideoPlayer != null) {
            mVideoPlayer.release();
        }
        if (mSoundPlayer != null) {
            mSoundPlayer.release();
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerFragment) getFragmentManager().findFragmentById(R.id.youtube_fragment);
    }

}

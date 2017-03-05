package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;
import com.doyouevenplank.android.app.SessionManager;
import com.doyouevenplank.android.model.Video;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PreviewVideoActivity extends DoYouEvenPlankActivity {

    private static final String EXTRA_DURATION = "extra_duration";

    @BindView(R.id.video_title_textview) TextView mVideoTitleTextView;
    @BindView(R.id.action_plank_textview) TextView mActionPlankTextView;
    @BindView(R.id.action_skip_textview) TextView mActionSkipTextView;

    private Random mRandom;
    private Video mCurrentVideo;

    public static void start(Context caller, int duration) {
        Intent intent = new Intent(caller, PreviewVideoActivity.class);
        intent.putExtra(EXTRA_DURATION, duration);
        caller.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_video);
        ButterKnife.bind(this);

        int duration = getIntent().getIntExtra(EXTRA_DURATION, -1);
        if (duration == -1) {
            finish();
            return;
        }

        mRandom = new Random();

        // get a random video of the specified duration
        final List<Video> videos = SessionManager.getInstance().getVideosForDuration(duration);
        int randomIndex = mRandom.nextInt(videos.size());
        mCurrentVideo = videos.get(randomIndex);
        this.fetchAndSetVideoTitle();

        mActionPlankTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VideoActivity.start(PreviewVideoActivity.this, mCurrentVideo);
                finish();
            }
        });

        mActionSkipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newRandomIndex = mRandom.nextInt(videos.size());
                mCurrentVideo = videos.get(newRandomIndex);
                fetchAndSetVideoTitle();
            }
        });
    }

    private void fetchAndSetVideoTitle() {
        mVideoTitleTextView.setText(mCurrentVideo.videoId);
    }

}

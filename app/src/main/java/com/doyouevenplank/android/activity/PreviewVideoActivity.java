package com.doyouevenplank.android.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.base.DoYouEvenPlankActivity;
import com.doyouevenplank.android.app.Config;
import com.doyouevenplank.android.app.SessionManager;
import com.doyouevenplank.android.app.SharedPreferencesManager;
import com.doyouevenplank.android.component.YouTubeThumbnailListener;
import com.doyouevenplank.android.model.Video;
import com.doyouevenplank.android.network.YouTubeApi;
import com.doyouevenplank.android.network.YouTubeVideoMetadataResponse;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PreviewVideoActivity extends DoYouEvenPlankActivity {

    private static final String EXTRA_DURATION = "extra_duration";

    @BindView(R.id.video_thumbnail_view) YouTubeThumbnailView mVideoThumbnailView;
    @BindView(R.id.video_title_textview) TextView mVideoTitleTextView;
    @BindView(R.id.video_description_textview) TextView mVideoDescriptionTextView;
    @BindView(R.id.action_plank_textview) TextView mActionPlankTextView;
    @BindView(R.id.action_skip_textview) TextView mActionSkipTextView;

    private YouTubeApi mYouTubeApi;

    private Random mRandom;
    private YouTubeThumbnailListener mThumbnailListener;
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.YOUTUBE_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mYouTubeApi = retrofit.create(YouTubeApi.class);

        mRandom = new Random();

        // get a random video of the specified duration
        final List<Video> videos = SessionManager.getInstance().getVideosForDuration(duration);
        if (videos == null || videos.size() == 0) {
            Toast.makeText(this, R.string.error_no_videos_found_toast, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        int randomIndex = mRandom.nextInt(videos.size());
        mCurrentVideo = videos.get(randomIndex);

        mThumbnailListener = new YouTubeThumbnailListener();
        mVideoThumbnailView.setTag(mCurrentVideo.videoId);
        mVideoThumbnailView.initialize(Config.YOUTUBE_API_KEY, mThumbnailListener);
        this.fetchAndSetVideoMetadata();

        mActionPlankTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialogThenVideoActivity();
            }
        });

        mActionSkipTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int newRandomIndex = mRandom.nextInt(videos.size());
                mCurrentVideo = videos.get(newRandomIndex);
                fetchAndSetVideoMetadata();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mThumbnailListener != null) {
            mThumbnailListener.releaseLoader();
        }
    }

    private void showAlertDialogThenVideoActivity() {
        if (!SharedPreferencesManager.getInstance(this).getHasShownCountdownInfoPopup()) {
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setMessage(getResources().getString(R.string.countdown_info_popup_message))
                    .setPositiveButton(R.string.countdown_info_popup_confirmation, new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferencesManager.getInstance(PreviewVideoActivity.this).setHasShownCountdownInfoPopup();

                            VideoActivity.start(PreviewVideoActivity.this, mCurrentVideo);
                            finish();
                        }

                    })
                    .show();
        } else {
            VideoActivity.start(PreviewVideoActivity.this, mCurrentVideo);
            finish();
        }
    }

    private void fetchAndSetVideoMetadata() {
        mVideoThumbnailView.setImageResource(R.drawable.loading_placeholder);
        mVideoTitleTextView.setText(R.string.video_preview_title_placeholder);
        mVideoDescriptionTextView.setText(null);
        mThumbnailListener.loadNewThumbnail(mCurrentVideo.videoId);

        Call<YouTubeVideoMetadataResponse> request = mYouTubeApi.getVideoMetadataPayload(mCurrentVideo.videoId, Config.YOUTUBE_API_KEY);
        request.enqueue(new Callback<YouTubeVideoMetadataResponse>() {
            @Override
            public void onResponse(Call<YouTubeVideoMetadataResponse> call, Response<YouTubeVideoMetadataResponse> response) {
                YouTubeVideoMetadataResponse responseObject = response.body();
                if (responseObject == null) {
                    return;
                }
                if (responseObject.items == null || responseObject.items.size() == 0) {
                    return;
                }
                YouTubeVideoMetadataResponse.Video video = responseObject.items.get(0);
                if (!TextUtils.equals(mCurrentVideo.videoId, video.id)) {
                    return;
                }
                mVideoTitleTextView.setText(video.snippet.title);
                mVideoDescriptionTextView.setText(video.snippet.description);
            }

            @Override
            public void onFailure(Call<YouTubeVideoMetadataResponse> call, Throwable t) {
                Log.e(Config.LOG_WARNING_TAG, "error fetching video metadata");
            }
        });
    }

}

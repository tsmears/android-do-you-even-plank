package com.doyouevenplank.android.component;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doyouevenplank.android.R;
import com.doyouevenplank.android.activity.VideoActivity;
import com.doyouevenplank.android.activity.menu.ListHistoryActivity;
import com.doyouevenplank.android.app.Config;
import com.doyouevenplank.android.db.HistoryContract;
import com.doyouevenplank.android.network.YouTubeApi;
import com.doyouevenplank.android.network.YouTubeVideoMetadataResponse;
import com.doyouevenplank.android.util.StringUtils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ListHistoryActivity mActivity;
    private CursorAdapter mCursorAdapter;

    private YouTubeApi mYouTubeApi;
    private Map<String, YouTubeVideoMetadataResponse.Video> mVideoByIdCache;

    public HistoryAdapter(ListHistoryActivity activity, Cursor c) {
        mActivity = activity;
        mVideoByIdCache = new HashMap<String, YouTubeVideoMetadataResponse.Video>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.YOUTUBE_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mYouTubeApi = retrofit.create(YouTubeApi.class);

        mCursorAdapter = new CursorAdapter(mActivity, c, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(context).inflate(R.layout.history_list_item, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                RelativeLayout container = (RelativeLayout) view;
                final ImageView thumbnailView = (ImageView) container.findViewById(R.id.video_thumbnail_view);
                final TextView titleTextView = (TextView) container.findViewById(R.id.title);
                final TextView subtitleTextView = (TextView) container.findViewById(R.id.subtitle);
                TextView durationTextView = (TextView) container.findViewById(R.id.duration);
                TextView dateTextView = (TextView) container.findViewById(R.id.date);

                // first clear the thumbnail, title, and subtitle until we can load the real info from network
                thumbnailView.setImageDrawable(null);
                titleTextView.setText(null);
                subtitleTextView.setText(null);

                final String videoId = getVideoId(cursor);
                String videoTimestamp = getVideoTimestamp(cursor);
                final int videoStartTimeSeconds = getVideoStartTimeSeconds(cursor);
                final int videoDurationSeconds = getVideoDurationSeconds(cursor);

                durationTextView.setText(StringUtils.getTimeStringFromIntDuration(videoDurationSeconds));
                dateTextView.setText(StringUtils.isoStringToFriendlyString(videoTimestamp));

                if (mVideoByIdCache.containsKey(videoId)) { // if it's cached
                    fillViewsWithVideoData(titleTextView, subtitleTextView, thumbnailView, mVideoByIdCache.get(videoId));
                } else { // fetch it from network
                    Call<YouTubeVideoMetadataResponse> request = mYouTubeApi.getVideoMetadataPayload(videoId, Config.YOUTUBE_API_KEY);
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
                            if (!TextUtils.equals(videoId, video.id)) {
                                return;
                            }

                            mVideoByIdCache.put(videoId, video);
                            fillViewsWithVideoData(titleTextView, subtitleTextView, thumbnailView, video);
                        }

                        @Override
                        public void onFailure(Call<YouTubeVideoMetadataResponse> call, Throwable t) {
                            Log.e(Config.LOG_WARNING_TAG, "error fetching video metadata");
                        }
                    });
                }

                container.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        VideoActivity.start(mActivity, videoId, videoStartTimeSeconds, videoDurationSeconds);
                    }
                });
            }
        };
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mCursorAdapter.newView(mActivity, mCursorAdapter.getCursor(), parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mCursorAdapter.getCursor().moveToPosition(position);
        mCursorAdapter.bindView(holder.itemView, mActivity, mCursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return mCursorAdapter.getCount();
    }

    private String getVideoId(Cursor cursor) {
        int videoIdIndex = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_VIDEO_ID);
        if (videoIdIndex == -1) {
            return null;
        }
        return cursor.getString(videoIdIndex);
    }

    private String getVideoTimestamp(Cursor cursor) {
        int videoTimestampIndex = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_TIMESTAMP);
        if (videoTimestampIndex == -1) {
            return null;
        }
        return cursor.getString(videoTimestampIndex);
    }

    private int getVideoStartTimeSeconds(Cursor cursor) {
        int videoStartTimeIndex = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_START_TIME_SECONDS);
        if (videoStartTimeIndex == -1) {
            return 0;
        }
        return cursor.getInt(videoStartTimeIndex);
    }

    private int getVideoDurationSeconds(Cursor cursor) {
        int videoDurationIndex = cursor.getColumnIndex(HistoryContract.HistoryEntry.COLUMN_NAME_DURATION_SECONDS);
        if (videoDurationIndex == -1) {
            return 0;
        }
        return cursor.getInt(videoDurationIndex);
    }

    private void fillViewsWithVideoData(TextView titleTextView, TextView subtitleTextView, ImageView thumbnailImageView, YouTubeVideoMetadataResponse.Video video) {
        titleTextView.setText(video.snippet.title);
        subtitleTextView.setText(video.snippet.description);
        Picasso.with(mActivity)
                .load(video.snippet.getDefaultThumbnailUrl())
                .into(thumbnailImageView);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }

}

package com.doyouevenplank.android.model;

import android.util.Log;
import android.util.SparseArray;

import com.doyouevenplank.android.app.Config;
import com.doyouevenplank.android.network.GoogleSheetsDurationPayload;
import com.doyouevenplank.android.network.GoogleSheetsVideoMetadataPayload;
import com.doyouevenplank.android.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Session {

    // use SparseArray to avoid autoboxing
    private SparseArray<List<Video>> mVideosByDuration;
    private int[] mDurationChoices;

    public Session() {
        mVideosByDuration = new SparseArray<List<Video>>();
    }

    public boolean isReady() {
        return mVideosByDuration.size() != 0 && mDurationChoices != null && mDurationChoices[mDurationChoices.length - 1] != 0;
    }

    public void readVideoMetadataPayload(GoogleSheetsVideoMetadataPayload payload) {
        for (GoogleSheetsVideoMetadataPayload.Entry entry : payload.feed.entry) {
            try {
                String videoId = StringUtils.getVideoIdFromGsxLink(entry.gsx$link.getString());
                int durationSeconds = StringUtils.getIntDurationFromTimeString(entry.gsx$time.getString());
                int startTimeSeconds = StringUtils.getIntDurationFromTimeString(entry.gsx$starttime.getString());
                int endTimeSeconds = StringUtils.getIntDurationFromTimeString(entry.gsx$endtime.getString());
                Video video = new Video(videoId, durationSeconds, startTimeSeconds, endTimeSeconds, entry.gsx$genre.getString());
                this.safeInsertIntoMap(video);
            } catch (Exception e) {
                Log.e(Config.LOG_WARNING_TAG, "error parsing link " + entry.gsx$link.getString());
            }
        }
    }

    public void readDurationPayload(GoogleSheetsDurationPayload payload) {
        mDurationChoices = new int[payload.feed.entry.size()];
        for (int i = 0; i < payload.feed.entry.size(); i += 1) {
            GoogleSheetsDurationPayload.Entry entry = payload.feed.entry.get(i);
            mDurationChoices[i] = Integer.parseInt(entry.gsx$duration.getString());
        }
    }

    public List<Video> getVideosForDuration(int durationSeconds) {
        return mVideosByDuration.get(durationSeconds);
    }

    public int[] getDurationChoices() {
        return mDurationChoices;
    }

    private void safeInsertIntoMap(Video video) {
        List<Video> durationSubset = mVideosByDuration.get(video.durationSeconds);
        if (durationSubset == null) {
            durationSubset = new ArrayList<Video>();
            mVideosByDuration.put(video.durationSeconds, durationSubset);
        }
        durationSubset.add(video);
    }

}

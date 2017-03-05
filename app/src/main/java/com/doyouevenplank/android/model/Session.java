package com.doyouevenplank.android.model;

import android.util.Log;
import android.util.SparseArray;

import com.doyouevenplank.android.app.Config;
import com.doyouevenplank.android.network.GoogleSheetsVideoMetadataPayload;
import com.doyouevenplank.android.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class Session {

    // use SparseArray to avoid autoboxing
    private SparseArray<Set<Video>> mVideosByDuration;

    public Session(GoogleSheetsVideoMetadataPayload payload) {
        mVideosByDuration = new SparseArray<Set<Video>>();

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

    private void safeInsertIntoMap(Video video) {
        Set<Video> durationSubset = mVideosByDuration.get(video.durationSeconds);
        if (durationSubset == null) {
            durationSubset = new HashSet<Video>();
            mVideosByDuration.put(video.durationSeconds, durationSubset);
        }
        durationSubset.add(video);
    }

}

package com.doyouevenplank.android.model;

import android.util.Log;
import android.util.SparseArray;

import com.doyouevenplank.android.network.GoogleSheetsVideoMetadataPayload;
import com.doyouevenplank.android.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

public class Session {

    // use SparseArray to avoid autoboxing
    private SparseArray<Set<Video>> mVideosByDuration;

    public Session(GoogleSheetsVideoMetadataPayload payload) {
        mVideosByDuration = new SparseArray<Set<Video>>();

        for (GoogleSheetsVideoMetadataPayload.Entry entry : payload.entry) {
            try {
                String videoId = StringUtils.getVideoIdFromGsxLink(entry.gsx$link.getString());
            } catch (Exception e) {
                Log.d("patricia", "videoId error " + entry.gsx$link.getString());
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

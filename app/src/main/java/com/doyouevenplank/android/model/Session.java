package com.doyouevenplank.android.model;

import android.util.SparseArray;

import com.doyouevenplank.android.network.GoogleSheetsVideoMetadataPayload;

import java.util.Set;

public class Session {

    // use SparseArray to avoid autoboxing
    private SparseArray<Set<Video>> mVideosByDuration;

    public Session(GoogleSheetsVideoMetadataPayload payload) {
        mVideosByDuration = new SparseArray<Set<Video>>();

        // TODO populate mVideosByDuration
    }

}

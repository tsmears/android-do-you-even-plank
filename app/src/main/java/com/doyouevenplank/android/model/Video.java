package com.doyouevenplank.android.model;

public class Video {

    public String videoId;
    public int durationSeconds;
    public int startTimeSeconds;
    public int endTimeSeconds;
    public String genre;

    public Video(String videoId, int durationSeconds, int startTimeSeconds, int endTimeSeconds, String genre) {
        super();
        this.videoId = videoId;
        this.durationSeconds = durationSeconds;
        this.startTimeSeconds = startTimeSeconds;
        this.endTimeSeconds = endTimeSeconds;
        this.genre = genre;
    }

}

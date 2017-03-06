package com.doyouevenplank.android.network;

import java.util.List;

/**
 * https://developers.google.com/youtube/v3/docs/videos
 */
public class YouTubeVideoMetadataResponse {

    public List<Video> items;

    public static class Video {

        public String id;
        public Snippet snippet;

        public static class Snippet {
            public String title;
            public String description;
        }

    }

}

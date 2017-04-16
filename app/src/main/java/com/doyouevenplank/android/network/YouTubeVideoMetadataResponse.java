package com.doyouevenplank.android.network;

import java.util.List;
import java.util.Map;

/**
 * https://developers.google.com/youtube/v3/docs/videos
 */
public class YouTubeVideoMetadataResponse {

    public List<Video> items;

    public static class Video {
        public String id;
        public Snippet snippet;
    }

    public static class Snippet {

        public static final String[] THUMBNAIL_KEYS = new String[]{
                "default",
                "medium",
                "high",
                "standard",
                "maxres",
        };

        public String title;
        public String description;
        public Map<String, Thumbnail> thumbnails; // default, medium, high, standard, maxres

        public String getDefaultThumbnailUrl() {
            for (String key : THUMBNAIL_KEYS) {
                if (thumbnails.containsKey(key)) {
                    return thumbnails.get(key).url;
                }
            }
            return null;
        }

    }

    public static class Thumbnail {
        public String url;
        public int width;
        public int height;
    }

}

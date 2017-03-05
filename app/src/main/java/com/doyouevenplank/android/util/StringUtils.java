package com.doyouevenplank.android.util;

import android.net.UrlQuerySanitizer;
import android.text.TextUtils;

import com.doyouevenplank.android.app.Config;

public class StringUtils {

    /**
     * we'll only handle the following formats:
     *  - :xy
     *  - x:yz
     *  - xy:zw
     *  I'm assuming nobody is going to be using this app to plank for more than an hour.
     */
    public static int getIntDurationFromTimeString(String timeString) throws Exception {
        if (TextUtils.isEmpty(timeString)) {
            throw new Exception("empty or missing timeString from server");
        }

        String[] tokens = timeString.split(":");
        if (tokens.length == 1) {
            return Integer.parseInt(tokens[0]);
        } else if (tokens.length == 2) {
            return 60 * Integer.parseInt(tokens[0]) + Integer.parseInt(tokens[1]);
        } else {
            throw new Exception("unexpected time string from server; " + timeString);
        }
    }

    /**
     * we only handle links of the form youtu.be, and otherwise look at the query param v.
     * we assume that videoIds are always 11 characters long. this will break if that ever changes.
     */
    public static String getVideoIdFromGsxLink(String gsxLink) throws Exception {
        if (TextUtils.isEmpty(gsxLink)) {
            throw new Exception("empty or missing gsxLink from server");
        }

        // special case youtu.be shortened links
        String shortenedYoutubePrefix = "https://youtu.be/";
        if (gsxLink.startsWith(shortenedYoutubePrefix)) {
            String videoId = gsxLink.replace(shortenedYoutubePrefix, "");
            if (videoId.length() != Config.YOUTUBE_VIDEO_ID_LENGTH) {
                throw new Exception("did not recognize shortened youtube link " + gsxLink);
            }
            return videoId;
        }

        // otherwise, just look at the v= param
        UrlQuerySanitizer sanitizer = new UrlQuerySanitizer(gsxLink);
        String videoId = sanitizer.getValue("v");
        if (TextUtils.isEmpty(videoId)) {
            throw new Exception("could not find query param v in " + gsxLink);
        }
        if (videoId.length() != Config.YOUTUBE_VIDEO_ID_LENGTH) {
            throw new Exception("query param v was malformed in " + gsxLink);
        }
        return videoId;
    }

}

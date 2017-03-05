package com.doyouevenplank.android.util;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String getVideoIdFromGsxLink(String gsxLink) throws Exception {
        if (TextUtils.isEmpty(gsxLink)) {
            throw new Exception("empty or missing gsxLink from server");
        }

        // http://markmail.org/message/jb6nsveqs7hya5la
        Pattern pattern = Pattern.compile("^https://www.youtube.com/watch?v\\u003d([a-zA-Z0-9_-]{11,})[$|\\]]");
        Matcher matcher = pattern.matcher(gsxLink);
        if (matcher.find()) {
            return matcher.group();
        } else {
            throw new Exception("gsxLink did not match pattern; " + gsxLink);
        }
    }

}

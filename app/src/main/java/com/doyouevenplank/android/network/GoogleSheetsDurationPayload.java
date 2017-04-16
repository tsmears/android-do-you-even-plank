package com.doyouevenplank.android.network;

import com.doyouevenplank.android.util.WrappedString;

import java.util.List;

/**
 * https://spreadsheets.google.com/feeds/list/16HjsPso7jGQRJXP2Dnn_1maux0Oye_Vpr6bJzWSAsTY/2/public/values?alt=json
 */
public class GoogleSheetsDurationPayload {

    public Feed feed;

    public static class Feed {
        public List<Entry> entry;
    }

    public static class Entry {

        public WrappedString gsx$duration;

    }

}

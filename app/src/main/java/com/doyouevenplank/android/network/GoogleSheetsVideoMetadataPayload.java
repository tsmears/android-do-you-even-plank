package com.doyouevenplank.android.network;

import java.util.List;

/**
 * there are a ton of other fields at all levels, which we will ignore. see
 * https://spreadsheets.google.com/feeds/list/16HjsPso7jGQRJXP2Dnn_1maux0Oye_Vpr6bJzWSAsTY/1/public/values?alt=json
 */
public class GoogleSheetsVideoMetadataPayload {

    public Feed feed;

    public static class Feed {
        public List<Entry> entry;
    }

    public static class Entry {

        public WrappedString id;
        public WrappedString gsx$link;
        public WrappedString gsx$time;
        public WrappedString gsx$genre;
        public WrappedString gsx$starttime;
        public WrappedString gsx$endtime;

        public static class WrappedString {

            private String $t;

            public String getString() {
                return $t;
            }

        }

    }

}

package com.doyouevenplank.android.network;

import java.util.List;

/**
 * there are a ton of other fields at all levels, which we will ignore. see
 * https://spreadsheets.google.com/feeds/list/16HjsPso7jGQRJXP2Dnn_1maux0Oye_Vpr6bJzWSAsTY/1/public/values?alt=json
 */
public class GoogleSheetsVideoMetadataPayload {

    public List<Entry> entry;

    public static class Entry {

        public WrappedString id;
        public WrappedString gsx$link; // NOTE: this shit is escaped
        public WrappedString gsx$time; // NOTE: this is for some reason a string
        public WrappedString gsx$genre;
        public WrappedString gsx$starttime;
        public WrappedString gsx$endtime;

        public static class WrappedString {
            public String $t;
        }

    }

}

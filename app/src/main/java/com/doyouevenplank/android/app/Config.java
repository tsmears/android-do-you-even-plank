package com.doyouevenplank.android.app;

public class Config {

    public static final String LOG_WARNING_TAG = "DoYouEvenPlank";

    // credentials for the developer account are in the Airtable base linked to in the shared Drive folder
    public static final String YOUTUBE_API_KEY = "AIzaSyDjPkbaxgKE86qsTg-t7Z04ufIya7yAlRE";

    public static final String GOOGLE_SHEETS_BASE_URL = "https://spreadsheets.google.com/";
    public static final String GOOGLE_SHEETS_VIDEO_METADATA_ENDPOINT_ID = "16HjsPso7jGQRJXP2Dnn_1maux0Oye_Vpr6bJzWSAsTY";
    public static final int YOUTUBE_VIDEO_ID_LENGTH = 11;

    public static final int[] PLANK_CHOICE_DURATIONS = new int[]{
            30,
            45,
            60,
            75,
            90,
            105,
            120,
            150,
            180,
    };

}

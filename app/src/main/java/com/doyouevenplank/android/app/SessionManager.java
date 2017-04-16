package com.doyouevenplank.android.app;

import android.util.Log;

import com.doyouevenplank.android.model.Session;
import com.doyouevenplank.android.model.Video;
import com.doyouevenplank.android.network.GoogleSheetsApi;
import com.doyouevenplank.android.network.GoogleSheetsDurationPayload;
import com.doyouevenplank.android.network.GoogleSheetsVideoMetadataPayload;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SessionManager {

    private static SessionManager sInstance;

    private Session mSession;

    private SessionManager() {
        mSession = new Session();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.GOOGLE_SHEETS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GoogleSheetsApi googleSheets = retrofit.create(GoogleSheetsApi.class);

        // get video metadata payload
        Call<GoogleSheetsVideoMetadataPayload> videoMetadataRequest = googleSheets.getVideoMetadataPayload(Config.GOOGLE_SHEETS_VIDEO_METADATA_ENDPOINT_ID);
        videoMetadataRequest.enqueue(new Callback<GoogleSheetsVideoMetadataPayload>() {
            @Override
            public void onResponse(Call<GoogleSheetsVideoMetadataPayload> call, Response<GoogleSheetsVideoMetadataPayload> response) {
                mSession.readVideoMetadataPayload(response.body());
            }

            @Override
            public void onFailure(Call<GoogleSheetsVideoMetadataPayload> call, Throwable t) {
                Log.e(Config.LOG_WARNING_TAG, "error fetching video metadata");
            }
        });

        // get duration payload
        Call<GoogleSheetsDurationPayload> durationRequest = googleSheets.getDurationPayload(Config.GOOGLE_SHEETS_VIDEO_METADATA_ENDPOINT_ID);
        durationRequest.enqueue(new Callback<GoogleSheetsDurationPayload>() {
            @Override
            public void onResponse(Call<GoogleSheetsDurationPayload> call, Response<GoogleSheetsDurationPayload> response) {
                mSession.readDurationPayload(response.body());
            }

            @Override
            public void onFailure(Call<GoogleSheetsDurationPayload> call, Throwable t) {
                Log.e(Config.LOG_WARNING_TAG, "error fetching duration data");
            }
        });
    }

    public static synchronized SessionManager getInstance() {
        if (sInstance == null) {
            sInstance = new SessionManager();
        }
        return sInstance;
    }

    public boolean isReady() {
        return mSession != null && mSession.isReady();
    }

    public List<Video> getVideosForDuration(int durationSeconds) {
        return mSession.getVideosForDuration(durationSeconds);
    }

    public int[] getDurationChoices() {
        return mSession.getDurationChoices();
    }

}

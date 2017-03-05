package com.doyouevenplank.android.app;

import com.doyouevenplank.android.model.Session;
import com.doyouevenplank.android.network.GoogleSheetsApi;
import com.doyouevenplank.android.network.GoogleSheetsVideoMetadataPayload;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SessionManager {

    private static SessionManager sInstance;

    private Session mSession;

    private SessionManager() {
        // get video metadata payload
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Config.GOOGLE_SHEETS_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        GoogleSheetsApi googleSheets = retrofit.create(GoogleSheetsApi.class);

        Call<GoogleSheetsVideoMetadataPayload> request = googleSheets.getVideoMetadataPayload(Config.GOOGLE_SHEETS_VIDEO_METADATA_ENDPOINT_ID);
        request.enqueue(new Callback<GoogleSheetsVideoMetadataPayload>() {
            @Override
            public void onResponse(Call<GoogleSheetsVideoMetadataPayload> call, Response<GoogleSheetsVideoMetadataPayload> response) {
                mSession = new Session(response.body());
            }

            @Override
            public void onFailure(Call<GoogleSheetsVideoMetadataPayload> call, Throwable t) {
                // TODO handle failure
            }
        });
    }

    public static synchronized SessionManager getInstance() {
        if (sInstance == null) {
            sInstance = new SessionManager();
        }
        return sInstance;
    }

}

package com.doyouevenplank.android.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GoogleSheetsApi {

    @GET("feeds/list/{endpointId}/1/public/values?alt=json")
    Call<GoogleSheetsVideoMetadataPayload> getVideoMetadataPayload(@Path("endpointId") String endpointId);

    @GET("feeds/list/{endpointId}/2/public/values?alt=json")
    Call<GoogleSheetsDurationPayload> getDurationPayload(@Path("endpointId") String endpointId);

}

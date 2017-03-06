package com.doyouevenplank.android.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YouTubeApi {

    @GET("videos?part=snippet")
    Call<YouTubeVideoMetadataResponse> getVideoMetadataPayload(@Query("id") String videoId, @Query("key") String apiKey);

}

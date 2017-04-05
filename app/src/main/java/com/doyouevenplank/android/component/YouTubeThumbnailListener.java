package com.doyouevenplank.android.component;

import com.doyouevenplank.android.R;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

public class YouTubeThumbnailListener implements YouTubeThumbnailView.OnInitializedListener, YouTubeThumbnailLoader.OnThumbnailLoadedListener {

    private YouTubeThumbnailLoader mLoader;

    public void releaseLoader() {
        if (mLoader != null) {
            mLoader.release();
        }
    }

    public void loadNewThumbnail(String videoId) {
        if (mLoader != null) {
            mLoader.setVideo(videoId);
        }
    }

    @Override
    public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
        loader.setOnThumbnailLoadedListener(this);
        mLoader = loader;
        view.setImageResource(R.drawable.loading_placeholder);
        String videoId = (String) view.getTag();
        mLoader.setVideo(videoId);
    }

    @Override
    public void onInitializationFailure(YouTubeThumbnailView view, YouTubeInitializationResult loader) {
        view.setImageResource(R.drawable.loading_placeholder);
    }

    @Override
    public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
    }

    @Override
    public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
        view.setImageResource(R.drawable.loading_placeholder);
    }

}

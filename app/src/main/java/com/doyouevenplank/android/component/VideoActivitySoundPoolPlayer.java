package com.doyouevenplank.android.component;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.SparseIntArray;

import com.doyouevenplank.android.R;

public class VideoActivitySoundPoolPlayer {

    private static final float LEFT_VOLUME = 1.f;
    private static final float RIGHT_VOLUME = 1.f;
    private static final int PRIORITY = 0;
    private static final int LOOP = 0;
    private static final float RATE = 1.f;

    private SoundPool mSoundPool;
    private SparseIntArray mSoundIdByResourceId;

    public VideoActivitySoundPoolPlayer(Context context) {
        mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
        mSoundIdByResourceId = new SparseIntArray();

        mSoundIdByResourceId.put(R.raw.ready, mSoundPool.load(context, R.raw.ready, 1));
        mSoundIdByResourceId.put(R.raw.go, mSoundPool.load(context, R.raw.go, 1));
    }

    public void playSoundFromResource(int resource) {
        int soundIdIfNotFound = -1;
        int soundId = mSoundIdByResourceId.get(resource, soundIdIfNotFound);
        if (soundId != soundIdIfNotFound) {
            mSoundPool.play(soundId, LEFT_VOLUME, RIGHT_VOLUME, PRIORITY, LOOP, RATE);
        }
    }

    public void release() {
        mSoundPool.release();
        mSoundPool = null;
    }

}

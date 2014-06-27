package com.ManDraw.navi;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by parkerliu22 on 6/12/14.
 */
public class AudioPlay {
    private static final String LOG_TAG = "MessageReviewAudioPlayTest";
    Context mContext;
    public static MediaPlayer mPlayer;
    private static long audioLength;
    FileManager fileManager = new FileManager();

    public AudioPlay(Context ctx) {
        this.mContext = ctx;
        mPlayer = new MediaPlayer();
    }

    //start or stop playing
    public void onPlay() {
        startPlaying();
    }

    private void stopPlaying() {
        mPlayer.release();
        mPlayer = null;
    }

    public long getReviewAudioLength() {
        audioLength = mPlayer.getDuration();
        return audioLength;
    }

    private void startPlaying() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileManager.getTemp().getPath());
            mPlayer.prepare();
            audioLength = mPlayer.getDuration();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    public static void playFile(File fileToPlay) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(fileToPlay.getPath());
            mPlayer.prepare();
            audioLength = mPlayer.getDuration();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }


    }

    public void mediaDestroy() {
        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }
}

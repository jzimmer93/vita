package com.ManDraw.navi;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;

/**
 * Created by parkerliu22 on 6/12/14.
 */
public class AudioRec {
    static FileManager fileManager = new FileManager();
    private static final String LOG_TAG = "AudioRecordTest";
    Context mContext;
    private MediaRecorder mRecorder = null;
    private static Long tsLong;
    private static String ts;

    public AudioRec(Context ctx) {
        this.mContext = ctx;
    }

    public void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        new vitaSettings();
        FileManager.createTemp();
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(FileManager.type);
        mRecorder.setOutputFile(fileManager.getTemp().getPath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
    }

    private static void renameTemp(String oldName, String newName) {
        File dir = fileManager.getDir();
        File temp_dir = fileManager.getTempDir();
        if (dir.exists()) {
            File from = new File(temp_dir, oldName);
            File to = new File(dir, newName);
            if (from.exists())
                from.renameTo(to);
        }
    }

    public void save() {
        tsLong = System.currentTimeMillis() / 1000;
        ts = tsLong.toString();
        renameTemp(fileManager.getTemp().getName(), ts + FileManager.FILE_TYPE);
    }

    public void mediaRecorderDestroy() {
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }
    }
}
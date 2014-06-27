package com.ManDraw.navi;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.*;
import android.os.Environment;
import android.webkit.WebView.FindListener;
import android.widget.Toast;

import com.ManDraw.navi.vitaSettings;

public class FileManager {
    private File appDir;
    private static File temp_Dir;
    private ArrayList<String> names;
    private static File temp = null;
    public static String FILE_TYPE;

    public static final String PREFERENCES_FILE = "SpinnerPrefs";
    public static final int DEFAULT_POSITION = 1;
    public static final String POSITION_KEY = "Position";
    static SharedPreferences mSharedPreference = MainActivity.ma.getApplicationContext().getSharedPreferences(PREFERENCES_FILE, 0);
    public static int type = mSharedPreference.getInt(POSITION_KEY, DEFAULT_POSITION) + 1;
    public FileManager() {
        try {
            this.appDir = new File(Environment.getExternalStorageDirectory().getCanonicalPath() + "/SoundDiary");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        appDir.mkdir();

        try {
            temp_Dir = new File(appDir.getCanonicalPath() + "/temp");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        temp_Dir.mkdir();

        setFileType(type);

    }

    public File getDir() {
        return this.appDir;
    }

    public File getTempDir() {
        return FileManager.temp_Dir;
    }

    private void createNameList() {
        this.names = new ArrayList<String>();
        if (this.appDir.listFiles() != null) {
            for (File f : this.appDir.listFiles()) {
                if ((f.isFile()) && !(f.getName().endsWith(".jpg"))) {
                    this.names.add(f.getName());
                }
            }
        }
    }

    public String[] getFileNamesArray() {
        createNameList();
        String[] array = new String[this.names.size()];

        for (int i = 0; i < array.length; i++) {
            array[i] = this.names.get(i);
        }

        return array;
    }

    public static void createTemp() {

        type = mSharedPreference.getInt(POSITION_KEY, DEFAULT_POSITION) + 1;
        setFileType(type);

        if (temp != null) {
            temp.delete();
        }

        try {
            temp = File.createTempFile("temp", FILE_TYPE, temp_Dir);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
    }

    public File getTemp() {
        return temp;
    }

    private static void setFileType(int pref)
    {
        switch (pref) {
            case 1:
                FILE_TYPE = ".3gpp";
                break;
            case 2:
                FILE_TYPE = ".mpg4";
                break;
            case 3:
                FILE_TYPE = ".amr";
                break;
        }
    }
}

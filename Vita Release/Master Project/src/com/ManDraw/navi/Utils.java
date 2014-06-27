package com.ManDraw.navi;

/**
 * Created by jonathanzimmer on 6/18/14.
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class Utils {
    public static final String PREFS_NAME = "UtilsPrefsFile";
    private static String ip = "";
    private static Context uContext;

    public Utils(Context ctx) {
        this.uContext = ctx;
    }

    public static String getIP(){
        String ipaddr = getPreferencesString("server_ip");
        return ipaddr;
    }
    public static String getUserID(){
        String uid = getPreferencesString("UserID");
        return uid;
    }
    public static void updateIP(){
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[] { "http://vitaserver.duapp.com/vita/localserver/getip" });
    }
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }
    private static class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                Log.d("The http url", url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(
                            new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            ip = result;
            Log.d("result ", ip);
            savePreferencesString("server_ip",ip);
        }
    }



    public static void savePreferencesBool(String key, boolean value) {
        SharedPreferences settings = uContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static void savePreferencesString(String key, String value) {
        SharedPreferences settings = uContext.getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public boolean getPreferencesBool(String key) {
        SharedPreferences settings = uContext.getSharedPreferences(PREFS_NAME, 0);
        boolean getBool;
        try {
            getBool = settings.getBoolean(key, false);
        } catch (ClassCastException e) {
            Log.e("UserPreferences", "Can't get the UserPrefs");
            getBool = false;
        }
        return getBool;
    }

    public static String getPreferencesString(String key) {
        SharedPreferences settings = uContext.getSharedPreferences(PREFS_NAME, 0);
        String getString;
        try {
            getString = settings.getString(key, null);
        } catch (ClassCastException e) {
            Log.e("UserPreferences", "Can't get the UserPrefs");
            getString = null;
        }
        return getString;
    }

    //check if audio folder exists
    public void setShoutDir() {
        boolean ifShout = this.getPreferencesBool("ShoutDirExists");
        if (!ifShout) {
            File file = new File(uContext.getExternalFilesDir(null), "Shout");
            String path = file.getAbsolutePath();
            if (!file.mkdirs()) {
                Log.e("Create Directory", "Directory not created");
            }
            this.savePreferencesString("ShoutDir", path);
            this.savePreferencesBool("ShoutDirExists", true);
        }
    }
}

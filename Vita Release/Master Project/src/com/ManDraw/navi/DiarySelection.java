package com.ManDraw.navi;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import us.monoid.json.JSONObject;
import us.monoid.web.Resty;

import static us.monoid.web.Resty.content;

/**
 * Created by Carlos on 6/19/2014.
 */
public class DiarySelection {
    Utils dsUtils = new Utils(MainActivity.ma);

    public DiarySelection(final ListView lv, final MyDiaryAdapter adapter, final Fragment fr) {

        final FileManager fileManager = new FileManager();

        lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            public ArrayList<Integer> selections = new ArrayList<Integer>();
            private int nr = 0;

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                // TODO Auto-generated method stub
                selections.clear();
                adapter.clearSelection();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // TODO Auto-generated method stub

                nr = 0;
                MenuInflater inflater = MainActivity.ma.getMenuInflater();
                inflater.inflate(R.menu.file_menu, menu);
                return true;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                // TODO Auto-generated method stub
                switch (item.getItemId()) {
                    case R.id.a_item:
                        for (int i = 0; i < selections.size(); i++) {
                            File aFile = new File(fileManager.getDir() + "/" + fileManager.getFileNamesArray()[selections.get(i)]);
                            File pFile = new File(fileManager.getDir() + "/" + fileManager.getFileNamesArray()[selections.get(i)].substring(0, fileManager.getFileNamesArray()[selections.get(i)].indexOf(".")) + ".jpg");
                            if (aFile.isFile() || pFile.isFile()) {
                                aFile.delete();
                                pFile.delete();
                                adapter.remove(adapter.getItem(selections.get(i)));
                                adapter.notifyDataSetChanged();
//                                fr.getFragmentManager().beginTransaction().replace(R.id.container, new MyDiaryFragment()).commit();
                            }
                        }
                        nr = 0;
                        selections.clear();
                        mode.finish();
                        return true;

//                    case R.id.b_item:
//
//                        AlertDialog.Builder alert = new AlertDialog.Builder(lv.getContext());
//                        if (selections.size() == 1) {
//
//                            final File aFile = new File(fileManager.getDir() + "/" + fileManager.getFileNamesArray()[selections.get(0)]);
//                            final File pFile = new File(fileManager.getDir() + "/" + fileManager.getFileNamesArray()[selections.get(0)].substring(0, fileManager.getFileNamesArray()[selections.get(0)].indexOf(".")) + ".jpg");
//
//                            alert.setTitle("Rename");
//                            alert.setMessage("Enter New File Name:");
//
//                            final EditText input = new EditText(lv.getContext());
//                            alert.setView(input);
//                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    String inValue = input.getText().toString();
//                                    if (!inValue.isEmpty()) {
//                                        new File(fileManager.getDir(), aFile.getName()).renameTo(new File(fileManager.getDir(), inValue + fileManager.FILE_TYPE));
//                                        new File(fileManager.getDir(), pFile.getName()).renameTo(new File(fileManager.getDir(), inValue + ".jpg"));
//                                    }
//                                    selections.clear();
//                                }
//                            });
//
//                            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int whichButton) {
//                                    // Canceled.
//                                }
//                            });
//
//                            alert.show();
//                        } else {
//                            Toast.makeText(MainActivity.ma, "Cannot Rename Multiple Post", Toast.LENGTH_LONG).show();
//                            nr = 0;
//                            adapter.clearSelection();
//                            selections.clear();
//                            mode.finish();
//                        }
//                        return true;

                    case R.id.c_item:
                        if (selections.size() == 1) {
                            File aFile = new File(fileManager.getDir() + "/" + fileManager.getFileNamesArray()[selections.get(0)]);
                            File pFile = new File(fileManager.getDir() + "/" + fileManager.getFileNamesArray()[selections.get(0)].substring(0, fileManager.getFileNamesArray()[selections.get(0)].indexOf(".")) + ".jpg");
                            new upload().execute(aFile.getPath(), pFile.getPath());
                        } else {
                            Toast.makeText(MainActivity.ma, "Multi-Posting Is Not Allowed", Toast.LENGTH_LONG).show();
                            nr = 0;
                            adapter.clearSelection();
                            selections.clear();
                            mode.finish();
                        }
                        selections.clear();
                        return true;
                }
                return false;
            }

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                // TODO Auto-generated method stub
                selections.add(position);
                if (checked) {
                    nr++;
                    adapter.setNewSelection(position, true);
                } else {
                    nr--;
                    adapter.removeSelection(position);
                    selections.remove(position);
                    selections.trimToSize();
                }
                mode.setTitle(nr + " selected");
            }
        });

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int position, long arg3) {
                // TODO Auto-generated method stub

                lv.setItemChecked(position, !adapter.isPositionChecked(position));
                return false;
            }
        });
    }

    private class upload extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String...params) {
            Resty r = new Resty();
            String result;
            try {
                String uid = dsUtils.getUserID();
                JSONObject entryJson = new JSONObject().put("uid", uid);

                //audio
                File audio = new File(params[0]);
                FileInputStream fis = new FileInputStream(audio);
                byte[] b64audio = IOUtils.toByteArray(fis);

                String encodedAudio = Base64.encodeToString(b64audio, Base64.DEFAULT);
                entryJson = entryJson.put("b64audio", encodedAudio);

                //image
//                File pic = new File(params[1]);
                Bitmap bm= BitmapFactory.decodeFile(params[1]);
                int width=bm.getWidth();
                Log.d("Width ", String.valueOf(width));
                int height=bm.getHeight();
                Log.d("height ", String.valueOf(height));

                double ratio=1000.0/(float)width;
                double desiredWidth = 1000;
                double desiredHeight = height * ratio;
                Log.d("height ", String.valueOf(ratio));


                bm=Bitmap.createScaledBitmap(bm, (int)desiredWidth, (int)desiredHeight, true);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
                byte[] b64pic = bos.toByteArray();
//                fis = new FileInputStream(pic);
//                byte[] b64pic = IOUtils.toByteArray(fis);
                if(!bm.isRecycled())
                {
                    bm.recycle();
                    bm = null;

                }

                String encodedPic = Base64.encodeToString(b64pic, Base64.DEFAULT);
                entryJson = entryJson.put("b64pic", encodedPic);
                String ip = dsUtils.getIP();
                String address = ip+ "/vitas";
                Log.d("address ",address);
                result = r.text(address, content(entryJson)).toString();


            }catch(us.monoid.json.JSONException je)
            {
                result = "Post Bundle Error";
            }catch(IOException ioe)
            {
                result = "Connection Error";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(MainActivity.ma, r, Toast.LENGTH_LONG).show();
        }
    }
}

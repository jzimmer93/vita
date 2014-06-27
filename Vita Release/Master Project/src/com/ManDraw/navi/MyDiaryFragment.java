package com.ManDraw.navi;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import static us.monoid.web.Resty.*;
import us.monoid.json.*;
import us.monoid.web.*;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ComponentCallbacks2;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.io.IOUtils;

public class MyDiaryFragment extends Fragment {

    public FileManager fileManager = new FileManager();
    MyDiaryAdapter adapter;
    ListView lv;
    private static final String ARG_PARAM1 = "param1";
    private OnFragmentInteractionListener mListener;

    public static MyDiaryFragment newInstance(int param1) {
        MyDiaryFragment fragment = new MyDiaryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public MyDiaryFragment() {
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<String> lst = new ArrayList<String>();
        lst.addAll(Arrays.asList(fileManager.getFileNamesArray()));
        adapter = new MyDiaryAdapter(inflater.getContext(), lst, fileManager.getFileNamesArray());

        View diaryView = inflater.inflate(R.layout.fragment_my_diary, container, false);
        lv = (ListView) diaryView.findViewById(R.id.recordings);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Toast.makeText(inflater.getContext(), "Playing", Toast.LENGTH_LONG).show();
                AudioPlay.playFile(new File(fileManager.getDir() + "/" + fileManager.getFileNamesArray()[position]));
            }
        });

        DiarySelection ds = new DiarySelection(lv, adapter, this);

        return diaryView;
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_PARAM1));
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}

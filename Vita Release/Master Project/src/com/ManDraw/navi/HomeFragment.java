package com.ManDraw.navi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.web.Resty;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private int mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;
    ListView msgList;
    TextView userText;
    LazyAdapter adapter;
    View homeView;
    String imgUrls[];
    ArrayList<PostDetails> details;
    MediaPlayer mp;
    String url_json = Utils.getIP()+"/vitas/1/all";
    boolean audioLoaded = false;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(int param1) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //adapter = new HomeAdapter(inflater.getContext(), lst, fileManager.getFileNamesArray());
        details = new ArrayList<PostDetails>();
        mp = new MediaPlayer();
        Log.d("ArrayList", "Created");

        homeView = inflater.inflate(R.layout.fragment_home, container, false);

        Context ctx = inflater.getContext();

        msgList = (ListView) homeView.findViewById(R.id.listView1);

        new ConnectToServer_getJASON().execute(url_json);

        msgList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {

               /* Toast.makeText(BrowseActivity.this,
                        details.get(position).getId(), Toast.LENGTH_SHORT)
                        .show();*/

                if (mp.isPlaying() || audioLoaded) {
                    mp.stop();
                    mp.release();
                    mp = new MediaPlayer();
                    audioLoaded = false;
                }

                String url = details.get(position).getAudioPath();
                new ConnectToServer_getAudio().execute(url);

            }
        });
        msgList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                details = new ArrayList<PostDetails>();
                new ConnectToServer_getJASON().execute(url_json);
                Log.v("long clicked", "pos: " + pos);

                return true;
            }
        });


        return homeView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        msgList.setAdapter(null);
    }

    public void update() {

        details = new ArrayList<PostDetails>();

        if (mp.isPlaying()) {
            mp.stop();
            mp.release();
            mp = new MediaPlayer();
        }

        new ConnectToServer_getJASON().execute(url_json);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    private class ConnectToServer_getJASON extends
            AsyncTask<String, String, JSONArray> {

        protected void onPreExecute() {

        }

        protected void onProgressUpdate(String... params) {

        }

        protected JSONArray doInBackground(String... params) {

            Resty r = new Resty();
            JSONArray json = null;
            publishProgress("downloading");

            try {
                json = r.json(params[0]).array();
            } catch (IOException e) {
                publishProgress("Opps! Server Error");
                cancel(true);

            } catch (JSONException e) {

                publishProgress("Opps! Server Error");
                cancel(true);
            }

            return json;
        }

        protected void onCancelled(JSONArray json) {

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

            alertDialog.setTitle("Error...");
            alertDialog.setMessage("Cannot connect to server");

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();

        }

        protected void onPostExecute(JSONArray json) {

            int size = json.length();
            for (int i = 0; i < json.length(); i++) {

                PostDetails Detail;
                Detail = new PostDetails();

                try {
                    String aPath = json.getJSONObject(i).get("audio_path").toString();
                    String pPath = (String) json.getJSONObject(i).get("picture_path");
                    String uName = json.getJSONObject(i).get("username").toString();
                    Detail.setImagePath(pPath);
                    Detail.setAudioPath(aPath);
                    Detail.setUsername(uName);
                    details.add(Detail);
                    msgList.setAdapter(new LazyAdapter(getActivity(), details));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

    }

    private class ConnectToServer_getAudio extends
            AsyncTask<String, String, File> {

        protected void onPreExecute() {


        }

        protected File doInBackground(String... params) {

            Resty r = new Resty();
            File f = null;


            try {
                f = r.bytes(params[0]).save(File.createTempFile("tmp", ".mp3"));
            } catch (IOException e) {
                publishProgress("Opps! Server Error");
                cancel(true);
                e.printStackTrace();
            }

            return f;
        }

        protected void onCancelled(File f) {

            AlertDialog alertDialog = new AlertDialog.Builder(
                    getActivity()).create();

            alertDialog.setTitle("Error...");
            alertDialog.setMessage("Cannot connect to server");

            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            alertDialog.show();

        }

        protected void onPostExecute(File f) {


            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(f);
            } catch (Exception ex) {

            }
            try {
                mp.setDataSource(inputStream.getFD());
            } catch (Exception ex) {

            }

            try {
                inputStream.close();
            } catch (Exception ex) {

            }

            try {
                mp.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            mp.start();
            audioLoaded = true;


        }

    }


}

package com.ManDraw.navi;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiaryRecordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiaryRecordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiaryRecordFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Button mPlayButton;
    private Button mRecButton;
    private Button mSaveButton;

    private AudioRec mAudioRec;
    private AudioPlay mAudioPlay;

    //    boolean mStartPlaying = false;
    boolean mSaveRecording = false;
//    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DiaryRecordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DiaryRecordFragment newInstance(String param1, String param2) {
        DiaryRecordFragment fragment = new DiaryRecordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public DiaryRecordFragment() {
        // Required empty public constructor
        Context ctx = getActivity();
        mAudioPlay = new AudioPlay(ctx);
        mAudioRec = new AudioRec(ctx);
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vi = inflater.inflate(R.layout.fragment_diary_record, container, false);
        mRecButton = (Button) vi.findViewById(R.id.Record);
        mRecButton.setOnClickListener(new View.OnClickListener() {
            boolean mStartRecording = true;

            @Override
            public void onClick(View v) {
                mAudioRec.onRecord(mStartRecording);
                if (mStartRecording) {
                    mRecButton.setBackgroundResource(R.drawable.redstop);
                } else {
                    mRecButton.setBackgroundResource(R.drawable.button);
                    mPlayButton.setVisibility(android.view.View.VISIBLE);
                    mRecButton.setVisibility(android.view.View.INVISIBLE);
                    mSaveRecording = true;
                    //mStartPlaying = true;
                }
                mStartRecording = !mStartRecording;
            }
        });
        mPlayButton = (Button) vi.findViewById(R.id.Play);

        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAudioPlay.onPlay();
                mRecButton.setVisibility(android.view.View.VISIBLE);
                mPlayButton.setVisibility(android.view.View.INVISIBLE);
            }
        });


        mPlayButton.setVisibility(android.view.View.GONE);

        mSaveButton = (Button) vi.findViewById(R.id.Save);

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSaveRecording) {
                    mAudioRec.save();
                    mSaveRecording = !mSaveRecording;
                    mRecButton.setVisibility(android.view.View.VISIBLE);
                    mPlayButton.setVisibility(android.view.View.INVISIBLE);
                }
            }
        });

        return vi;
    }

    // TODO: Rename method, update argument and hook method into UI event

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try {
//            mListener = (OnFragmentInteractionListener) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        public void onFragmentInteraction(Uri uri);
//    }

}

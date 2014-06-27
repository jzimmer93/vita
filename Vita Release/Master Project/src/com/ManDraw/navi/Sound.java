/**
 *
 */
package com.ManDraw.navi;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//import android.support.v4.app.Fragment;


/**
 * @author Mohammad
 *
 */
public class Sound extends Fragment {

    /**
     *
     */
    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private static final String LOG_TAG = "AudioRecordTest";
    private static String mFileName = null;
    private MediaRecorder mRecorder = null;
    private MediaPlayer   mPlayer = null;
    ImageView recordingLightImg = null;
    static final int GALLERY_RESULT = 3;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 4;
    private OnFragmentInteractionListener mListener;
    ImageView mImageView2 = null;
    ImageView imgView = null;
    private String selectedImagePath;
    private static final int SELECT_PICTURE = 2;
    ImageButton imgButton = null;
    ImageButton imgButton2 = null;

    Bitmap myBitmap = null;
    String path = null;
    String PICTURE_PATH = null;
    String AUDIO_PATH = null;
    boolean RECORDED;
    View vi = null;
    ImageButton recordButton = null;
    ImageButton saveButton = null;
    ImageButton playButton = null;
    ImageButton stopButton = null;
    ImageButton galleryButton = null;
    ImageButton captureButton = null;
    ImageView mainPicture = null;
    String FOLDER_PATH = null;
    String TEMP_FOLDER_PATH = null;
    FileManager fm = null;

    private static final String ARG_PARAM1 = "param1";

    public static Sound newInstance(int param1) {
        Sound fragment = new Sound();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    public Sound() {
        // TODO Auto-generated constructor stub
    }



    private FragmentActivity fa;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fm = new FileManager();

        TEMP_FOLDER_PATH =  fm.getTempDir().getPath();
        FOLDER_PATH = fm.getDir().getPath();


        //Toast.makeText(MainActivity.ma.getApplicationContext(),PICTURE_PATH , Toast.LENGTH_SHORT).show();
        //fa = (FragmentActivity) super.getActivity();
        //rl = (RelativeLayout) inflater.inflate(R.layout.fragment_main, container, false);
        View rl = inflater.inflate(R.layout.sounds_and_pictures, container, false);
        vi = rl;

        recordButton = (ImageButton) rl.findViewById(R.id.Record);
        recordButton.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {
                //Toast.makeText(MainActivity.ma.getApplicationContext(), "Record!", Toast.LENGTH_SHORT).show();
                 Toast.makeText(MainActivity.ma.getApplicationContext(), "Record!", Toast.LENGTH_SHORT).show();
                Record(vi);
            }
        });

        saveButton = (ImageButton) rl.findViewById(R.id.Save);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.ma.getApplicationContext(), "Saved!", Toast.LENGTH_SHORT).show();
                Save(vi);
            }
        });

        playButton = (ImageButton) rl.findViewById(R.id.Play);
        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.ma.getApplicationContext(), "Play!", Toast.LENGTH_SHORT).show();
                Play(vi);
            }
        });

        stopButton = (ImageButton) rl.findViewById(R.id.Stop);
        stopButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.ma.getApplicationContext(), "Stop!", Toast.LENGTH_SHORT).show();
                Stop(vi);
            }
        });

        galleryButton = (ImageButton) rl.findViewById(R.id.Gallery);
        galleryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(MainActivity.ma.getApplicationContext(), "Gallery!", Toast.LENGTH_SHORT).show();
                Gallery(vi);
            }
        });

        captureButton = (ImageButton) rl.findViewById(R.id.Capture);
        captureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(MainActivity.ma.getApplicationContext(), "Capture!", Toast.LENGTH_SHORT).show();
                Capture(vi);
            }
        });

        mainPicture = (ImageView) rl.findViewById(R.id.mainPicture);
        mainPicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(MainActivity.ma.getApplicationContext(), "Play!", Toast.LENGTH_SHORT).show();
                Play(vi);
            }
        });




        return vi;
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
    public void Record(View view){



        if(mPlayer == null){
            //plays the sound file
            if(mRecorder == null){
                //set file name
                //String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

                //mFileName = FOLDER_PATH +"/"+timeStamp+".3gp";

                mFileName = TEMP_FOLDER_PATH + "/tempAudio.3gp";
                AUDIO_PATH = mFileName;



                //Toast.makeText(MainActivity.ma.getApplicationContext(), mFileName, Toast.LENGTH_SHORT).show();

                //allocate the recorder
                mRecorder = new MediaRecorder();
                mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                mRecorder.setOutputFile(mFileName);
                mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

                //try to record
                try {
                    mRecorder.prepare();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }


                mRecorder.start();
                if(!RECORDED)
                    RECORDED = true;

                //Display red recording light

                recordingLightImg = null;
                recordingLightImg = (ImageView)view.findViewById(R.id.imageView2);
                recordingLightImg.setVisibility(View.VISIBLE);

                imgButton = null;
                imgButton = (ImageButton)view.findViewById(R.id.Stop);
                imgButton.setVisibility(View.VISIBLE);
                imgButton2 = null;
                imgButton2 = (ImageButton)view.findViewById(R.id.Record);
                imgButton2.setVisibility(View.GONE);

            }//end of if
            else
            {
                Toast.makeText(MainActivity.ma.getApplicationContext(), "Currently recording. Press Stop button!", Toast.LENGTH_SHORT).show();
                //Stop(view);
            }
        }
        else {
            //Toast.makeText(getApplicationContext(), "Already playing. Press Stop button!", Toast.LENGTH_SHORT).show();
            Stop(view);
            Record(view);
        }

    }

    public void Stop(View view) {

        // if it is recording then stop it
        if(mRecorder != null)
        {
            //stop recording

            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;

            //Disable red recording light

            recordingLightImg = null;
            recordingLightImg = (ImageView)view.findViewById(R.id.imageView2);
            recordingLightImg.setVisibility(View.GONE);
            //disappear the stop button
            imgButton = (ImageButton)view.findViewById(R.id.Stop);
            imgButton.setVisibility(View.GONE);
            imgButton = null;
            //reappear the record button
            imgButton = (ImageButton)view.findViewById(R.id.Record);
            imgButton.setVisibility(View.VISIBLE);
        }

        //if it is playing, stop it
        if(mPlayer != null){
            //stop it
            if(mPlayer.isPlaying())
                mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }

    }

    public void Play(View view) {

        if(RECORDED){
            if(mPlayer == null){
                //plays the sound file
                if(mRecorder == null){
                    //set file name

                    //allocate new player
                    mPlayer = new MediaPlayer();

                    //try to start player
                    try {
                        mPlayer.setDataSource(AUDIO_PATH);
                        mPlayer.prepare();

                        mPlayer.start();


                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                }//end of if
                else
                {
                    Toast.makeText(MainActivity.ma.getApplicationContext(), "Currently recording. Press Stop button!", Toast.LENGTH_SHORT).show();
                    //Stop(view);
                }
            }
            else {
                //	Toast.makeText(getApplicationContext(), "Already playing. Press Stop button!", Toast.LENGTH_SHORT).show();


                if(!mPlayer.isPlaying()){
                    Stop(view);
                    Play(view);
                }
                else
                    Stop(view);
                //Play(view);
            }
        }
        else
            Toast.makeText(MainActivity.ma.getApplicationContext(), "Please make a recording to play", Toast.LENGTH_SHORT).show();


    }//end of Play

    public void Save(View view){
        Stop(view);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        NAME_OF_FILE = fm.getDir().getPath()+"/"+timeStamp ;

        if (PICTURE_PATH != null && AUDIO_PATH != null )
        {

            File f = new File(AUDIO_PATH);
            File g = new  File(NAME_OF_FILE+".3gp");
            File h = new File(PICTURE_PATH);
            File i = new File (NAME_OF_FILE+".jpg");
            f.renameTo(g);
            h.renameTo(i);
            //Toast.makeText(MainActivity.ma.getApplicationContext(), PICTURE_PATH  , Toast.LENGTH_SHORT).show();

        }
        else{
            if(PICTURE_PATH == null && AUDIO_PATH == null){
                Toast.makeText(MainActivity.ma.getApplicationContext(), "You have not recorded a sound or taken a picture yet!"  , Toast.LENGTH_SHORT).show();
            }
            else if(AUDIO_PATH == null){
                Toast.makeText(MainActivity.ma.getApplicationContext(), "You have not recorded a sound yet!"  , Toast.LENGTH_SHORT).show();
/*
				File f = new File(AUDIO_PATH);
				File g = new  File(NAME_OF_FILE+".3gp");
                File h = new File(PICTURE_PATH);
                File i = new File (NAME_OF_FILE+".jpg");
				f.renameTo(g);
                h.renameTo(i);
				Toast.makeText(MainActivity.ma.getApplicationContext(), NAME_OF_FILE  , Toast.LENGTH_SHORT).show();
				*/
            }
            else if(PICTURE_PATH == null){
                Toast.makeText(MainActivity.ma.getApplicationContext(), "You have not taken a picture yet!"  , Toast.LENGTH_SHORT).show();

            }


        }

        Toast.makeText(MainActivity.ma.getApplicationContext(), "You have not recorded a sound yet!" , Toast.LENGTH_SHORT).show();

    }


    private  void renameTemp(String oldName, String newName) {
        File dir = fm.getDir();
        File temp_dir = fm.getTempDir();
        if (dir.exists()) {
            File from = new File(temp_dir, oldName);
            File to = new File(dir, newName);
            if (from.exists())
                from.renameTo(to);
        }
    }

    String NAME_OF_FILE = null;
    public void SaveImageFile(String source)
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        NAME_OF_FILE = FOLDER_PATH+"/"+timeStamp ;
        String destinationFilename = NAME_OF_FILE +".jpg";

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(source));
            bos = new BufferedOutputStream(new FileOutputStream(destinationFilename, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while(bis.read(buf) != -1);
        } catch (IOException e) {

        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {

            }
        }
    }

    public void Capture(View view){
        dispatchTakePictureIntent();
    }

    //Gets an img from the gallery
    public void Gallery(View view){
        ContentResolver contentProvider = MainActivity.ma.getContentResolver();
        Intent intent = new Intent();
        intent.setType("image/*");

        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), SELECT_PICTURE);
    }

    //gets new image captured by camera
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(MainActivity.ma.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //////////////activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        //for receiving picture from gallery
        if (resultCode == MainActivity.ma.RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {


                Uri extras = null;

                extras = data.getData();
                path = getPath(extras);
                PICTURE_PATH = path;
                //orient the image
                Bitmap bm = PrepareForPreview(path);



                //////////////////////////////////////////////////////
                ImageView mImageView=null;
                mImageView = (ImageView)vi.findViewById(R.id.mainPicture);
                mImageView.setImageBitmap(bm);



            }
        }

        //for capturing picture

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == MainActivity.ma.RESULT_OK) {
            // Bundle extras2 = data.getExtras();
            //Bitmap bm = (Bitmap) extras2.get("data");


            //set URI
            Uri extras=null;
            extras = data.getData();
            path = getPath(extras);
            Bitmap bm = PrepareForPreview(path);
            PICTURE_PATH = path;




            ImageView mImageView=null;
            mImageView = (ImageView)vi.findViewById(R.id.mainPicture);
            mImageView.setImageBitmap(bm);


            //Toast.makeText(MainActivity.ma.getApplicationContext(), "Goodmoring everyone!" , Toast.LENGTH_SHORT).show();

        }

    }

    public Bitmap PrepareForPreview(String path){

        //allocate matrix for rotation help
        Matrix matrix = new Matrix();
        Bitmap bmO = null;
        //make sure the picture exists
        File imgFile = new File(path);
        if(imgFile.exists()){


            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 2;

            //build bitmap from received img
            bmO = BitmapFactory.decodeFile(path,opts);


            //check rotation and set the matrix to help solve it
            ExifInterface exifReader;
            try {
                exifReader = new ExifInterface(path);
                int orientation = exifReader.getAttributeInt(ExifInterface.TAG_ORIENTATION, -1);

                if (orientation ==ExifInterface.ORIENTATION_NORMAL) {

                    // Do nothing. The original image is fine.
                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {

                    matrix.postRotate(90);

                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {

                    matrix.postRotate(180);

                } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {

                    matrix.postRotate(270);

                }


                //bmO=CompressBitmap(bmO,matrix);
                //Toast.makeText(getApplicationContext(), "Height: "+bm.getHeight() + " Width: "+bm.getWidth(), Toast.LENGTH_SHORT).show();

                int width=bmO.getWidth();
                Log.d("Width ", String.valueOf(width));
                int height=bmO.getHeight();
                Log.d("height ", String.valueOf(height));

                double ratio=1000.0/(float)width;
                double desiredWidth = 1000;
                double desiredHeight = height * ratio;
                Log.d("height ", String.valueOf(ratio));

                bmO = Bitmap.createBitmap(bmO, 0, 0, bmO.getWidth(), bmO.getHeight(), matrix, true);
//                bmO=Bitmap.createScaledBitmap(bmO, (int)desiredWidth, (int)desiredHeight, true);
                Bitmap bitmap = bmO;
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
                if(imgFile.exists()) {
                    imgFile.delete();
                    imgFile = new File(path);
                    imgFile.createNewFile();
                    FileOutputStream fos = new FileOutputStream(imgFile);
                    fos.write(bitmapdata);
                }



            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }


        }
        //Toast.makeText(MainActivity.ma.getApplicationContext(), "Got here!" , Toast.LENGTH_SHORT).show();
        return bmO;

    }
    //Bitmap bm = null;
    Bitmap bm2 = null;
    public Bitmap CompressBitmap(Bitmap bmO, Matrix matrix){



        //	Toast.makeText(getApplicationContext(), "Its recycled!", Toast.LENGTH_SHORT).show();

        int difference = 0;
        int width=bmO.getWidth();
        Log.d("Width ", String.valueOf(width));
        int height=bmO.getHeight();
        Log.d("height ", String.valueOf(height));

        double ratio=1000.0/(float)width;
        double desiredWidth = 1000;
        double desiredHeight = height * ratio;
        Log.d("height ", String.valueOf(ratio));


        bmO=Bitmap.createScaledBitmap(bmO, (int)desiredWidth, (int)desiredHeight, true);

//        //create a bitmap for use with the ImageView
//        if(height>1000){
//           // difference = height - 3996;
//            bmO=Bitmap.createBitmap(bmO, 0, 0, 1000, 1000, matrix, true);
//
//        }
//        else if(width>1000)
//        {
//            //difference = width - 3996;
//            bmO=Bitmap.createBitmap(bmO, 0, 0, 1000, 1000, matrix, true);
//
//
//        }
//        else{
//            bmO=Bitmap.createBitmap(bmO, 0, 0, bmO.getWidth(),bmO.getHeight(), matrix, true);
//
//        }



        //Toast.makeText(MainActivity.ma.getApplicationContext(), "Wasnted recylde!" , Toast.LENGTH_SHORT).show();


        return bmO;
    }
    ////////////////////////
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = MainActivity.ma.managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

}

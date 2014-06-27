package com.ManDraw.navi;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.io.File;
import java.util.HashMap;

public class MyDiaryAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    private HashMap<Integer, Boolean> mSelection = new HashMap<Integer, Boolean>();
    FileManager fl;

    public MyDiaryAdapter(Context context, ArrayList<String> lst, String[] array) {

        super(context, R.layout.my_diary_adapter, lst);
        this.context = context;
        this.values = array;
        fl = new FileManager();
    }

    public void setNewSelection(int position, boolean value) {
        mSelection.put(position, value);
        notifyDataSetChanged();
    }

    public boolean isPositionChecked(int position) {
        Boolean result = mSelection.get(position);
        return result == null ? false : result;
    }

    public void removeSelection(int position) {
        mSelection.remove(position);
        notifyDataSetChanged();
    }

    public void clearSelection() {
        mSelection = new HashMap<Integer, Boolean>();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = inflater.inflate(R.layout.my_diary_adapter, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.file_name);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.post_pic);

        BitmapFactory.Options bOptions = new BitmapFactory.Options();

        bOptions.inSampleSize = 10;
        Bitmap b = null;
        File f = new File(fl.getDir() + "/" + values[position].substring(0, values[position].indexOf(".")) + ".jpg");

        try {
            if (f.exists() && (f.getPath().endsWith(".jpg"))) {

                b = BitmapFactory.decodeFile(f.getPath());

                if (b.getHeight() > 4098 || b.getWidth() > 4098) {
                    bOptions.inMutable = true;
                    b = BitmapFactory.decodeFile(f.getPath(),bOptions);
                }
                textView.setText(values[position]);
                imageView.setImageBitmap(b);
            }
        }catch (OutOfMemoryError memoryError)
        {
            if(b != null) {
                if (!b.isRecycled()) {
                    b.isRecycled();

//                    b = null;
                }
            }
        }
        return rowView;
    }
}

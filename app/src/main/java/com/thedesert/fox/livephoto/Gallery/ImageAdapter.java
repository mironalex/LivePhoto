package com.thedesert.fox.livephoto.Gallery;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.budiyev.android.imageloader.ImageLoader;

import java.io.File;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> imageList = new ArrayList<>();

    public ImageAdapter(Context context) {
        mContext = context;
    }

    public void clear() {
        imageList.clear();
    }

    public void add(String path) {
        imageList.add(path);
    }

    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int i) {
        return imageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        //I am being forced to implement this method, I don't have IDs for my images :\
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ImageView imageView;
        if (view == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(250, 250));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) view;
        }

        File image = new File(imageList.get(i));

        ImageLoader.with(mContext)
                .from(image)
                .size(250, 250)
                .load(imageView);

        //Bitmap bitmap = decodeBitmapFromUri(imageList.get(i), 220, 220);
        //imageView.setImageBitmap(bitmap);

        return imageView;
    }
}

/*
    This decodes the image in the main thread of the app resulting in a janky gallery
    Replaced with a ImageLoader library...

    private Bitmap decodeBitmapFromUri(String path, int requestedWidth, int requestedHeight){
        Bitmap bitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, requestedWidth, requestedHeight);

        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(path, options);

        return bitmap;
    }

    private int calculateInSampleSize(BitmapFactory.Options options,
                                      int requestedWidth,
                                      int requestedHeight){

        int rawHeight = options.outHeight;
        int rawWidth = options.outWidth;
        int inSampleSize = 1;

        if (rawHeight > requestedHeight || rawWidth > requestedWidth){
            if (rawWidth > rawHeight){
                inSampleSize = Math.round((float)rawHeight / (float)requestedHeight);
            } else {
                inSampleSize = Math.round((float)rawWidth / (float)requestedWidth);
            }
        }
        return inSampleSize;
    }
*/


package com.thedesert.fox.livephoto.Gallery;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.File;

public class AsyncTaskLoadFiles extends AsyncTask<Void, String, Void> {

    private File targetDirector;
    private ImageAdapter myTaskAdapter;

    public AsyncTaskLoadFiles(ImageAdapter adapter) {
        myTaskAdapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        String ExternalStorageDirectoryPath = Environment
                .getExternalStorageDirectory().getAbsolutePath();

        String targetPath = ExternalStorageDirectoryPath + "/LivePhoto/.thumbnails/";
        targetDirector = new File(targetPath);
        myTaskAdapter.clear();
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {

        File[] files = targetDirector.listFiles();
        for (File file : files) {
            publishProgress(file.getAbsolutePath());
            if (isCancelled()) break;
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(String... values) {
        myTaskAdapter.add(values[0]);
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(Void result) {
        myTaskAdapter.notifyDataSetChanged();
        super.onPostExecute(result);
    }
}
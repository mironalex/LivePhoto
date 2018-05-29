package com.thedesert.fox.livephoto.Gallery;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.thedesert.fox.livephoto.R;

import java.io.File;
import java.io.IOException;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class GifActivity extends AppCompatActivity {

    public String GifPath;

    private void shareLivePhoto(String photo){
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);

        File gif_file = new File(photo);
        Uri photoUri = Uri.fromFile(gif_file);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, "Send LivePhoto"));
    }

    private void deleteLivePhoto(String photo){
        File gif_file = new File(photo);
        String dir = gif_file.getParentFile().getAbsolutePath();
        File thumb_file = new File(dir + "/.thumbnails/" +
                gif_file.getName().substring(0, gif_file.getName().lastIndexOf(".")) +
                ".jpg");
        boolean deleted = gif_file.delete();
        deleted = thumb_file.delete();
        Toast.makeText(this, "Livephoto deleted.", Toast.LENGTH_SHORT).show();
        this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gif);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            GifPath = extras.getString("gif");
        }

        GifImageView gifImageView = findViewById(R.id.gifView);
        try {
            GifDrawable gif = new GifDrawable(GifPath);
            gifImageView.setBackground(gif);
        } catch (IOException e){
            Log.e("IOException", e.getMessage());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gif, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.share:
                shareLivePhoto(GifPath);
                break;
            case R.id.delete:
                deleteLivePhoto(GifPath);
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

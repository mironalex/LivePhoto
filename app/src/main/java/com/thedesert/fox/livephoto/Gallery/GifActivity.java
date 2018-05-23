package com.thedesert.fox.livephoto.Gallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import com.thedesert.fox.livephoto.R;
import java.io.IOException;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class GifActivity extends AppCompatActivity {

    public String GifPath;

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
}

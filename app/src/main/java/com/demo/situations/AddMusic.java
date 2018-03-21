package com.demo.situations;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.VideoView;

public class AddMusic extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_music);
        String url = getIntent().getStringExtra("selectedSituation");
        Uri situationUrl = Uri.parse(url);
        VideoView preview = findViewById(R.id.situationPreview);
        preview.setVideoURI(situationUrl);
        preview.seekTo(2);
    }
}

package com.gtappdevelopers.netflixapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class VideoDisplayActivity extends AppCompatActivity {

    String videoTitle, videoDesc, videoID, videoCategory;
    private ImageView videoIV;
    private TextView videoTitleTV, videoDescTV, videoCategoryTV;
    private FloatingActionButton playFAB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_display);
        videoTitle = getIntent().getStringExtra("videoTitle");
        videoDesc = getIntent().getStringExtra("videoDesc");
        videoID = getIntent().getStringExtra("videoID");
        videoCategory = getIntent().getStringExtra("videoCategory");
        videoIV = findViewById(R.id.idIVVideo);
        videoTitleTV = findViewById(R.id.idTVVideoTitle);
        videoDescTV = findViewById(R.id.idTVDesc);
        videoCategoryTV = findViewById(R.id.idTVCategory);
        playFAB = findViewById(R.id.idFABPlay);

        playFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VideoDisplayActivity.this, VideoPlayerActivity.class);
                i.putExtra("videoID", videoID);
                startActivity(i);
            }
        });

        String thumbnailUrl = "http://img.youtube.com/vi/" + videoID + "/hqdefault.jpg";
        Picasso.get().load(thumbnailUrl).into(videoIV);
        videoTitleTV.setText(videoTitle);
        videoCategoryTV.setText(videoCategory);
        videoDescTV.setText(videoDesc);

    }
}
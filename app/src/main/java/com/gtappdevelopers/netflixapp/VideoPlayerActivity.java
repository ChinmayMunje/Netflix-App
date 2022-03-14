package com.gtappdevelopers.netflixapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayerActivity extends AppCompatActivity {

    String videoID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_player);
        Intent i=getIntent();
        videoID=  i.getStringExtra("videoID");
        final YouTubePlayerView youTubePlayerView = findViewById(R.id.player);
        youTubePlayerView.enterFullScreen();
        youTubePlayerView.toggleFullScreen();
        getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.getPlayerUiController();
        youTubePlayerView.enterFullScreen();
        youTubePlayerView.toggleFullScreen();


        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // loading the selected video into the YouTube Player
                String id=videoID;
                youTubePlayer.loadVideo(id, 0);
            }

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer,
                                      @NonNull PlayerConstants.PlayerState state) {
                // if video has ended, show an ad
                //  Toast.makeText(VideoPlayer.this, "state changed", Toast.LENGTH_SHORT).show();
                super.onStateChange(youTubePlayer, state);
            }
        });
    }
}
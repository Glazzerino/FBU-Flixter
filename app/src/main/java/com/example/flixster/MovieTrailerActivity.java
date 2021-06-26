package com.example.flixster;

import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import org.jetbrains.annotations.NotNull;

public class MovieTrailerActivity extends YouTubeBaseActivity {

    String videoKey;
    String ytKey;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_trailer);
        youTubePlayerView = findViewById(R.id.player);
        ytKey = getString(R.string.api_key_youtube);

        videoKey = getIntent().getStringExtra("VIDEO_KEY");
        youTubePlayerView.initialize(ytKey, new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo(videoKey);
                player = youTubePlayer;
                player.play();
                // youTubePlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        });

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            player.setFullscreen(true);
            Toast.makeText(MovieTrailerActivity.this, "Fullscreen mode", Toast.LENGTH_SHORT).show();
            Log.d("Trailer", "Fullscren");
        }
    }
}
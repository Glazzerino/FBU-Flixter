package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    private String mvApiKey;
    private String ytKey;
    private String videoKey;
    TextView tvTitle;
    TextView tvDescription;
    RatingBar ratings;
    Movie movie;
    ImageView ivBackdrop;
    ImageView ivPlayIcon;
    float rating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        videoKey = "";
        mvApiKey = getString(R.string.api_key_moviedb);
        ytKey = getString(R.string.api_key_youtube);

        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Movie details: %s", movie.getTitle()));

        ratings = findViewById(R.id.ratings);
        tvTitle = findViewById(R.id.tvTitle);
        tvDescription = findViewById(R.id.tvDesc);
        ivPlayIcon = findViewById(R.id.ivPlayIcon);

        rating = ((float) movie.getRating()) / 2.0f;
        ratings.setRating(rating);
        ivBackdrop = findViewById(R.id.ivVideo);
        tvDescription.setText(movie.getDescription());

        tvDescription.setMovementMethod(new ScrollingMovementMethod());


        String videoAPICallUrl = String.format("https://api.themoviedb.org/3/movie/%s/videos?api_key=%s&language=en-US", movie.getId(), mvApiKey);
        //Fetch youtube
        AsyncHttpClient client = new AsyncHttpClient();
        Log.d("MovieDeta", videoAPICallUrl);
        client.get(videoAPICallUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                JSONObject jsonData = json.jsonObject;
                try {
                    JSONArray results = jsonData.getJSONArray("results");
                    Log.d("MovieDetailsActivity", results.toString());

                    // Grabs the first YT video it finds
                    for (int x = 0; x < results.length(); x++) {
                        JSONObject videoOption = results.getJSONObject(x);
                        if (videoOption.getString("site").equals("YouTube")) {
                            videoKey = videoOption.getString("key");
                            Log.d("MovieDetailsActivity", "Found a vid!");
                            ivPlayIcon.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    Log.d("MovieDetailsActivity", "Could not parse: " + e.toString());
                }
            }
            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Log.e("MovieDetails", "Cout not fetch video options");
            }
        });


        tvTitle.setText(movie.getTitle());
        int placeholder = R.drawable.flicks_movie_placeholder;
        Glide.with(this)
                .load(movie.getBackdropPath())
                .placeholder(placeholder)
                .transform(new CenterCrop(), new RoundedCorners(15))
                .into(ivBackdrop);

        ivBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoKey.length() != 0) {
                    Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                    intent.putExtra("VIDEO_KEY", videoKey);
                    startActivity(intent);
                } else {
                    Toast.makeText(MovieDetailsActivity.this, "Could not find video", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
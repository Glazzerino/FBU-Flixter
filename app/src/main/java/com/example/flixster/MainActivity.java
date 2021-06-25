package com.example.flixster;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.example.flixster.adapters.MovieAdapter;
import com.example.flixster.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {

    //Retrieve key and set api call
    String apiKey;
    String NOW_PLAYING_URL;
    public final String TAG = "MainActivity";
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiKey = getString(R.string.api_key_moviedb);

        NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + apiKey;

        //Setting up the Adapter
        movies = new ArrayList<>();
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        // Setting up recyclerV
        RecyclerView recyclerMovies = findViewById(R.id.recyclerMovies);

        recyclerMovies.setAdapter(movieAdapter);
        recyclerMovies.setLayoutManager(new LinearLayoutManager(this));

        //Fetch data
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(NOW_PLAYING_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int i, Headers headers, JSON json) {
                Log.d(TAG, "onSuccess");
                JSONObject jsonData = json.jsonObject;
                try {

                    JSONArray results = jsonData.getJSONArray("results");
                    Log.i(TAG, "Results: "+ results.toString());
                    movies.addAll(Movie.fromJsonArray(results));
                    movieAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    Log.d(TAG,"Could not parse: " + e.toString());
                }
            }

            @Override
            public void onFailure(int i, Headers headers, String s, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Could not fetch", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure");

            }
        });
    }
}
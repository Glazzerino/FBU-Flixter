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
import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class MainActivity extends AppCompatActivity {
    final String API_KEY = "d88d1baca0e04e252c7d818009c94023";
    final String NOW_PLAYING_URL = "https://api.themoviedb.org/3/movie/now_playing?api_key=d88d1baca0e04e252c7d818009c94023";
    public final String TAG = "MainActivity";
    List<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movies = new ArrayList<>();
        MovieAdapter movieAdapter = new MovieAdapter(this, movies);

        RecyclerView recyclerMovies = findViewById(R.id.recyclerMovies);

        recyclerMovies.setAdapter(movieAdapter);
        recyclerMovies.setLayoutManager(new LinearLayoutManager(this));

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
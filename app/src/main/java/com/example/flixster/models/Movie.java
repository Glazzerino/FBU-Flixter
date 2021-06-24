package com.example.flixster.models;

import com.facebook.stetho.inspector.jsonrpc.JsonRpcException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel

public class Movie {
    String posterPath;
    String title;
    String description;

    String backdropPath;

    public Movie() {

    }

    public Movie(JSONObject json) throws JSONException {
        this.posterPath = json.getString("poster_path");
        this.title = json.getString("title");
        this.description = json.getString("overview");
        this.backdropPath = json.getString("backdrop_path");
    }

    public static List<Movie> fromJsonArray(JSONArray jsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i=0;i<jsonArray.length(); i++) {
            movies.add(new Movie(jsonArray.getJSONObject(i)));
        }

        return movies;
    }

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterPath);
    }
    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropPath);
    }
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

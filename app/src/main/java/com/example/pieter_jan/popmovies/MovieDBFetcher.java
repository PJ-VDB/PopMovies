package com.example.pieter_jan.popmovies;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pieter-jan on 1/17/2017.
 * Fetches the information from the MovieDB website "www.themoviedb.org"
 */

public class MovieDBFetcher {

    // Debugging TAG
    private static final String TAG = "MovieDBFetcher";

    private static final String API_KEY = BuildConfig.MY_API_KEY; // The API key hidden in gradle



    // TODO: declare the statics for the uri request
    // TODO: parse them into a uri


    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String FETCH_POPULAR = "/movie/popular";
    private static final String FETCH_TOP_RATED = "/movie/top_rated";
    private static final String LANGUAGE = "en-US";


    public List<GalleryItem> fetchPopularMovies(){
        String url = buildUrl(FETCH_POPULAR);
        return fetchItems(url);
    }

    public List<GalleryItem> fetchTopMovies(){
        String url = buildUrl(FETCH_TOP_RATED);
        return fetchItems(url);
    }

    /*
    Build the URL string: top rated movies / popular movies
     */
    private String buildUrl(String method){
        Uri.Builder uri = Uri.parse(BASE_URL)
                .buildUpon();
                if(method.equals(FETCH_POPULAR)){
                    uri.appendPath("movie");
                    uri.appendPath("popular");
                }
        if(method.equals(FETCH_TOP_RATED)){
            uri.appendPath("movie");
            uri.appendPath("top_rated");
        }
        uri.appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("language", LANGUAGE);

        String url = uri.build().toString();

        Log.i(TAG, "The API request url is: " + url);

        return url;

    }


    public List<GalleryItem> fetchItems(String url){

        List<GalleryItem> items = new ArrayList<>();
        try {

            String jsonString = getUrlString(url);

            Gson gson = new Gson();
            MovieDB response = gson.fromJson(jsonString, MovieDB.class);
            items = response.getResults();

            Log.i(TAG, "Received JSON " + jsonString);
            Log.i(TAG, "Amount of items: " + items.size());

        }
        catch (IOException ioe) {

            Log.e(TAG, "Failed to fetch items", ioe);
        }

        return items;

    }


    //TODO: replace by Retrofit/Volley later?
    public byte[] getUrlbytes(String urlSpec) throws IOException {
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(); // open a connection to an URL

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream(); // get the inputstream from the open connection

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " +
                        urlSpec);
            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead); // write the inputstream into bytes
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }

    }

    /**
     * Converts the byte result from getUrlBytes to a String
     */
    public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlbytes(urlSpec));
    }


}

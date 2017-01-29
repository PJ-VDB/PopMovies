package com.example.pieter_jan.popmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by pieter-jan on 1/28/2017.
 */

public interface MovieDBApiEndpointInterface {

    @GET("movie/top_rated")
    Call<GalleryItem> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/popular")
    Call<GalleryItem> getPopularMovies(@Query("api_key") String apiKey);


    @GET("movie/{id}/videos")
    Call<MovieVideo.Response> getMovieVideos(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReview.Response> getMovieReviews(@Path("id") int id, @Query("api_key") String apiKey);


}


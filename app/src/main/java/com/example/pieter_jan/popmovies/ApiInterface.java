package com.example.pieter_jan.popmovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by pieter-jan on 1/17/2017.
 */


public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MovieDB> getTopRatedMovies(@Query("api_key") String API_KEY);

    @GET("movie/{id}")
    Call<MovieDB> getMovieDetails(@Path("id") int id, @Query("api_key") String API_KEY);

}

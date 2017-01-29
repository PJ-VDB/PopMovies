package com.example.pieter_jan.popmovies.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.pieter_jan.popmovies.GalleryItem;

import static com.example.pieter_jan.popmovies.database.MoviesContract.PopularMovies;

/**
 * Created by pieter-jan on 11/21/2016.
 *
 * Class that wraps the queries of the cursor in the database
 *
 */

public class MovieCursorWrapper extends CursorWrapper {
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public MovieCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public GalleryItem getFavoriteMovie(){


        String posterPath = getString(getColumnIndex(PopularMovies.MOVIE_POSTER_PATH));
        String overview = getString(getColumnIndex(PopularMovies.MOVIE_OVERVIEW));
        String releaseDate = getString(getColumnIndex(PopularMovies.MOVIE_RELEASE_DATE));
        int movieId = getInt(getColumnIndex(PopularMovies.MOVIE_ID));
        String title = getString(getColumnIndex(PopularMovies.MOVIE_TITLE));
        String backdropPath = getString(getColumnIndex(PopularMovies.MOVIE_BACKDROP_PATH));
        Double popularity = getDouble(getColumnIndex(PopularMovies.MOVIE_POPULARITY));
        int voteCount = getInt(getColumnIndex(PopularMovies.MOVIE_VOTE_COUNT));
        double voteAverage = getDouble(getColumnIndex(PopularMovies.MOVIE_VOTE_AVERAGE));


        GalleryItem galleryItem = new GalleryItem();
        galleryItem.setPosterPath(posterPath);
        galleryItem.setOverview(overview);
        galleryItem.setReleaseDate(releaseDate);
        galleryItem.setId(movieId);
        galleryItem.setBackdropPath(backdropPath);
        galleryItem.setPopularity(popularity);
        galleryItem.setVoteCount(voteCount);
        galleryItem.setVoteAverage(voteAverage);

        return galleryItem;
    }

}

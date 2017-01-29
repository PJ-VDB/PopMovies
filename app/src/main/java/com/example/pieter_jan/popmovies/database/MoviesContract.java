/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.pieter_jan.popmovies.database;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the Popular Movies database.
    Adapted from the sunshine application

 */
public class MoviesContract {

    // The "Content authority" is a name for the entire content provider, similar to the
    // relationship between a domain name and its website.  A convenient string to use for the
    // content authority is the package name for the app, which is guaranteed to be unique on the
    // device.
    public static final String CONTENT_AUTHORITY = "com.example.pieter_jan.popmovies";

    // Use CONTENT_AUTHORITY to create the base of all URI's which apps will use to contact
    // the content provider.
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    // Possible paths (appended to base content URI for possible URI's)
     public static final String PATH_POPULAR = "popular_movies";

    public interface PopularMoviesColumns{
        String MOVIE_ID = "movie_id";
        String MOVIE_TITLE = "movie_title";
        String MOVIE_OVERVIEW = "movie_overview";
        String MOVIE_POPULARITY = "movie_popularity";
        String MOVIE_GENRE_IDS = "movie_genre_ids";
        String MOVIE_VOTE_COUNT = "movie_vote_count";
        String MOVIE_VOTE_AVERAGE = "movie_vote_average";
        String MOVIE_RELEASE_DATE = "movie_release_date";
        String MOVIE_FAVORED = "movie_favored";
        String MOVIE_POSTER_PATH = "movie_poster_path";
        String MOVIE_BACKDROP_PATH = "movie_backdrop_path";
    }


    /* Inner class that defines the table contents of the location table */
    public static final class PopularMovies implements PopularMoviesColumns, BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;

        // Table name
        public static final String TABLE_NAME = "popular_movies";

        // Sort order
        public static final String SORT_ORDER = BaseColumns._ID + " DESC";

        public static Uri buildFavoriteMoviesUri(){
            return BASE_CONTENT_URI.buildUpon()
                    .appendPath(PATH_POPULAR)
                    .build();
        }

        public static Uri buildFavoriteMoviesWithID(int movieId) {
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(movieId)).build();
        }

        public static String getMovieId(Uri uri){
            return uri.getPathSegments().get(2);
        }

    }

}

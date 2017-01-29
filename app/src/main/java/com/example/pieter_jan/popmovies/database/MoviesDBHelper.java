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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.pieter_jan.popmovies.database.MoviesContract.PopularMovies;
import com.example.pieter_jan.popmovies.database.MoviesContract.PopularMoviesColumns;

/**
 * Manages a local database for weather data.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movies.db";

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create a table to hold locations.  A location consists of the string supplied in the
        // location setting, the city name, and the latitude and longitude
        final String SQL_CREATE_POPULAR_MOVIES_TABLE = "CREATE TABLE " + PopularMovies.TABLE_NAME + " (" +
                BaseColumns._ID + " INTEGER NOT NULL PRIMARY KEY,"
                + PopularMoviesColumns.MOVIE_ID + " TEXT NOT NULL,"
                + PopularMoviesColumns.MOVIE_TITLE + " TEXT NOT NULL,"
                + PopularMoviesColumns.MOVIE_OVERVIEW + " TEXT,"
                + PopularMoviesColumns.MOVIE_GENRE_IDS + " TEXT,"
                + PopularMoviesColumns.MOVIE_POPULARITY + " REAL,"
                + PopularMoviesColumns.MOVIE_VOTE_AVERAGE + " REAL,"
                + PopularMoviesColumns.MOVIE_VOTE_COUNT + " INTEGER,"
                + PopularMoviesColumns.MOVIE_BACKDROP_PATH + " TEXT,"
                + PopularMoviesColumns.MOVIE_POSTER_PATH + " TEXT,"
                + PopularMoviesColumns.MOVIE_RELEASE_DATE + " TEXT,"
                + PopularMoviesColumns.MOVIE_FAVORED + " INTEGER NOT NULL DEFAULT 0,"
                + "UNIQUE (" + PopularMoviesColumns.MOVIE_ID + ") ON CONFLICT REPLACE)";

        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        // Note that this only fires if you change the version number for your database.
        // It does NOT depend on the version number for your application.
        // If you want to update the schema without wiping data, commenting out the next 2 lines
        // should be your top priority before modifying this method.
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularMovies.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

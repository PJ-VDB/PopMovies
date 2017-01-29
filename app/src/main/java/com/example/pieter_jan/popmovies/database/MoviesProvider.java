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

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import static com.example.pieter_jan.popmovies.database.MoviesContract.CONTENT_AUTHORITY;
import static com.example.pieter_jan.popmovies.database.MoviesContract.PopularMovies;

public class MoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mOpenHelper;

    static final int POPULAR_MOVIES = 100;
    static final int MOVIES_ID = 101;

    private static  UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = CONTENT_AUTHORITY;

        matcher.addURI(authority, PopularMovies.TABLE_NAME, POPULAR_MOVIES);
        matcher.addURI(authority, PopularMovies.TABLE_NAME + "/#", MOVIES_ID);

        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDBHelper(getContext());
        return true;
    }



    /*
        Students: Here's where you'll code the getType function that uses the UriMatcher.  You can
        test this by uncommenting testGetType in TestProvider.

     */
    @Override
    public String getType(Uri uri) {

        // Use the Uri Matcher to determine what kind of URI this is.
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case POPULAR_MOVIES:
                return PopularMovies.CONTENT_TYPE;
            case MOVIES_ID:
                return PopularMovies.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        // Here's the switch statement that, given a URI, will determine what kind of request it is,
        // and query the database accordingly.
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "weather"
            case POPULAR_MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PopularMovies.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIES_ID:
                retCursor = mOpenHelper.getReadableDatabase().query(
                        PopularMovies.MOVIE_ID + " = ?", //Todo, not sure if it should be movie_id
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /*
        Student: Add the ability to insert Locations to the implementation of this function.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case POPULAR_MOVIES: {
                long _id = db.insert(PopularMovies.TABLE_NAME, null, values);
                if (_id > 0) {
                        returnUri = PopularMovies.buildFavoriteMoviesUri();
                    } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case POPULAR_MOVIES:
                rowsDeleted = db.delete(
                        PopularMovies.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIES_ID:
                rowsDeleted = db.delete(PopularMovies.TABLE_NAME,
                        PopularMovies.MOVIE_ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case POPULAR_MOVIES:
                rowsUpdated = db.update(PopularMovies.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case MOVIES_ID: {
                rowsUpdated = db.update(PopularMovies.TABLE_NAME,
                        values,
                        PopularMovies.MOVIE_ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
//
//    @Override
//    public int bulkInsert(Uri uri, ContentValues[] values) {
//        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
//        final int match = sUriMatcher.match(uri);
//        switch (match) {
//            case WEATHER:
//                db.beginTransaction();
//                int returnCount = 0;
//                try {
//                    for (ContentValues value : values) {
//                        normalizeDate(value);
//                        long _id = db.insert(WeatherContract.WeatherEntry.TABLE_NAME, null, value);
//                        if (_id != -1) {
//                            returnCount++;
//                        }
//                    }
//                    db.setTransactionSuccessful();
//                } finally {
//                    db.endTransaction();
//                }
//                getContext().getContentResolver().notifyChange(uri, null);
//                return returnCount;
//            default:
//                return super.bulkInsert(uri, values);
//        }
//    }

    // You do not need to call this method. This is a method specifically to assist the testing
    // framework in running smoothly. You can read more at:
    // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
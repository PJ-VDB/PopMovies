//package com.example.pieter_jan.popmovies.database;
//
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//
//import com.example.pieter_jan.popmovies.GalleryItem;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by pieter-jan on 1/28/2017.
// *
// * Database with the favorited movies
// */
//
//public class PopularMovieDatabase {
//
//    private static final String TAG = "PopularMovieDatabase";
//    private static PopularMovieDatabase sPopularMovieDatabase; // static variable
//
//    // SQLite db
//    private Context mContext;
//    private SQLiteDatabase mDatabase;
//
//    public static PopularMovieDatabase get(Context context) {
//        if (sPopularMovieDatabase == null) {
//            sPopularMovieDatabase = new PopularMovieDatabase(context);
//        }
//        return sPopularMovieDatabase;
//    }
//
//    private PopularMovieDatabase(Context context) {
//
//        // create a SQLite database
//        mContext = context.getApplicationContext();
//        mDatabase = new MoviesDBHelper(mContext).getWritableDatabase();
//    }
//
//    public List<GalleryItem> getFavoriteMovies() {
//
//        List<GalleryItem> movies = new ArrayList<>();
//        MovieCursorWrapper cursor = queryMovies(null, null);
//
//        try {
//            cursor.moveToFirst();
//            while (!cursor.isAfterLast()) {
//                movies.add(cursor.getFavoriteMovie());
//                cursor.moveToNext();
//            }
//        } finally {
//            cursor.close();
//        }
//
//        return movies;
//    }
//
//    public GalleryItem getMovie(int movieId) {
//        MovieCursorWrapper cursor = queryMovies(MoviesContract.PopularMovies.MOVIE_ID + " = ?",
//                new String[]{Integer.toString(movieId)});
//
//        try {
//            if (cursor.getCount() == 0) {
//                return null; // there are no crimes
//            }
//
//            cursor.moveToFirst();
//            return cursor.getFavoriteMovie();
//        } finally {
//            cursor.close(); // again close the cursor to avoid nasty errors
//        }
//    }
//
//    // query the movies
//    private MovieCursorWrapper queryMovies(String whereClause, String[] whereArgs) {
//        Cursor cursor = mDatabase.query(
//                MoviesContract.PopularMovies.TABLE_NAME,
//                null,
//                whereClause,
//                whereArgs, null,
//                null,
//                null
//        );
//
//        return new MovieCursorWrapper(cursor);
//    }
//
//    public void addMovie(GalleryItem galleryItem)
//}
//
//

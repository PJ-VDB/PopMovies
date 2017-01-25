package com.example.pieter_jan.popmovies;

import android.content.Context;
import android.preference.PreferenceManager;

/**
* Class with the SharedPreferences of PopMovies (used for the sorting)
 */

public class QueryPreferences {
    private static final String PREF_SORT_ORDER = "sortOrder"; // the key for sorting results
    private static final String PREF_LAST_RESULT_ID  = "lastResultId";

    /*
    Read the query from shared preferences
    (does not have a context of its own, that's why a context needs to be passed in)
     */
    public static String getStoredOrder(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_SORT_ORDER, null);
    }

    /*
    Write the query to shared preferences
     */
    public static void setStoredOrder(Context context, String query){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_SORT_ORDER, query)
                .apply();
    }

    // Poll Flickr to check the latest id (of the results)
    public static String getPrefLastResultId(Context context){
        return PreferenceManager.getDefaultSharedPreferences(context)
                .getString(PREF_LAST_RESULT_ID, null);
    }

    public static void setLastResultId(Context context, String lastResultId){
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(PREF_LAST_RESULT_ID, lastResultId)
                .apply();
    }

}

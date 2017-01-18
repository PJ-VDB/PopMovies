package com.example.pieter_jan.popmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class MovieGalleryActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return MovieGalleryFragment.newInstance();
    }

    public static Intent newIntent (Context context){
        return new Intent(context, MovieGalleryActivity.class);
    }
}

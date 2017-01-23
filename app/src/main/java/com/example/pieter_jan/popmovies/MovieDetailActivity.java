package com.example.pieter_jan.popmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class MovieDetailActivity extends SingleFragmentActivity {

    private static final String EXTRA_GALLERY_ITEM = "com.example.pieterjan.criminalintent.gallery_item";

    @Override
    protected Fragment createFragment() {

        GalleryItem galleryItem = getIntent().getParcelableExtra(EXTRA_GALLERY_ITEM);

        return MovieDetailFragment.newInstance(galleryItem);
    }

    public static Intent newIntent(Context packageContext, GalleryItem galleryItem){
        Intent intent = new Intent(packageContext, MovieDetailActivity.class);
        intent.putExtra(EXTRA_GALLERY_ITEM, galleryItem);
        return intent;
    }

}

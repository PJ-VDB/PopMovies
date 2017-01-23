//package com.example.pieter_jan.popmovies;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
//
///**
// * Created by pieter-jan on 1/21/2017.
// */
//
//public class MoviePagerActivity extends AppCompatActivity{
//
//    private static final String EXTRA_GALLERY_ITEM = "com.example.pieter_jan.popmovies.gallery_item";
//    private ViewPager mViewPager;
//
//
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_movie_pager);
//
//        GalleryItem galleryItem = getIntent().getParcelableExtra(EXTRA_GALLERY_ITEM);
//
//        mViewPager = (ViewPager) findViewById(R.id.activity_movie_pager_view_pager);
//
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        mViewPager.setAdapter(new FragmentPagerAdapter() {
//            @Override
//            public Fragment getItem(int position) {
//                return null;
//            }
//
//            @Override
//            public int getCount() {
//                return 0;
//            }
//        });
//
//    }
//}

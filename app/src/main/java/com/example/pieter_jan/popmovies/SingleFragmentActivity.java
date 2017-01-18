package com.example.pieter_jan.popmovies;

/**
 * Created by pieter-jan on 12/1/2016.
 * An abstract class that creates a fragment
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;


public abstract class SingleFragmentActivity extends AppCompatActivity {

    protected abstract android.support.v4.app.Fragment createFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
//        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null){
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)
                    .commit();
        }
    }

//    // Annotation tells Android Studio that any implementation of this method should return a valid layout resource ID
//    @LayoutRes
//    protected int getLayoutResId() {
//        return R.layout.activity_masterdetail;
//    }
}

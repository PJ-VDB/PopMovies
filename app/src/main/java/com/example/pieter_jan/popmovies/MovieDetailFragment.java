package com.example.pieter_jan.popmovies;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pieter-jan on 11/15/2016.
 */



public class MovieDetailFragment extends Fragment {

    private static final String TAG = "MovieDetailFragment";
    private static final String ARG_GALLERY_ITEM = "gallery_item";


    private GalleryItem mGalleryItem;

    @BindView(R.id.fragment_movie_detail_title) TextView mMovieTitle;
    @BindView(R.id.fragment_movie_detail_description) TextView mMovieDescription;
    @BindView(R.id.fragment_movie_detail_thumbnail) ImageView mMovieThumbnail;
    @BindView(R.id.fragment_movie_detail_release_date) TextView mMovieReleaseDate;
    @BindView(R.id.fragment_movie_detail_rating) TextView mMovieRating;
    @BindView(R.id.black_mask) View mBlackMask;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         mGalleryItem = getArguments().getParcelable(ARG_GALLERY_ITEM);

//        Log.d(TAG, "Galleryitem title: " + mGalleryItem.getTitle());

        setHasOptionsMenu(true);


    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_movie_detail_test, container, false); // second parameter is the view's parent;

        ButterKnife.bind(this, v);

        // Set all the views
        mMovieTitle.setText(mGalleryItem.getTitle());
        mMovieDescription.setText(mGalleryItem.getOverview());
        mMovieRating.setText(mGalleryItem.getVoteAverage().toString());
        mMovieReleaseDate.setText(mGalleryItem.getReleaseDate());
        Picasso.with(getActivity())
                .load(mGalleryItem.getFullPosterPathw342())
//                    .placeholder(R.drawable.bill_up_close)
                .fit()
                .into(mMovieThumbnail, new Callback() {
                            @Override
                            public void onSuccess() {
                                mBlackMask.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onError() {

                            }
                        });

        return v;
    }



    //TODO: check this
    // Create a bundle with the arguments that are connected to the CrimeFragment;
    // Has to happen after the Fragment gets created but Before it is added to the activity
    public static MovieDetailFragment newInstance(GalleryItem galleryItem){
        Bundle args = new Bundle();
//        args.putSerializable(ARG_CRIME_ID, crimeId);
        args.putParcelable(ARG_GALLERY_ITEM, galleryItem);


        MovieDetailFragment fragment = new MovieDetailFragment(); // Create a new fragment using the self implemented abstract class SimpleFragment
        fragment.setArguments(args);
        return fragment;
    }



}

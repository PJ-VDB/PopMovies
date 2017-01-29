package com.example.pieter_jan.popmovies;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pieter-jan on 11/15/2016.
 */



public class MovieDetailFragment extends Fragment {

    private static final String TAG = "MovieDetailFragment";
    private static final String ARG_GALLERY_ITEM = "gallery_item";

    private GalleryItem mGalleryItem;

    @BindView(R.id.fragment_movie_detail_title)
    TextView mMovieTitle;
    @BindView(R.id.fragment_movie_detail_description)
    TextView mMovieDescription;
    @BindView(R.id.fragment_movie_detail_thumbnail)
    ImageView mMovieThumbnail;
    @BindView(R.id.fragment_movie_detail_release_date)
    TextView mMovieReleaseDate;
    @BindView(R.id.fragment_movie_detail_rating)
    TextView mMovieRating;
    @BindView(R.id.black_mask)
    View mBlackMask;

    private static final String API_KEY = BuildConfig.MY_API_KEY; // The API key hidden in gradle
    private List<MovieVideo> mMovieVideos;
    private List<MovieReview> mMovieReviews;


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
        View v = inflater.inflate(R.layout.fragment_movie_detail, container, false); // second parameter is the view's parent;

        ButterKnife.bind(this, v);

        Log.i(TAG, "Movie ID: " + mGalleryItem.getId());

        // Obtain trailers and reviews
        MovieDBApiEndpointInterface apiService =
                RetrofitFetcher.getClient().create(MovieDBApiEndpointInterface.class);

        fetchMovieReviews(apiService);
        fetchMovieVideos(apiService);

        // Set all the views
        mMovieTitle.setText(mGalleryItem.getTitle());
//        mMovieTitle.setText(mMovieVideos.get(0).getSite());
        mMovieDescription.setText(mGalleryItem.getOverview());
        mMovieRating.setText(mGalleryItem.getVoteAverage().toString());
        mMovieReleaseDate.setText(mGalleryItem.getReleaseDate());
        Picasso.with(getActivity())
                .load(mGalleryItem.getFullPosterPathw342())
//                    .placeholder(R.drawable.bill_up_close)
                .fit()
//                .into(mMovieThumbnail)
                .into(mMovieThumbnail, new com.squareup.picasso.Callback() {
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
    public static MovieDetailFragment newInstance(GalleryItem galleryItem) {
        Bundle args = new Bundle();
//        args.putSerializable(ARG_CRIME_ID, crimeId);
        args.putParcelable(ARG_GALLERY_ITEM, galleryItem);


        MovieDetailFragment fragment = new MovieDetailFragment(); // Create a new fragment using the self implemented abstract class SimpleFragment
        fragment.setArguments(args);
        return fragment;
    }

    private void fetchMovieVideos(MovieDBApiEndpointInterface apiService){
        Call<MovieVideo.Response> callVideos = apiService.getMovieVideos(mGalleryItem.getId(), API_KEY);
        callVideos.enqueue(new Callback<MovieVideo.Response>() {
            @Override
            public void onResponse(Call<MovieVideo.Response> call, Response<MovieVideo.Response> response) {
                mMovieVideos = response.body().getMovieVideos();
                Log.d(TAG, "Number of movies received: " + mMovieVideos.size());
            }

            @Override
            public void onFailure(Call<MovieVideo.Response> call, Throwable t) {
                Log.e(TAG, t.toString());

            }
        });
    }

    private void fetchMovieReviews(MovieDBApiEndpointInterface apiService){
        Call<MovieReview.Response> callReviews = apiService.getMovieReviews(mGalleryItem.getId(), API_KEY);
        callReviews.enqueue(new Callback<MovieReview.Response>() {
            @Override
            public void onResponse(Call<MovieReview.Response> call, Response<MovieReview.Response> response) {
                mMovieReviews = response.body().getMovieReviews();
                Log.d(TAG, "Number of reviews received: " + mMovieReviews.size());
            }
            @Override
            public void onFailure(Call<MovieReview.Response> call, Throwable t) {
                Log.e(TAG, t.toString());

            }
        });
    }

//    public void playVideo(Video video) {
//        if (video.getSite().equals(Video.SITE_YOUTUBE)) {
//            mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + video.getKey())));
//        }
//    }


}

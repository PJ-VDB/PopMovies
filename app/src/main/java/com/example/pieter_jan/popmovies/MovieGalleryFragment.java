package com.example.pieter_jan.popmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pieter-jan on 1/16/2017.
 */
public class MovieGalleryFragment extends Fragment{

    // TAG for debugging
    private static final String TAG = "MovieGalleryFragment";

    // Setting up the recyclerview
    @BindView(R.id.fragment_movie_gallery_recycler_view) RecyclerView mMovieRecyclerView;

    private List<GalleryItem> mItems = new ArrayList<>();

    public static MovieGalleryFragment newInstance() {
        return new MovieGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // Do it like this temporarily, easiest way to save state
        setHasOptionsMenu(false); // TODO: add options menu for displaying results

        updateItems();
    }

    //TODO: implement options
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_gallery, container, false);

        // Bind the recyclerview using butter knife
        ButterKnife.bind(this, view);
        mMovieRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));

        setupAdapter();

        return view;

    }


    /**
     * Set up the adapter to the RecyclerView
     */
    private void setupAdapter() {
        if(isAdded()){ // confirms that the fragment has been added to an activity
            mMovieRecyclerView.setAdapter(new MovieAdapter(mItems));
            Log.d(TAG, "Adapter created");
        }
    }

    private void updateItems(){
        if (mMovieRecyclerView != null){
            mItems.clear();
            mMovieRecyclerView.getAdapter().notifyDataSetChanged();
        }

        new FetchItemTask(null).execute();
        Log.d(TAG, "The size of mItems is :" + mItems.size());

    }

//    private List<GalleryItem> fetchItems(){
//        mItems = new MovieDBFetcher().fetchTopRatedMovies();
//        return mItems;
//
//    }

    // TODO: asynctask to do the background work
    /*
    Class to create a background thread for the network connection, networking is not allowed on the main thread (UI thread).
     */
    public class FetchItemTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        private String mQuery;

        public FetchItemTask(String query){
            mQuery = query;
        }

        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {

            return new MovieDBFetcher().fetchPopularMovies();

        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {
            mItems = galleryItems;
            setupAdapter();
        }
    }

    /**
     * ViewHolder class
     */
    private class MovieHolder extends RecyclerView.ViewHolder{

//        private ImageView mItemImageView;
//        private TextView mItemMovieTitle;

//        private ImageView mItemImageView;
        private TextView mItemTitle;

        public MovieHolder(View itemView) {
            super(itemView);

              mItemTitle = (TextView) itemView.findViewById(R.id.fragment_movie_gallery_title);
//            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_movie_gallery_image_view);
        }

        // Bind the image to the ViewHolder
//        public void bindDrawable(Drawable drawable) {
//            mItemImageView.setImageDrawable(drawable);
//        }

//        public void bindGalleryItem(GalleryItem galleryItem){
//            Picasso.with(getActivity())
//                    .load(galleryItem.getUrl())
//                    .placeholder(R.drawable.bill_up_close)
//                    .into(mItemImageView);
//
//        }
    }

    /**
     * Adapter class
     */
    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {

        private List<GalleryItem> mGalleryItems;

        public MovieAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }

        @Override
        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, parent, false);
            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(MovieHolder holder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);

//            Drawable placeHolder = getResources().getDrawable(R.drawable.bill_up_close);
//            holder.bindDrawable(placeHolder); // Replace by Picasso

            holder.mItemTitle.setText(galleryItem.getTitle());

            //TODO: add the downloaded image of the movie
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
//            return 0;
        }
    }


}

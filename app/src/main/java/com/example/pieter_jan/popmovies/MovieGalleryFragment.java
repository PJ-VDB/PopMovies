package com.example.pieter_jan.popmovies;

import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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

    // The navigation drawer
    @BindView(R.id.left_drawer) ListView mDrawerList;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    ActionBar mActionBar;

    public static MovieGalleryFragment newInstance() {
        return new MovieGalleryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // Do it like this temporarily, easiest way to save state
        setHasOptionsMenu(true); // TODO: add options menu for displaying results

        mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);


        // Populate the list
        updateItems();
    }

    //TODO: implement options
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_movie_gallery, container, false);

        // Bind the view (recycler & drawer) using butter knife
        ButterKnife.bind(this, view);

        // RecyclerView
        mMovieRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setupAdapter();

        mActivityTitle = getActivity().getTitle().toString();

        // The navigation drawer
        addDrawerItems();
        setupDrawer();

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDrawerToggle.syncState();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
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

    /*
    Set up the navigation drawer
     */
    private void addDrawerItems(){
        String[] choiceArray = {"Popular movies", "Top rated movies"};
        mAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, choiceArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(getActivity(), "Time for an upgrade!" + position, Toast.LENGTH_SHORT).show();

                if (position == 0){
                    QueryPreferences.setStoredOrder(getActivity(), "popular");
                    updateItems();
                }


                if (position == 1){
                    QueryPreferences.setStoredOrder(getActivity(), "top");
                    updateItems();
                }

            }
        });
    }

    /*
    Called after drawer items have been set up
     */
    private void setupDrawer(){
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                mActionBar.setTitle("Options");
                getActivity().invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                mActionBar.setTitle(mActivityTitle);
                getActivity().invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void updateItems(){

        String sortOrder = QueryPreferences.getStoredOrder(getActivity());

        if (mMovieRecyclerView != null){
            mItems.clear();
            mMovieRecyclerView.getAdapter().notifyDataSetChanged();
        }

        new FetchItemTask(sortOrder).execute();
        Log.d(TAG, "The size of mItems is :" + mItems.size());

    }

    /*
    Class to create a background thread for the network connection, networking is not allowed on the main thread (UI thread).
     */
    public class FetchItemTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        private String mSortOrder;

        public FetchItemTask(String sortOrder){
            mSortOrder = sortOrder;
        }

        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {

            if (mSortOrder=="popular") {
                return new MovieDBFetcher().fetchPopularMovies();
            }
            if (mSortOrder=="top"){
                return new MovieDBFetcher().fetchTopMovies();
            }
            else {
                return new MovieDBFetcher().fetchPopularMovies();
            }

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


        private ImageView mItemImageView;
        private TextView mItemTitle;
        private TextView mItemDescription;

        public MovieHolder(View itemView) {
            super(itemView);

                mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_movie_item_thumbnail);
                mItemTitle = (TextView) itemView.findViewById(R.id.fragment_movie_item_title);
                mItemDescription = (TextView) itemView.findViewById(R.id.fragment_movie_item_description);

        }
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

            Picasso.with(getActivity())
                    .load(galleryItem.getFullPosterPathw342())
                    .placeholder(R.drawable.bill_up_close)
                    .fit()
                    .into(holder.mItemImageView);

//            Log.d(TAG, "onBindViewHolder: " + galleryItem.getFullPosterPathw185());

            holder.mItemTitle.setText(galleryItem.getTitle()); // Set title
            holder.mItemDescription.setText(galleryItem.getOverview()); //Set Description
        }

        @Override
        public int getItemCount() {
            return mGalleryItems.size();
        }
    }


}

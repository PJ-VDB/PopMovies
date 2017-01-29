package com.example.pieter_jan.popmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
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

import com.example.pieter_jan.popmovies.database.MovieCursorWrapper;
import com.example.pieter_jan.popmovies.database.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.CONNECTIVITY_SERVICE;
import static com.example.pieter_jan.popmovies.QueryPreferences.setStoredOrder;

/**
 * Created by pieter-jan on 1/16/2017.
 */
public class MovieGalleryFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    // TAG for debugging
    private static final String TAG = "MovieGalleryFragment";

    // Setting up the recyclerview
    @BindView(R.id.fragment_movie_gallery_recycler_view) RecyclerView mMovieRecyclerView;
    @BindView(R.id.no_connection) TextView mNoConnectionTextView;

    private List<GalleryItem> mItems = new ArrayList<>();

    // the adapter
    private MovieAdapter mMovieAdapter;

    // The navigation drawer
    @BindView(R.id.left_drawer) ListView mDrawerList;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;
    ActionBar mActionBar;

    // Solution for endless scrolling
    private int lastFetchedPage = 1;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 5;
    int firstVisibleItem, visibleItemCount, totalItemCount;

    // Check network connection
    boolean mNetworkConnected = false;

    // Loader for the database
    private static final int CURSOR_LOADER_ID = 0;

    // the columns of the database

    static final String[] MOVIE_COLUMNS = {
            MoviesContract.PopularMovies._ID,
            MoviesContract.PopularMovies.MOVIE_ID,
            MoviesContract.PopularMovies.MOVIE_TITLE,
            MoviesContract.PopularMovies.MOVIE_OVERVIEW,
            MoviesContract.PopularMovies.MOVIE_POPULARITY,
            MoviesContract.PopularMovies.MOVIE_GENRE_IDS,
            MoviesContract.PopularMovies.MOVIE_VOTE_COUNT,
            MoviesContract.PopularMovies.MOVIE_VOTE_AVERAGE,
            MoviesContract.PopularMovies.MOVIE_RELEASE_DATE,
            MoviesContract.PopularMovies.MOVIE_FAVORED,
            MoviesContract.PopularMovies.MOVIE_POSTER_PATH,
            MoviesContract.PopularMovies.MOVIE_BACKDROP_PATH
    };

    static final int COL_CURSOR_ID = 0; // required for the cursor
    static final int COL_MOVIE_ID = 1;
    static final int COL_MOVIE_TITLE = 2;
    static final int COL_MOVIE_OVERVIEW = 3;
    static final int COL_MOVIE_POPULARITY = 4;
    static final int COL_MOVIE_GENRE_IDS = 5;
    static final int COL_MOVIE_VOTE_COUNT = 6;
    static final int COL_MOVIE_VOTE_AVERAGE = 7;
    static final int COL_MOVIE_RELEASE_DATE = 8;
    static final int COL_MOVIE_FAVORED = 9;
    static final int COL_MOVIE_POSTER_PATH = 10;
    static final int COL_MOVIE_BACKDROP_PATH = 11;


    public static MovieGalleryFragment newInstance() {
        return new MovieGalleryFragment();
    }

//    /*
//    Avoid disappearing of navigation bar; TODO: better fix for this
//     */
    @Override
    public void onResume() {
        super.onResume();
        setupDrawer();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); // Do it like this temporarily, easiest way to save state
        setHasOptionsMenu(true); // TODO: add options menu for displaying results

        initializeSortOrder();
        mNetworkConnected = isNetworkAvailableAndConnected();

        if(mNetworkConnected) {
            mActionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setHomeButtonEnabled(true);

            updateItems();
        }

    }



    private void initializeSortOrder() {
        if(QueryPreferences.getStoredOrder(getActivity()) == null) {
            QueryPreferences.setStoredOrder(getActivity(), "popular"); // Set the initial sort order to popular, this needs to be changed later if setRetainInstance is not true
            Log.i(TAG, "Sort order initialized");
        }
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


        if(mNetworkConnected) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
            mMovieRecyclerView.setLayoutManager(gridLayoutManager);

            mMovieRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    //Gridlayout
                    visibleItemCount = mMovieRecyclerView.getChildCount();
                    totalItemCount = gridLayoutManager.getItemCount();
                    firstVisibleItem = gridLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if (totalItemCount > previousTotal) {
                            loading = false;
                            previousTotal = totalItemCount;
                        }
                    }
                    if (!loading && (totalItemCount - visibleItemCount)
                            <= (firstVisibleItem + visibleThreshold)) {
                        // End has been reached

                        new FetchItemTask(QueryPreferences.getStoredOrder(getActivity()), lastFetchedPage).execute();

                        loading = true;
                    }

                }

            });

            setupAdapter();

            // The navigation drawer
            mActivityTitle = getActivity().getTitle().toString();
            addDrawerItems();
            setupDrawer();
        } else {
            mNoConnectionTextView.setVisibility(View.VISIBLE);
        }

        return view;

    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);


// network status
        if(mNetworkConnected) {
            mDrawerToggle.syncState();
        }
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(mNetworkConnected) {
            mDrawerToggle.onConfigurationChanged(newConfig);
        }
    }

    /**
     * Set up the adapter to the RecyclerView
     */
    private void setupAdapter() {
        if(isAdded()){ // confirms that the fragment has been added to an activity
           mMovieAdapter = new MovieAdapter(mItems);
            mMovieRecyclerView.setAdapter(mMovieAdapter);
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
                    String oldOrder  = QueryPreferences.getStoredOrder(getActivity());
                    QueryPreferences.setStoredOrder(getActivity(), "popular");
                    if (oldOrder.equals("top")) {
                        updateItems();
                    }
                }


                if (position == 1){
                    String oldOrder  = QueryPreferences.getStoredOrder(getActivity());
                    setStoredOrder(getActivity(), "top");
                    if (oldOrder.equals("popular")) {
                        updateItems();
                    }
                }

            }
        });
    }

    /*
    Called after drawer items have been set up
     */
    private void setupDrawer(){
        if(mNetworkConnected) {
            mDrawerToggle = new ActionBarDrawerToggle(getActivity(), mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
                /**
                 * Called when a drawer has settled in a completely open state.
                 */
                public void onDrawerOpened(View drawerView) {
                    super.onDrawerOpened(drawerView);
                    mActionBar.setTitle("Options");
                    getActivity().invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }

                /**
                 * Called when a drawer has settled in a completely closed state.
                 */
                public void onDrawerClosed(View view) {
                    super.onDrawerClosed(view);
                    mActionBar.setTitle(mActivityTitle);
                    getActivity().invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                }
            };

            mDrawerToggle.setDrawerIndicatorEnabled(true);
            mDrawerLayout.setDrawerListener(mDrawerToggle);
        }
    }

    private void updateItems(){

        String sortOrder = QueryPreferences.getStoredOrder(getActivity());
        resetScrolling();

        Log.i(TAG, "Page counter reset");

        if (mMovieRecyclerView != null){
            mItems.clear();
            mMovieRecyclerView.getAdapter().notifyDataSetChanged();
        }

        new FetchItemTask(sortOrder, 1).execute();

    }

    private void resetScrolling() {
        // Reset the scrolling params
        lastFetchedPage = 1;
        previousTotal = 0;
        loading = true;
        firstVisibleItem = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
    }

    private boolean isNetworkAvailableAndConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(CONNECTIVITY_SERVICE);
        boolean isNetworkAvailable = cm.getActiveNetworkInfo() != null;
        boolean isNetworkConnected = isNetworkAvailable &&
                cm.getActiveNetworkInfo().isConnected();
        return isNetworkConnected;
    }

    // Attach loader to our favorite database query
    // run when loader is initialized
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri;
//        String sortOrder;

        // Favorites
        uri = MoviesContract.PopularMovies.buildFavoriteMoviesUri();

        CursorLoader loader = new CursorLoader(
                getActivity(),
                uri,
                MOVIE_COLUMNS,
                null,
                null,
                MoviesContract.PopularMovies.SORT_ORDER
        );

        return loader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data.moveToFirst()) {
            Vector<ContentValues> cVVector = new Vector<ContentValues>(data.getCount());

            do {
                ContentValues cv = new ContentValues();
                DatabaseUtils.cursorRowToContentValues(data, cv);
                cVVector.add(cv);
            } while(data.moveToNext());

            // TODO: update adapter and view
            mMovieAdapter.swapCursor(data);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }


    /*
    Class to create a background thread for the network connection, networking is not allowed on the main thread (UI thread).
     */
    public class FetchItemTask extends AsyncTask<Integer, Void, List<GalleryItem>> {

        private String mSortOrder;
        private int mPage;

        public FetchItemTask(String sortOrder, int page){
            mSortOrder = sortOrder;
            mPage = page;
        }

        @Override
        protected List<GalleryItem> doInBackground(Integer... params) {

            if (mSortOrder.equals("popular")) {
                return new MovieDBFetcher().fetchPopularMovies(mPage);
            }
            if (mSortOrder.equals("top")){
                return new MovieDBFetcher().fetchTopMovies(mPage);
            }
            else {
                return new MovieDBFetcher().fetchPopularMovies(mPage);
            }

        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {

            if(lastFetchedPage > 1){
                mItems.addAll(galleryItems);
                mMovieRecyclerView.getAdapter().notifyDataSetChanged();
            }
            else{
                mItems = galleryItems;
                setupAdapter();
            }

            lastFetchedPage++;
        }
    }


//    //TODO: extract this into a new class
//
//    private Cursor mAdapterCursor;
//    private int mPosition = -1;
//
//    /**
//     * ViewHolder class
//     */
//    private class MovieHolder extends RecyclerView.ViewHolder{
//
//
//        private ImageView mItemImageView;
//        private TextView mItemTitle;
//        private GalleryItem mGalleryItem;
//
//        public MovieHolder(View itemView) {
//            super(itemView);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    int adapterPosition = getAdapterPosition();
//                    mAdapterCursor.moveToPosition(adapterPosition);
//
//                    int movie_id_column = mAdapterCursor.getColumnIndex(MoviesContract.PopularMovies.MOVIE_ID);
//                    if(movie_id_column == -1) {
//                        Log.i(TAG, "Movie not in database"); //TODO: check this
//                    }
//
//                    int movie_id = mAdapterCursor.getInt(movie_id_column);
//
//                    Log.i(TAG, "Item clicked");
//                    Intent intent = new MovieDetailActivity().newIntent(getActivity(), mGalleryItem);
//                    startActivity(intent);
//                }
//            });
//
//            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_movie_item_thumbnail);
////                mItemTitle = (TextView) itemView.findViewById(R.id.fragment_movie_item_title);
//
//        }
//
//        public void bindGalleryItem(GalleryItem galleryItem){
//            mGalleryItem = galleryItem;
//        }
//
//    }
//
//    /**
//     * Adapter class
//     */
//    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder>{
//
//        private List<GalleryItem> mGalleryItems;
//
//        // Endless scrolling
//        private int lastBoundPosition;
//
//        public MovieAdapter(List<GalleryItem> galleryItems) {
//            mGalleryItems = galleryItems;
//        }
//
//        @Override
//        public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            LayoutInflater inflater = LayoutInflater.from(getActivity());
//            View view = inflater.inflate(R.layout.gallery_item, parent, false);
//
//            return new MovieHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(MovieHolder holder, int position) {
//            GalleryItem galleryItem = mGalleryItems.get(position);
//
//            mAdapterCursor.moveToPosition(position);
//            MovieCursorWrapper movieCursorWrapper = new MovieCursorWrapper(mAdapterCursor);
////            movieCursorWrapper.getFavoriteMovie();
//
//            //TODO: Some images like the one from Interstellar are there but do not load into the imageview
//            Picasso.with(getActivity())
//                    .load(galleryItem.getFullPosterPathw342())
////                    .placeholder(R.drawable.bill_up_close)
//                    .fit()
//                    .into(holder.mItemImageView);
//
////            Log.d(TAG, "onBindViewHolder: " + galleryItem.getFullPosterPathw185());
//
////            holder.mItemTitle.setText(galleryItem.getTitle()); // Set title
//            holder.bindGalleryItem(galleryItem);
//
//            lastBoundPosition = position;
////            Log.i(TAG,"Last bound position is " + Integer.toString(lastBoundPosition));
//
//        }
//
//        @Override
//        public int getItemCount() {
//            return mGalleryItems.size();
//        }
//
//        public int getLastBoundPosition() {
//            return lastBoundPosition;
//        }
//
//    }
//
//    public void swapCursor(Cursor newCursor){
//        mAdapterCursor = newCursor;
//        notifyDataSetChanged();
//    }
//
//    public void closeCursor() {
//        if (mAdapterCursor != null) {
//            mAdapterCursor.close();
//        }
//    }
//
//        public Cursor getCursor(){
//        return mAdapterCursor;
//    }



}

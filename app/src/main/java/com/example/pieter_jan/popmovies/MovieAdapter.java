package com.example.pieter_jan.popmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pieter_jan.popmovies.database.MovieCursorWrapper;
import com.example.pieter_jan.popmovies.database.MoviesContract;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by pieter-jan on 1/29/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {

    private static final String TAG = "MovieAdapter";
    private List<GalleryItem> mGalleryItems;

    private Context mContext;
//    final private View mEmptyView;

    // Endless scrolling
    private int lastBoundPosition;

    //
    private Cursor mAdapterCursor;
    private int mPosition = -1;


    public class MovieHolder extends RecyclerView.ViewHolder {
        private ImageView mItemImageView;
        private TextView mItemTitle;
        private GalleryItem mGalleryItem;

        public MovieHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int adapterPosition = getAdapterPosition();
                    mAdapterCursor.moveToPosition(adapterPosition);

                    int movie_id_column = mAdapterCursor.getColumnIndex(MoviesContract.PopularMovies.MOVIE_ID);
                    if (movie_id_column == -1) {
                        Log.i(TAG, "Movie not in database"); //TODO: check this
                    }

                    int movie_id = mAdapterCursor.getInt(movie_id_column);

                    Log.i(TAG, "Item clicked");
                    Intent intent = new MovieDetailActivity().newIntent(mContext, mGalleryItem);
                    mContext.startActivity(intent);
                }
            });

            mItemImageView = (ImageView) itemView.findViewById(R.id.fragment_movie_item_thumbnail);
//                mItemTitle = (TextView) itemView.findViewById(R.id.fragment_movie_item_title);

        }

        public void bindGalleryItem(GalleryItem galleryItem) {
            mGalleryItem = galleryItem;
        }

    }

    // Adapter continued

    public MovieAdapter(Context context) {
        mContext = context;
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.gallery_item, parent, false);

        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        GalleryItem galleryItem = mGalleryItems.get(position);

        mAdapterCursor.moveToPosition(position);
        MovieCursorWrapper movieCursorWrapper = new MovieCursorWrapper(mAdapterCursor);
//            movieCursorWrapper.getFavoriteMovie();

        //TODO: Some images like the one from Interstellar are there but do not load into the imageview
        Picasso.with(mContext)
                .load(galleryItem.getFullPosterPathw342())
//                    .placeholder(R.drawable.bill_up_close)
                .fit()
                .into(holder.mItemImageView);

//            Log.d(TAG, "onBindViewHolder: " + galleryItem.getFullPosterPathw185());

//            holder.mItemTitle.setText(galleryItem.getTitle()); // Set title
        holder.bindGalleryItem(galleryItem);

        lastBoundPosition = position;
//            Log.i(TAG,"Last bound position is " + Integer.toString(lastBoundPosition));

    }

    @Override
    public int getItemCount() {
        return mGalleryItems.size();
    }

    public int getLastBoundPosition() {
        return lastBoundPosition;
    }

}


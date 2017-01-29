package com.example.pieter_jan.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pieter-jan on 1/28/2017.
 */

public class MovieReview implements Parcelable {


    @SerializedName("id")
    private String id;

    @SerializedName("author")
    private String author;

    @SerializedName("content")
    private String content;

    @SerializedName("url")
    private String url;

    public MovieReview(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    //    -------------------------------------------------

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public MovieReview(Parcel parcel) {
        id = parcel.readString();
        author = parcel.readString();
        content = parcel.readString();
        url = parcel.readString();
    }

    public static final Parcelable.Creator<MovieReview> CREATOR = new Parcelable.Creator<MovieReview>() {

        @Override
        public MovieReview createFromParcel(Parcel source) {
            return new MovieReview(source);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };


    public static final class Response {

        @SerializedName("id")
        public long id;

        @SerializedName("page")
        public int page;

        @SerializedName("results")
        public List<MovieReview> mMovieReviews;

        @SerializedName("total_pages")
        public int totalPages;

        @SerializedName("total_results")
        public int totalResults;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public List<MovieReview> getMovieReviews() {
            return mMovieReviews;
        }

        public void setMovieReviews(List<MovieReview> movieReviews) {
            mMovieReviews = movieReviews;
        }

        public int getTotalPages() {
            return totalPages;
        }

        public void setTotalPages(int totalPages) {
            this.totalPages = totalPages;
        }

        public int getTotalResults() {
            return totalResults;
        }

        public void setTotalResults(int totalResults) {
            this.totalResults = totalResults;
        }
    }

}

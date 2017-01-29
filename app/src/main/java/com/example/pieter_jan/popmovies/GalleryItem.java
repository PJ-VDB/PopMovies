package com.example.pieter_jan.popmovies;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pieter-jan on 1/16/2017.
 */

public class GalleryItem implements Parcelable {

    @SerializedName("poster_path")
    private String posterPath;
    @SerializedName("adult")
    private boolean adult;
    @SerializedName("overview")
    private String overview;
    @SerializedName("release_date")
    private String releaseDate;
    @SerializedName("genre_ids")
    private List<Integer> genreIds = new ArrayList<Integer>();
    @SerializedName("id")
    private Integer id;
    @SerializedName("original_title")
    private String originalTitle;
    @SerializedName("original_language")
    private String originalLanguage;
    @SerializedName("title")
    private String title;
    @SerializedName("backdrop_path")
    private String backdropPath;
    @SerializedName("popularity")
    private Double popularity;
    @SerializedName("vote_count")
    private Integer voteCount;
    @SerializedName("video")
    private Boolean video;
    @SerializedName("vote_average")
    private Double voteAverage;

    private String posterPathw185;
    private String posterPathw342;

    public GalleryItem(String posterPath, boolean adult, String overview, String releaseDate, List<Integer> genreIds, Integer id, String originalTitle, String originalLanguage, String title, String backdropPath, Double popularity, Integer voteCount, Boolean video, Double voteAverage) {
        this.posterPath = posterPath;
        this.adult = adult;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.genreIds = genreIds;
        this.id = id;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.title = title;
        this.backdropPath = backdropPath;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.video = video;
        this.voteAverage = voteAverage;
        this.posterPathw185 = getFullPosterPathw185();
        this.posterPathw342 = getFullPosterPathw342();
    }

//    public GalleryItem(String posterPath, String overview, String releaseDate, int movieId, String title, String backdropPath, double popularity,
//                       int voteCount, double voteAverage){
//
//        this.posterPath = posterPath;
//        this.overview = overview;
//        this.releaseDate = releaseDate;
//        this.id = movieId;
//        this.title = title;
//        this.backdropPath = backdropPath;
//        this.popularity = popularity;
//        this.voteCount = voteCount;
//        this.voteAverage = voteAverage;
//
//    }

    public GalleryItem(){

    }


    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public List<Integer> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Integer> genreIds) {
        this.genreIds = genreIds;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }


    /*
    Build the complete path where the poster of the movie can be found
    The available sizes are: "w92", "w154", "w185", "w342", "w500", "w780", or "original". For most phones we recommend using “w185”.
     */
    public String getFullPosterPath(String size){

        String BASE_URL =  "http://image.tmdb.org/t/p/";
        Uri.Builder url = Uri.parse(BASE_URL)
                .buildUpon();
        url.appendPath(size)
                .appendPath(getPosterPath().substring(1));

        return url.toString();
    }

    public String getFullPosterPathw185(){
        return getFullPosterPath("w185");
    }

    public String getFullPosterPathw342(){
        return getFullPosterPath("w342");
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    /*
    Write the object values to a parcel for storage
     */
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // write all properties to the parcel

        dest.writeString(posterPath);
        dest.writeValue(adult);
        dest.writeString(overview);
        dest.writeString(releaseDate);
//        dest.writeList(genreIds);
        dest.writeInt(id);
        dest.writeString(originalTitle);
        dest.writeString(originalLanguage);
        dest.writeString(title);
        dest.writeString(backdropPath);
        dest.writeDouble(popularity);
        dest.writeInt(voteCount);
        dest.writeValue(video);
        dest.writeDouble(voteAverage);
        dest.writeString(posterPathw185);
        dest.writeString(posterPathw342);


    }

    /*
    Constructor used for parcel
     */
    public GalleryItem(Parcel parcel){
        // Read and set saved values from parcel

//        List<Integer> genres = null;

        posterPath = parcel.readString();
        adult = (Boolean) parcel.readValue(null);
        overview = parcel.readString();
        releaseDate = parcel.readString();
//        genreIds = parcel.readList(genres, List.class.getClassLoader());
        id = parcel.readInt();
        originalTitle = parcel.readString();
        originalLanguage = parcel.readString();
        title = parcel.readString();
        backdropPath = parcel.readString();
        popularity = parcel.readDouble();
        voteCount = parcel.readInt();
        video = (Boolean) parcel.readValue(null);
        voteAverage = parcel.readDouble();
        posterPathw185 = parcel.readString();
        posterPathw342 = parcel.readString();


    }

    /*
    Creator - used when un-parceling the parcel (creates the object)
     */
    public static final Parcelable.Creator<GalleryItem> CREATOR = new Parcelable.Creator<GalleryItem>(){

        @Override
        public GalleryItem createFromParcel(Parcel source) {
            return new GalleryItem(source);
        }

        @Override
        public GalleryItem[] newArray(int size) {
            return new GalleryItem[0];
        }
    };


}

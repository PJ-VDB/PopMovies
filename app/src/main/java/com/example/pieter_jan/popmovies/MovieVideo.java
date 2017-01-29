package com.example.pieter_jan.popmovies;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pieter-jan on 1/28/2017.
 */

public class MovieVideo implements Parcelable {


    @SerializedName("id")
    private String id;
    @SerializedName("iso_639_1")
    private String iso6;
    @SerializedName("iso_3166_1")
    private String iso3;
    @SerializedName("key")
    private String key;
    @SerializedName("name")
    private String name;
    @SerializedName("site")
    private String site;
    @SerializedName("size")
    private int size;
    @SerializedName("type")
    private String type;

    public MovieVideo(String id, String iso6, String iso3, String key, String name, String site, int size, String type) {
        this.id = id;
        this.iso6 = iso6;
        this.iso3 = iso3;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIso6() {
        return iso6;
    }

    public void setIso6(String iso6) {
        this.iso6 = iso6;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Video{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    //    -------------------------------------------------

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.id);
        dest.writeString(this.iso3);
        dest.writeString(this.iso6);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.type);
    }

    public MovieVideo(Parcel parcel){
        this.id = parcel.readString();
        this.iso3 = parcel.readString();
        this.iso6 = parcel.readString();
        this.key = parcel.readString();
        this.name = parcel.readString();
        this.site = parcel.readString();
        this.size = parcel.readInt();
        this.type = parcel.readString();
    }

    public static final Parcelable.Creator<MovieVideo> CREATOR = new Parcelable.Creator<MovieVideo>(){

        @Override
        public MovieVideo createFromParcel(Parcel source) {
            return new MovieVideo(source);
        }

        @Override
        public MovieVideo[] newArray(int size) {
            return new MovieVideo[size];
        }
    };

    public static final class Response {

        @SerializedName("id")
        public long id;

        @SerializedName("results")
        public List<MovieVideo> mMovieVideos;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public List<MovieVideo> getMovieVideos() {
            return mMovieVideos;
        }

        public void setMovieVideos(List<MovieVideo> movieVideos) {
            mMovieVideos = movieVideos;
        }
    }
}

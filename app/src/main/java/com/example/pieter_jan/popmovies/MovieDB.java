package com.example.pieter_jan.popmovies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by pieter-jan on 1/17/2017.
 */

public class MovieDB {

        @SerializedName("page")
        private int page;
        @SerializedName("results")
        private List<GalleryItem> results;
        @SerializedName("total_results")
        private int totalResults;
        @SerializedName("total_pages")
        private int totalPages;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<GalleryItem> getResults() {
        return results;
    }

    public void setResults(List<GalleryItem> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }
}

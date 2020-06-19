package com.pavan.moviebuff.Utils.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class MoviesResponse {
    @SerializedName("page")
    private int page;

    @SerializedName("results")
    private ArrayList<Movie> movieList;

    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public ArrayList<Movie> getMovieList() {
        return movieList;
    }

    public int getTotalPages() {
        return totalPages;
    }

    @SerializedName("total_pages")
    private int totalPages;
}

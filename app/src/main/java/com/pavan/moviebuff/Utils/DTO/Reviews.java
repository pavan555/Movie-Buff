package com.pavan.moviebuff.Utils.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Reviews {


    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;


    @SerializedName("results")
    private ArrayList<Review> reviewArrayList;

    public class Review {
        @SerializedName("author")
        private String author;

        @SerializedName("content")
        private String review;

        public String getAuthor() {
            return "Written by <font color='green' size='+2' > <u>" + author + "</u></font>";
        }

        public String getReview() {
            return review;
        }

        public String getReviewUrl() {
            return reviewUrl;
        }

        @SerializedName("url")
        private String reviewUrl;

    }

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public ArrayList<Review> getReviewArrayList() {
        return reviewArrayList;
    }
}

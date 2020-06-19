package com.pavan.moviebuff.Utils.DTO;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Result {

    @SerializedName("results")
    private ArrayList<video> videoArrayList;

    public ArrayList<video> getVideoResult() {
        return videoArrayList;
    }

    public class video {
        public String getUrl() {
            //only two video sites are available youtube and vimeo
            if (site.toLowerCase().equals("youtube"))
                return "https://www.youtube.com/watch?v=" + url;
            else
                return "https://vimeo.com/" + url;
        }

        public String getKey() {
            return url;
        }

        @SerializedName("key")
        private String url;

        @SerializedName("name")
        private String name;

        @SerializedName("site")
        private String site;


    }
}

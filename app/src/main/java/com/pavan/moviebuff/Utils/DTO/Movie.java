package com.pavan.moviebuff.Utils.DTO;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

@Entity(tableName = "movies")
public class Movie implements Serializable {
    @PrimaryKey
    @NonNull
    @SerializedName("id")
    private Integer id;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String poster;

    @ColumnInfo(name = "title")
    @SerializedName("title")
    private String title;

    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    private String backdrop;

    @Ignore
    @SerializedName("genres")
    private ArrayList<Genre> genreList;

    @ColumnInfo(name = "release_date")
    @SerializedName("release_date")
    private String releaseDate;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String overview;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    private Double voteAvg;


    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    private Double popularity;

    public Movie(@NonNull Integer id, String poster, String title, String backdrop, String releaseDate, String overview, Double voteAvg, Double popularity) {
        this.id = id;
        this.poster = "https://image.tmdb.org/t/p/w500" + poster;
        this.title = title;
        this.backdrop = "https://image.tmdb.org/t/p/w1280" + backdrop;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.voteAvg = voteAvg;
        this.popularity = popularity;
    }

    public Integer getId() {
        return id;
    }

    public Double getPopularity() {
        return popularity;
    }

    public String getPoster() {
        return "https://image.tmdb.org/t/p/w500" + poster;
    }


    public String getTitle() {
        return title;
    }


    public String getOverview() {
        return overview;
    }


    public String getBackdrop() {
        if (backdrop != null)
            return "https://image.tmdb.org/t/p/w1280" + backdrop;
        else
            return getPoster();
    }

    public ArrayList<Genre> getGenreList() {
        return genreList;
    }

    public Double getVoteAvg() {
        return voteAvg;
    }

    public String getReleaseDate() {
        return releaseDate;
    }


    public class Genre {

        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

    }


}

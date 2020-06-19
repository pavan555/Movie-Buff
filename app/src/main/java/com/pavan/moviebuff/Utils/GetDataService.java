package com.pavan.moviebuff.Utils;


import com.pavan.moviebuff.Utils.DTO.MoviesResponse;
import com.pavan.moviebuff.Utils.DTO.Result;
import com.pavan.moviebuff.Utils.DTO.Reviews;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetDataService {
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(@Query("api_key") String apiKey, @Query("page") int page);

    @GET("movie/{movie_id}/videos")
    Call<Result> getVideos(@Path("movie_id") int movieId, @Query("api_key") String apiKey);


    @GET("movie/{movie_id}/reviews")
    Call<Reviews> getReviews(@Path("movie_id") int movieId, @Query("api_key") String apiKey, @Query("page") int page);

}

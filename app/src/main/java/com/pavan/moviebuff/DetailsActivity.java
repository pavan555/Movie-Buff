package com.pavan.moviebuff;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.transition.platform.MaterialContainerTransform;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.pavan.moviebuff.Utils.Adapters.ReviewAdapter;
import com.pavan.moviebuff.Utils.Adapters.VideosAdapter;
import com.pavan.moviebuff.Utils.DTO.Movie;
import com.pavan.moviebuff.Utils.DTO.Result;
import com.pavan.moviebuff.Utils.DTO.Reviews;
import com.pavan.moviebuff.Utils.GetDataService;
import com.pavan.moviebuff.Utils.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailsActivity extends AppCompatActivity {

    ToggleButton favButton;
    int movieId;
    TextView rating, releaseDate, title, overview, noReviews, noVideos;
    ImageView backDrop, poster;
    Movie movie;
    Result videosResult;
    Reviews reviewsResult;

    RecyclerView videos, reviews;
    GetDataService getDataService;
    LinearLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        //TODO USE Picasso or GLIDE instead of fresco to set transitions
        // Set the transition name, which matches Activity A’s start view transition name, on
        // the root view.
        findViewById(R.id.poster).setTransitionName("shared_element_container");

        // Attach a callback used to receive the shared elements from Activity A to be
        // used by the container transform transition.
        setEnterSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());

        // Set this Activity’s enter and return transition to a MaterialContainerTransform
        getWindow().setSharedElementEnterTransition(new MaterialContainerTransform().
                addTarget(R.id.poster).setDuration(300L));
        getWindow().setSharedElementExitTransition(new MaterialContainerTransform().
                addTarget(R.id.poster).setDuration(300L));
        getWindow().setSharedElementReturnTransition(new MaterialContainerTransform().
                addTarget(R.id.poster).setDuration(300L));


        if (getIntent().getExtras() != null) {
            movieId = getIntent().getExtras().getInt("movieId");
            movie = (Movie) getIntent().getSerializableExtra("movie");
        }


        favButton = findViewById(R.id.button_favorite);
        title = findViewById(R.id.title);
        overview = findViewById(R.id.movie_overview);
        releaseDate = findViewById(R.id.release_date);
        noReviews = findViewById(R.id.no_reviews);
        noVideos = findViewById(R.id.no_videos);
        rating = findViewById(R.id.rating);

        backDrop = findViewById(R.id.movie_backdrop);
        poster = findViewById(R.id.poster);
        videos = findViewById(R.id.videos);
        reviews = findViewById(R.id.reviews);
        getDataService = RetrofitInstance.getRetrofitInstance().create(GetDataService.class);
        layoutManager = new LinearLayoutManager(this);

        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.7f, 1.0f, 1.7f, 1.0f, Animation.RELATIVE_TO_SELF, 0.7f, Animation.RELATIVE_TO_SELF, 0.7f);
        scaleAnimation.setDuration(500);
        BounceInterpolator bounceInterpolator = new BounceInterpolator();
        scaleAnimation.setInterpolator(bounceInterpolator);

        favButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                buttonView.startAnimation(scaleAnimation);
            }
        });
        populateDetails();
        getVideos();
        getReviews();

    }

    private void getVideos() {

        Call<Result> movieArrayListCall = getDataService.getVideos(movieId, getString(R.string.api_key));

        movieArrayListCall.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                if (response.isSuccessful()) {
                    videosResult = response.body();
                    if (videosResult != null) {
                        Log.i("Videos", "onResponse: " + videosResult.getVideoResult().size());
                    }
                    populateVideoDetails();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(DetailsActivity.this, "Failed to retrive the data", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void populateVideoDetails() {
        if (videosResult != null) {
            if (videosResult.getVideoResult().size() != 0) {
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                videos.setLayoutManager(linearLayoutManager);
                VideosAdapter videosAdapter = new VideosAdapter(videosResult.getVideoResult(), this.getLifecycle());
                videos.setAdapter(videosAdapter);
                noVideos.setVisibility(View.GONE);
                return;
            }
        }
        videos.setVisibility(View.GONE);
        noVideos.setVisibility(View.VISIBLE);
    }

    int reviewPageNumber = 1;
    int totalReviewPages;
    ReviewAdapter reviewAdapter;

    private void getReviews() {
        Call<Reviews> reviewsCall = getDataService.getReviews(movieId, getString(R.string.api_key), reviewPageNumber);
        reviewsCall.enqueue(new Callback<Reviews>() {
            @Override
            public void onResponse(Call<Reviews> call, Response<Reviews> response) {
                if (response.isSuccessful()) {
                    reviewsResult = response.body();
                    if (reviewsResult != null) {
                        reviewPageNumber = reviewsResult.getPage();
                        totalReviewPages = reviewsResult.getTotalPages();
                        populateReviewDetails();
                    }
                }
            }

            @Override
            public void onFailure(Call<Reviews> call, Throwable t) {

            }
        });
    }

    private void addScrollListener() {
        reviews.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int visibleItemCount = layoutManager.getChildCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    reviews.removeOnScrollListener(this);
                    if (reviewPageNumber < totalReviewPages) {
                        reviewPageNumber++;
                        getReviews();
                    }
                }
            }
        });
    }


    private void populateReviewDetails() {
        if (reviewsResult != null) {
            if (reviewsResult.getReviewArrayList().size() > 0) {

                if (reviewAdapter == null) {
                    reviewAdapter = new ReviewAdapter(reviewsResult.getReviewArrayList());
                    reviews.setAdapter(reviewAdapter);
                    reviews.setLayoutManager(layoutManager);
                } else
                    reviewAdapter.updateFiles(reviewsResult.getReviewArrayList());
                addScrollListener();
                noReviews.setVisibility(View.GONE);
            } else {
                reviews.setVisibility(View.GONE);
                noReviews.setVisibility(View.VISIBLE);
            }
        }
    }

    public void populateDetails() {
        if (movie != null) {
            title.setText(movie.getTitle());
            overview.setText(movie.getOverview());
            rating.setText(String.valueOf(movie.getVoteAvg()));
            releaseDate.setText(movie.getReleaseDate());
            Glide.with(this)
                    .load(movie.getBackdrop())
                    .placeholder(R.drawable.film_poster_placeholder)
                    .centerCrop()
                    .into(backDrop);
            Glide.with(this)
                    .load(movie.getPoster())
                    .centerCrop()
                    .into(poster);
        }
    }

}
package com.pavan.moviebuff;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback;
import com.pavan.moviebuff.Utils.Adapters.DataAdapter;
import com.pavan.moviebuff.Utils.DTO.MoviesResponse;
import com.pavan.moviebuff.Utils.GetDataService;
import com.pavan.moviebuff.Utils.NetworkChangeReceiver;
import com.pavan.moviebuff.Utils.RetrofitInstance;
import com.pavan.moviebuff.Utils.model.MovieViewModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements NetworkChangeReceiver.ConnectionChangeCallback {

    ShimmerFrameLayout shimmerFrameLayout;
    LinearLayout errorLayout;
    RecyclerView itemsView;
    MaterialButton retry;
    int pageNumber, totalPages;
    DataAdapter dataAdapter;
    GridLayoutManager layoutManager;
    Call<MoviesResponse> moviesResponseCall;
    GetDataService getDataService;
    NetworkChangeReceiver networkChangeReceiver;
    SharedPreferences sharedPreferences;

    MovieViewModel movieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setExitSharedElementCallback(new MaterialContainerTransformSharedElementCallback());
        getWindow().setSharedElementsUseOverlay(false);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        shimmerFrameLayout = findViewById(R.id.shimmer_frame_layout);
        errorLayout = findViewById(R.id.errorLayout);
        itemsView = findViewById(R.id.movieData);
        retry = findViewById(R.id.retry);
        sharedPreferences = getSharedPreferences("MoviePrefs", MODE_PRIVATE);

        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleErrorView(false);
            }
        });

        toggleShimmer(true);
        pageNumber = 1;

        layoutManager = new GridLayoutManager(MainActivity.this, 2, RecyclerView.VERTICAL, false);
        getDataService = RetrofitInstance.getRetrofitInstance().create(GetDataService.class);


        networkChangeReceiver = new NetworkChangeReceiver(this);
        IntentFilter intentFilter = new
                IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkChangeReceiver, intentFilter);
        networkChangeReceiver.setConnectionChangeCallback(this);
        prevNetworkState = networkChangeReceiver.isNetworkAvailable();
        moviesResponseCall = getDataService.getPopularMovies(getString(R.string.api_key), pageNumber);
        getMoviesData();


    }


    private void toggleShimmer(boolean show) {
        if (show) {
            shimmerFrameLayout.setVisibility(View.VISIBLE);
            shimmerFrameLayout.startShimmer();
            itemsView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.GONE);
        } else {
            shimmerFrameLayout.setVisibility(View.GONE);
            shimmerFrameLayout.stopShimmer();
            itemsView.setVisibility(View.VISIBLE);
            itemsView.setAdapter(dataAdapter);
            itemsView.setLayoutManager(layoutManager);

        }
    }

    private void toggleErrorView(boolean show) {
        if (show) {
            errorLayout.setVisibility(View.VISIBLE);
            itemsView.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);
        } else {
            toggleShimmer(true);
            getMoviesData();
        }
    }

    private void setScrollListener() {

        itemsView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int totalItemCount = layoutManager.getItemCount();
                int visibleItemCount = layoutManager.getChildCount();
                int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    itemsView.removeOnScrollListener(this);
                    if (pageNumber < totalPages) {
                        if (prevNetworkState) {
                            pageNumber++;
                            getMoviesData();
                        } else {
                            Toast.makeText(MainActivity.this, "you are not connected to internet", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
    }

    private void getMoviesData() {
        moviesResponseCall = getDataService.getPopularMovies(getString(R.string.api_key), pageNumber);
        moviesResponseCall.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse != null) {
                        if (dataAdapter != null) {
                            dataAdapter.updateFiles(moviesResponse.getMovieList());
                            //ensuring the page count
                            pageNumber = moviesResponse.getPage();
                        } else {
                            dataAdapter = new DataAdapter(moviesResponse.getMovieList());
                            dataAdapter.setClickListener(new DataAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Intent intent = new Intent(MainActivity.this, DetailsActivity.class);
                                    intent.putExtra("movieId", dataAdapter.getMovieId(position));
                                    intent.putExtra("movie", dataAdapter.getMovie(position));
                                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, view.findViewById(R.id.itemSquare), "shared_element_container");
                                    startActivity(intent, options.toBundle());
                                }

                                @Override
                                public boolean onItemLongClick(View view, int position) {
                                    return false;
                                }
                            });
                            toggleShimmer(false);
                            totalPages = moviesResponse.getTotalPages();
                        }
                        setScrollListener();
                    }
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                if (dataAdapter == null) toggleErrorView(true);
            }
        });
    }


    boolean prevNetworkState;

    @Override
    public void onConnectionChange(boolean isConnected) {
        if (isConnected != prevNetworkState && isConnected) {
            Toast.makeText(MainActivity.this, "you are connected to internet", Toast.LENGTH_SHORT).show();
            if (dataAdapter == null) toggleErrorView(false);
            else getMoviesData();
        }
        prevNetworkState = isConnected;
    }

    @Override
    protected void onDestroy() {
        try {
            unregisterReceiver(networkChangeReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Glide.get(this).clearMemory();
        super.onDestroy();
    }


}
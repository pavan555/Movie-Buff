package com.pavan.moviebuff.Utils.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pavan.moviebuff.Utils.DTO.Movie;
import com.pavan.moviebuff.Utils.MovieRepository;

import java.util.List;


/**
 * <b>
 * Keeping a reference can cause a memory leak,
 * e.g. the ViewModel has a reference to a destroyed Activity! All these objects can be destroyed by
 * the operative system and recreated when there's a configuration change, and this can happen many times
 * during the lifecycle of a ViewModel.
 * </b>
 * <br/>
 * If you need the application context (which has a lifecycle that lives as long as the application does), use AndroidViewModel
 */
public class MovieViewModel extends AndroidViewModel {
    private MovieRepository movieRepository;
    private LiveData<List<Movie>> moviesArrayList;

    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        moviesArrayList = movieRepository.getMoviesLiveList();
    }

    public LiveData<List<Movie>> getMoviesArrayList() {
        return moviesArrayList;
    }

    public void insert(Movie movie) {
        movieRepository.insert(movie);
    }

    public void insertAll(List<Movie> movies) {
        movieRepository.insertAll(movies);
    }
}

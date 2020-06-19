package com.pavan.moviebuff.Utils;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.pavan.moviebuff.Utils.DAO.MovieDAO;
import com.pavan.moviebuff.Utils.DTO.Movie;

import java.util.List;

/**
 * the Repository implements the logic for deciding
 * whether to fetch data from a network or use results cached in a local database.
 * https://codelabs.developers.google.com/codelabs/android-room-with-a-view/img/57f20bf7a898c03d.png
 */
public class MovieRepository {

    private MovieDAO movieDAO;
    private LiveData<List<Movie>> moviesLiveList;


    public MovieRepository(Application application) {
        MovieRoomDataBase movieRoomDataBase = MovieRoomDataBase.getMovieRoomDataBase(application);
        movieDAO = movieRoomDataBase.movieDAO();
        moviesLiveList = movieDAO.getMoviesList();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Movie>> getMoviesLiveList() {
        return moviesLiveList;
    }


    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(final Movie movie) {
        MovieRoomDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                movieDAO.insert(movie);
            }
        });
    }

    public void insertAll(final List<Movie> movies) {
        MovieRoomDataBase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                movieDAO.insertAll(movies);
            }
        });
    }
}

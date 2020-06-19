package com.pavan.moviebuff.Utils;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pavan.moviebuff.Utils.DAO.MovieDAO;
import com.pavan.moviebuff.Utils.DTO.Movie;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Database(entities = Movie.class, version = 1, exportSchema = false)
public abstract class MovieRoomDataBase extends RoomDatabase {

    public abstract MovieDAO movieDAO();

    //We've defined a singleton, MovieRoomDataBase, to prevent having multiple instances of the database opened at the same time.
    /*
    “volatile” tells the compiler that the value of a variable must never be cached as its value may change outside of the scope of the program itself.
     */
    private static volatile MovieRoomDataBase movieRoomDataBaseInstance;
    private static final int NUMBER_OF_INSTANCES = 1;

    //We've created an ExecutorService with a fixed thread pool that you will use to run database operations asynchronously on a background thread.
    static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_INSTANCES);

    /**
     * getMovieRoomDataBase returns the singleton.
     * It'll create the database the first time it's accessed, using Room's database builder
     * to create a RoomDatabase object in the application context from the MovieRoomDatabase class
     * and names it "movie_database".
     *
     * @param context context where it is instantiated,required to create the object
     * @return RoomDataBase Object
     */
    static MovieRoomDataBase getMovieRoomDataBase(Context context) {
        if (movieRoomDataBaseInstance == null) {
            synchronized (MovieRoomDataBase.class) {
                if (movieRoomDataBaseInstance == null) {
                    movieRoomDataBaseInstance = Room.databaseBuilder(context,
                            MovieRoomDataBase.class, "movie_database").build();
                }
            }

        }
        return movieRoomDataBaseInstance;

    }

//    private static RoomDatabase.Callback moviesRoomDatabaseCallback = new RoomDatabase.Callback() {
//        @Override
//        public void onOpen(@NonNull SupportSQLiteDatabase db) {
//            super.onOpen(db);
//
//            // If you want to keep data through app restarts,
//            // comment out the following block
//            databaseWriteExecutor.execute(() -> {
//                // Populate the database in the background.
//                // If you want to start with more words, just add them.
//                MovieDAO dao = movieRoomDataBaseInstance.movieDAO();
//                dao.getMoviesList();
//            });
//        }
//    };
}

package com.example.a16022916.movieapppart2.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

@Database(entities = {FavMovies.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {

    private static final String LOG_TAG = AppDatabase.class.getSimpleName();

    private static final String DATABASE_NAME = "fav_movie_table";
    private static AppDatabase sInstance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (sInstance == null) {
                Log.d(LOG_TAG, "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();

        }
        Log.d(LOG_TAG, "Getting the database instance");
        return sInstance;
    }

    public abstract FavMovieDao favMovDao();

    // Populates the database on create
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Log.d("DATA TEST", "FavMovieRepository: ");
            new PopulateDbAsyncTask(sInstance).execute();


        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private FavMovieDao favMovieDao;

        private PopulateDbAsyncTask(AppDatabase mdb) {
            favMovieDao = mdb.favMovDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            Log.d("DATA TEST", "FavMovieRepository: ");

            int id, movieId;
            String title, releaseDate, moviePoster, avgVote, plot;
            title = "Justin's Movie";
            releaseDate = "26.19.1999";
            moviePoster = "/2uNW4WbgBXL25BAbXGLnLqX71Sw.jpg";
            avgVote = String.valueOf(10.0);
            plot = "I AM A DESC";
            movieId = 335983;

            FavMovies favMoviesInner = new FavMovies(title, releaseDate, moviePoster,
                    avgVote, plot, movieId);
            favMovieDao.insertFavMovie(favMoviesInner);
            return null;
        }
    }
}

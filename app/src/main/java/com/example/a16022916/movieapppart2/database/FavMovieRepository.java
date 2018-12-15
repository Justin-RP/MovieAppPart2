package com.example.a16022916.movieapppart2.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavMovieRepository {
    private FavMovieDao favMovieDao;
    private LiveData<List<FavMovies>> allFavMovies;

    public FavMovieRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        favMovieDao = database.favMovDao();
        allFavMovies = favMovieDao.loadAllMovies();
    }

    public void insert(FavMovies favMovies) {
        new InsertFavMovieAsyncTask(favMovieDao).execute(favMovies);
    }

    public void update(FavMovies favMovies) {
        new UpdateFavMovieAsyncTask(favMovieDao).execute(favMovies);
    }

    public void delete(FavMovies favMovies) {
        new DeleteFavMovieAsyncTask(favMovieDao).execute(favMovies);
    }



    public LiveData<List<FavMovies>> getAllFavMovies() {
        return allFavMovies;
    }

    private static class InsertFavMovieAsyncTask extends AsyncTask<FavMovies, Void, Void> {
        private FavMovieDao innerFavMoviesDao;

        private InsertFavMovieAsyncTask(FavMovieDao favMovieDao) {
            this.innerFavMoviesDao = favMovieDao;
        }
        @Override
        protected Void doInBackground(FavMovies... favMovies) {
            innerFavMoviesDao.insertFavMovie(favMovies[0]);
            return null;
        }
    }

    private static class UpdateFavMovieAsyncTask extends AsyncTask<FavMovies, Void, Void> {
        private FavMovieDao innerFavMoviesDao;

        private UpdateFavMovieAsyncTask(FavMovieDao favMovieDao) {
            this.innerFavMoviesDao = favMovieDao;
        }
        @Override
        protected Void doInBackground(FavMovies... favMovies) {
            innerFavMoviesDao.updateFavMovie(favMovies[0]);
            return null;
        }
    }

    private static class DeleteFavMovieAsyncTask extends AsyncTask<FavMovies, Void, Void> {
        private FavMovieDao innerFavMoviesDao;

        private DeleteFavMovieAsyncTask(FavMovieDao favMovieDao) {
            this.innerFavMoviesDao = favMovieDao;
        }
        @Override
        protected Void doInBackground(FavMovies... favMovies) {
            innerFavMoviesDao.deleteFavMovie(favMovies[0]);
            return null;
        }
    }

}

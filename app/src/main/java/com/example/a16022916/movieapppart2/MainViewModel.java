package com.example.a16022916.movieapppart2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.a16022916.movieapppart2.database.AppDatabase;
import com.example.a16022916.movieapppart2.database.FavMovies;

import java.util.List;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private LiveData<FavMovies> favMovies;
    private List<Movie> movieList;

    public MainViewModel(AppDatabase database, int favMovieId) {
        favMovies = database.favMovDao().loadMoviesById(favMovieId);
    }

    public LiveData<FavMovies> getMovies() {
        return favMovies;
    }
}

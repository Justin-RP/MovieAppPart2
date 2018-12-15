package com.example.a16022916.movieapppart2;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.a16022916.movieapppart2.database.FavMovieRepository;
import com.example.a16022916.movieapppart2.database.FavMovies;

import java.util.List;

public class FavMovieViewModel extends AndroidViewModel {

    private FavMovieRepository repository;
    private LiveData<List<FavMovies>> allFavMovie;

    public FavMovieViewModel(@NonNull Application application) {
        super(application);
        repository = new FavMovieRepository(application);
        allFavMovie = repository.getAllFavMovies();
    }

    public void insert(FavMovies favMovies) {
        repository.insert(favMovies);
    }

    public void update(FavMovies favMovies) {
        repository.update(favMovies);
    }

    public void delete(FavMovies favMovies) {
        repository.delete(favMovies);
    }

    public LiveData<List<FavMovies>> getAllFavMovie() {
        return allFavMovie;
    }


}

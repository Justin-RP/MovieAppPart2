package com.example.a16022916.movieapppart2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface FavMovieDao {

    @Query("SELECT * FROM fav_movie_table")
    LiveData<List<FavMovies>> loadAllMovies();

    @Insert
    void insertFavMovie(FavMovies favMovies);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFavMovie(FavMovies favMovies);

    @Delete
    void deleteFavMovie(FavMovies favMovies);

    @Query("DELETE FROM fav_movie_table WHERE movieId = :movieId")
    void deleteMovieById(int movieId);

    @Query("SELECT id FROM fav_movie_table WHERE movieId = :movieId")
    boolean isMovieFav(int movieId);

    @Query("SELECT * FROM fav_movie_table WHERE movieId = :movieId")
    LiveData<FavMovies> loadMoviesById(int movieId);

    // Will this work with the same Query?
    @Query("SELECT * FROM fav_movie_table WHERE movieId = :movieId")
    boolean getMovieById(int movieId);
}

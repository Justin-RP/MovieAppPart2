package com.example.a16022916.movieapppart2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "fav_movie_table")
public class FavMovies {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private int movieId;
    private String title, releaseDate, moviePoster, avgVote, plot;

    @Ignore
    public FavMovies(String title, String releaseDate, String moviePoster, String avgVote, String plot, int movieId) {
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.avgVote = avgVote;
        this.plot = plot;
        this.movieId = movieId;
    }

    public FavMovies(int id, String title, String releaseDate, String moviePoster, String avgVote, String plot, int movieId) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.moviePoster = moviePoster;
        this.avgVote = avgVote;
        this.plot = plot;
        this.movieId = movieId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public String getAvgVote() {
        return avgVote;
    }

    public void setAvgVote(String avgVote) {
        this.avgVote = avgVote;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
        this.movieId = movieId;
    }

}

package com.example.a16022916.movieapppart2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a16022916.movieapppart2.Review.ReviewFragment;
import com.example.a16022916.movieapppart2.Trailers.TrailerFragment;
import com.example.a16022916.movieapppart2.database.FavMovies;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MovieDetails extends AppCompatActivity {

    private static final String DEBUG_TAG = "MovieDetails";
    private TextView tvTitle, tvReleaseDate, tvRatings, tvSynopsis, tvReviews;
    private ImageView imageViewBackground, imageViewPoster, imageViewFavourite;
    private Movie movie;
    private FavMovieViewModel favMovieViewModel;
    private List<FavMovies> favMoviesList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitle = findViewById(R.id.mdTvTitle);
        tvReleaseDate = findViewById(R.id.mdTvReleaseDate);
        tvRatings = findViewById(R.id.mdTvUserRatings);
        tvSynopsis = findViewById(R.id.mdTvSynopsis);
        imageViewBackground = findViewById(R.id.mdImageBackground);
        imageViewPoster = findViewById(R.id.mdMovieImage);
        imageViewFavourite = findViewById(R.id.mdIvStar);

        movie = (Movie) getIntent().getSerializableExtra("movieData");
        tvTitle.setText(movie.getTitle());
        tvReleaseDate.setText(movie.getRelease_date());
        tvRatings.setText(String.valueOf(movie.getVote_average()));
        tvSynopsis.setText(movie.getOverview());

        favMoviesList = new ArrayList<>();
        favMovieViewModel = ViewModelProviders.of(this).get(FavMovieViewModel.class);
        favMovieViewModel.getAllFavMovie().observe(this, new Observer<List<FavMovies>>() {
            @Override
            public void onChanged(@Nullable List<FavMovies> favMovies) {
                initFavMovie(movie);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        final String strId = String.valueOf(movie.getId());
        imageViewFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFavMovie(movie);
            }
        });

        if(movie.getPoster_path().equalsIgnoreCase("N/A")){
            imageViewPoster.setImageResource(R.drawable.noimage);
        }else{
            String baseUrl = "http://image.tmdb.org/t/p/";
            String size = "w185/";
            Picasso.with(getBaseContext()).load(baseUrl + size + movie.getPoster_path()).into(imageViewPoster);
        }
        if(movie.getBackdrop_path().equalsIgnoreCase("N/A")) {
            imageViewBackground.setImageResource(R.drawable.noimage);
        } else {
            String baseUrl = "http://image.tmdb.org/t/p/";
            String size = "w185/";
            Picasso.with(getBaseContext()).load(baseUrl + size + movie.getBackdrop_path()).into(imageViewBackground);
        }



        Bundle args = new Bundle();
        args.putString("movieId", String.valueOf(movie.getId()));

        ReviewFragment reviewFragment= new ReviewFragment();
        reviewFragment.setArguments(args);
        TrailerFragment trailerFragment = new TrailerFragment();
        trailerFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mFragment_review, reviewFragment);
        fragmentTransaction.replace(R.id.mFragment_video, trailerFragment);
        fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        fragmentTransaction.commit();




    }

    public void initFavMovie(Movie movie) {
        if (getFavMovie(movie) != null) {
            setImageStar(true);
        } else {
            setImageStar(false);
        }
    }

    public void switchFavMovie(Movie movie) {
        if (getFavMovie(movie) != null) {
            setImageStar(false);
            favMovieViewModel.delete(getFavMovie(movie));
        } else {
            setImageStar(true);
            addFavMovie(movie);
        }
    }

    private void setImageStar(boolean isFav) {
        if (isFav) {
            imageViewFavourite.setImageResource(R.drawable.star);
        } else {
            imageViewFavourite.setImageResource(R.drawable.nostar);
        }
    }

    private FavMovies getFavMovie(Movie movie){
        FavMovies favMovie = null;
        for (int i = 0; i < favMovieViewModel.getAllFavMovie().getValue().size(); i ++ ) {
            if ((favMovieViewModel.getAllFavMovie().getValue().get(i).getMovieId()) == movie.getId()) {
                favMovie = favMovieViewModel.getAllFavMovie().getValue().get(i);
                break;
            } else {
                favMovie = null;
            }
        }
        return favMovie;
    }

    public void addFavMovie(Movie movie) {
        int movieId = movie.getId();
        String title = movie.getTitle();
        String releaseDate = movie.getRelease_date();
        String moviePoster = movie.getPoster_path();
        String avgVote = String.valueOf(movie.getVote_average());
        String plot = movie.getOverview();
        FavMovies insertFavMovie = new FavMovies(title, releaseDate, moviePoster, avgVote, plot, movieId);
        favMovieViewModel.insert(insertFavMovie);
    }



}



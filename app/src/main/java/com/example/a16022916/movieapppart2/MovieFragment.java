package com.example.a16022916.movieapppart2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a16022916.movieapppart2.database.AppDatabase;
import com.example.a16022916.movieapppart2.database.FavMovies;
import com.example.a16022916.movieapppart2.utilities.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MovieFragment extends Fragment {

    private static final String DEBUG_TAG = "MovieFragment";
    private List<Movie> movieList;
    private RecyclerViewAdapter myAdapter;
    private View view;
    private boolean sortByFav;
    private static final String BUNDLE_RECYCLER_LAYOUT = "classname.recycler.layout";
    private RecyclerView recyclerView;

    private AppDatabase mDb;

    public MovieFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        boolean test = getArguments().getBoolean("isSortedByTopRated");
        String sortBy = "top_rated";
        sortByFav = false;
        if (getArguments().getString("fragType", "").equals("itemTopRated")) {
            sortBy = "top_rated";
        } else if (getArguments().getString("fragType", "").equals("itemPopular")) {
            sortBy = "popular";
        }  else if (getArguments().getString("fragType", "").equals("itemFavourite")) {
            sortByFav = true;
        }


        Log.d(DEBUG_TAG, "onCreateView: " + sortByFav);
        view = inflater.inflate(R.layout.fragment_movie, container, false);

        movieList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.mRecycler_view);
        myAdapter = new RecyclerViewAdapter(getContext(), movieList);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.setAdapter(myAdapter);

        if (sortByFav) {
//            HttpRequest request = new HttpRequest
//                    ("http://api.themoviedb.org/3/movie/popular?api_key=" + BuildConfig.ApiKey);
            HttpRequest request = new HttpRequest
                    ("http://api.themoviedb.org/3/movie/popular?api_key=84c758d1cd57e7b7a7947e8880c4648f");
            request.setOnHttpResponseListener(mHttpResponseListener);
            request.setMethod("GET");
            request.execute();
            HttpRequest secondRequest = new HttpRequest
                    ("http://api.themoviedb.org/3/movie/top_rated?api_key=84c758d1cd57e7b7a7947e8880c4648f");
            secondRequest.setOnHttpResponseListener(mHttpResponseListener);
            secondRequest.setMethod("GET");
            secondRequest.execute();
        } else {
            HttpRequest request = new HttpRequest
                    ("http://api.themoviedb.org/3/movie/" + sortBy + "?api_key=84c758d1cd57e7b7a7947e8880c4648f");
            request.setOnHttpResponseListener(mHttpResponseListener);
            request.setMethod("GET");
            request.execute();
        }

        mDb = AppDatabase.getInstance(getContext());




        // Deletes tasks on Swipe
//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            // Called when a user swipes left or right on a ViewHolder
//            @Override
//            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
//                // Here is where you'll implement swipe to delete
//                AppExecutors.getInstance().diskIO().execute(new Runnable() {
//                    @Override
//                    public void run() {
//                        int position = viewHolder.getAdapterPosition();
//                        List<Movie> movies = myAdapter.getmMovie();
//                        mDb.favMovDao().deleteMovieById(movies.get(position).getId());
//                        // Retreive Tasks() To Load!
//                    }
//                });
//            }
//        }).attachToRecyclerView(myAdapter);

        // Code for step 1 end
        return view;
    }

    // Code for step 2 start
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {
                    Log.d(DEBUG_TAG, "onResponse: FgMainPage Displayed");
//                    movieList.clear();
                    try {
                        JSONObject rootObj = new JSONObject(response);
                        JSONArray resultArr = rootObj.getJSONArray("results");

                        int vote_count, id;
                        boolean video, adult;
                        double vote_average, popularity;
                        String title, poster_path, original_language, original_title,
                                backdrop_path, overview, release_date;

                        for (int i = 0; i < resultArr.length(); i++) {

                            JSONObject resultObj = resultArr.getJSONObject(i);

                            vote_count = resultObj.getInt("vote_count");
                            id = resultObj.getInt("id");

                            video = resultObj.getBoolean("video");
                            adult = resultObj.getBoolean("adult");

                            vote_average = resultObj.getDouble("vote_average");
                            popularity = resultObj.getDouble("popularity");

                            title = resultObj.getString("title");
                            poster_path = resultObj.getString("poster_path");
                            original_language = resultObj.getString("original_language");
                            original_title = resultObj.getString("original_title");
                            backdrop_path = resultObj.getString("backdrop_path");
                            overview = resultObj.getString("overview");
                            release_date = resultObj.getString("release_date");

                            JSONArray genre_idsArr = resultObj.getJSONArray("genre_ids");

                            int[] genre_ids = new int[genre_idsArr.length()];

                            for (int o = 0; o < genre_idsArr.length(); o++) {
                                genre_ids[o] = genre_idsArr.getInt(o);
                            }

                            // TODO If the frag is fav, only get fav
                            Movie movie;

                            movie = new Movie(id, vote_count, vote_average, popularity,
                                    title, poster_path, original_language, original_title,
                                    backdrop_path, overview, release_date, video, adult, genre_ids);

                            movieList.add(movie);


//                            myAdapter = new RecyclerViewAdapter(getContext(), movieList);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d(DEBUG_TAG, "onResponse: " + movieList.size());
                    myAdapter.reloadRecycler(movieList);
                    myAdapter.notifyDataSetChanged();

                }
            };

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }
}
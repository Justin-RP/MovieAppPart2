package com.example.a16022916.movieapppart2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.a16022916.movieapppart2.database.AppDatabase;
import com.example.a16022916.movieapppart2.database.FavMovies;

import java.util.ArrayList;
import java.util.List;

import static android.widget.LinearLayout.VERTICAL;

public class FavFragment extends Fragment implements FavMovieAdapter.ItemClickListener {

    private View view;
    private RecyclerView recyclerView;
    private FavMovieAdapter mAdapter;
    private Button btn;

    private List<FavMovies> favMovies;
    private static final String DEBUG_TAG = "FavFragment";
    private AppDatabase mDb;

    private FavMovieViewModel favMovieViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fav_frag, container, false);
        favMovies = new ArrayList<>();
        recyclerView = view.findViewById(R.id.fRecyclerView);

        // Set the layout for the RecyclerView to be a linear layout, which measures and
        // positions items within a RecyclerView into a linear list
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize the adapter and attach it to the RecyclerView
        mAdapter = new FavMovieAdapter(getContext(), this);
        recyclerView.setAdapter(mAdapter);
//
//        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
//        recyclerView.addItemDecoration(decoration);

        favMovieViewModel = ViewModelProviders.of(getActivity()).get(FavMovieViewModel.class);
        favMovieViewModel.getAllFavMovie().observe(this, new Observer<List<FavMovies>>() {
            @Override
            public void onChanged(@Nullable List<FavMovies> favMovies) {
                mAdapter.setFavMovies(favMovies);
                Log.d(DEBUG_TAG, "onChanged: " + favMovies.size());
            }
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            // TODO 1) DELETES THE TASK
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position = viewHolder.getAdapterPosition();
                        List<FavMovies> favMovie = mAdapter.getmFavMoviesEntries();
                        favMovieViewModel.delete(favMovie.get(position));
                    }
                });
            }
        }).attachToRecyclerView(recyclerView);

//        insert();


//        mDb = AppDatabase.getInstance(getContext());
//        setupViewModel();
        return view;
    }


    @Override
    public void onItemClickListener(int itemId) {

    }



    private void deleteMovie(FavMovies movie) {
        Log.d(DEBUG_TAG, "deleteMovie: " + movie);
        favMovieViewModel.delete(movie);
    }


}

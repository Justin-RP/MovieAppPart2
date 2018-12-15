package com.example.a16022916.movieapppart2;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.a16022916.movieapppart2.database.AppDatabase;
import com.example.a16022916.movieapppart2.database.FavMovies;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String DEBUG_TAG = "MainActivity";
    private static final String STATE_FRAG = "fragName";
    private MovieFragment movieFragment;
    private AppDatabase mDb;
    private String fragName;


    @Override
    protected void onStart() {
        super.onStart();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupSharedPreferences();

        if (savedInstanceState != null){
            fragName = savedInstanceState.getString(STATE_FRAG);
            launchFrag(fragName);
        } else {
            fragName = "itemPopular";
            launchFrag(fragName);
        }

        mDb = AppDatabase.getInstance((getApplicationContext()));
        retrieveTasks();
    }

    private void setupSharedPreferences() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our visualizer_menu layout to this menu */
        inflater.inflate(R.menu.movie_menu, menu);
        /* Return true so that the visualizer_menu is displayed in the Toolbar */
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int itemId = item.getItemId();
        FragmentManager fm = getSupportFragmentManager();

        switch (itemId) {
            case R.id.itemPopular:
                Log.d(DEBUG_TAG, "onOptionsItemSelected: itemPopular Selected");
                fragName = "itemPopular";
                launchFrag(fragName);
                break;
            case R.id.itemTopRated:
                Log.d(DEBUG_TAG, "onOptionsItemSelected: itemTopRated Selected");
                fragName = "itemTopRated";
                launchFrag(fragName);
                break;
            case R.id.itemFavourite:
                Log.d(DEBUG_TAG, "onOptionsItemSelected: itemFavourite Selected");
                fragName = "itemFavourite";
                launchFrag(fragName);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    // func: To listen for changes in SharedPreference
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    private void launchFrag(String fragName) {
        if (fragName == "itemFavourite") {
            Bundle args = new Bundle();
            args.putString("fragType", fragName);
            FavFragment favFragment = new FavFragment();
            FragmentTransaction fragmentTransactionFav = getSupportFragmentManager().beginTransaction();
            fragmentTransactionFav.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransactionFav.replace(R.id.mFragment_movie, favFragment);
            fragmentTransactionFav.commit();
        } else if (fragName == "itemPopular") {
            Bundle args = new Bundle();
            args.putString("fragType", fragName); //false
            MovieFragment movieFragmentPopular = new MovieFragment();
            movieFragmentPopular.setArguments(args);
            FragmentTransaction fragmentTransactionPopular = getSupportFragmentManager().beginTransaction();
            fragmentTransactionPopular.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransactionPopular.replace(R.id.mFragment_movie, movieFragmentPopular);
            fragmentTransactionPopular.commit();
        } else if (fragName == "itemTopRated") {
            Bundle args = new Bundle();
            args.putString("fragType", fragName); //true
            MovieFragment movieFragmentTopRated = new MovieFragment();
            movieFragmentTopRated.setArguments(args);
            FragmentTransaction fragmentTransactionRated = getSupportFragmentManager().beginTransaction();
            fragmentTransactionRated.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
            fragmentTransactionRated.replace(R.id.mFragment_movie, movieFragmentTopRated);
            fragmentTransactionRated.commit();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister Listener for SharedPreferences
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    private void retrieveTasks() {
        // ONLY Change to the database will call trigger this, init only onCreate
        Log.d(DEBUG_TAG, "retrieveTasks: Actively retrieving from tasks from Db");
        final LiveData<List<FavMovies>> favMovie = mDb.favMovDao().loadAllMovies();
        favMovie.observe(this, new Observer<List<FavMovies>>() {
            @Override
            public void onChanged(@Nullable List<FavMovies> favMovies) {
                Log.d(DEBUG_TAG, "onChanged: Receiving db update from LiveData");
                //mAdapter.setTasks(favMovie)
            }
        });
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {


        outState.putString(STATE_FRAG, fragName);
        super.onSaveInstanceState(outState, outPersistentState);
    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        RecyclerView.LayoutManager layoutManager = getLayoutManager();
//        if(layoutManager != null && layoutManager instanceof LinearLayoutManager){
//            mScrollPosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//        }
//        SavedState newState = new SavedState(superState);
//        newState.mScrollPosition = mScrollPosition;
//        return newState;
//        outState.putString(STATE_FRAG, fragName);
//    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    static class SavedState extends android.view.View.BaseSavedState {
        public int mScrollPosition;
        SavedState(Parcel in) {
            super(in);
            mScrollPosition = in.readInt();
        }
        SavedState(Parcelable superState) {
            super(superState);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(mScrollPosition);
        }
        public static final Parcelable.Creator<SavedState> CREATOR
                = new Parcelable.Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}

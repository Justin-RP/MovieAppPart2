package com.example.a16022916.movieapppart2.Review;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.a16022916.movieapppart2.BuildConfig;
import com.example.a16022916.movieapppart2.Movie;
import com.example.a16022916.movieapppart2.R;
import com.example.a16022916.movieapppart2.Reviews;
import com.example.a16022916.movieapppart2.utilities.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ReviewFragment extends Fragment {

    private static final String DEBUG_TAG = "ReviewFragment";
    //ADDED
    List<Reviews> reviewsList;
    RecyclerViewAdapterFragment myAdapter;
    View view;
    RecyclerView recyclerView;


    public ReviewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String movieId = getArguments().getString("movieId", "");

        Log.d(DEBUG_TAG, "onCreateView: " + movieId);


        reviewsList = new ArrayList<>();

        view = inflater.inflate(R.layout.fragment_movie, container, false);
        recyclerView = view.findViewById(R.id.mRecycler_view);
        myAdapter = new RecyclerViewAdapterFragment(getContext(), reviewsList);

        HttpRequest request = new HttpRequest
                ("http://api.themoviedb.org/3/movie/"+movieId+"/reviews?api_key=84c758d1cd57e7b7a7947e8880c4648f");

        request.setOnHttpResponseListener(mHttpResponseListener);
        request.setMethod("GET");
        request.execute();
        // Code for step 1 end
        return view;
    }

    // Code for step 2 start
    private HttpRequest.OnHttpResponseListener mHttpResponseListener =
            new HttpRequest.OnHttpResponseListener() {
                @Override
                public void onResponse(String response) {
                    Log.d(DEBUG_TAG, "onResponse: FgMainPage Displayed");
                    reviewsList.clear();
                    try {
                        JSONObject rootObj = new JSONObject(response);
                        JSONArray resultArr = rootObj.getJSONArray("results");

                        Reviews review;
                        String author, content, reviewId, url;

                        for (int i = 0; i < resultArr.length(); i++) {

                            JSONObject resultObj = resultArr.getJSONObject(i);

                            author = resultObj.getString("author");
                            content = resultObj.getString("content");
                            reviewId = resultObj.getString("id");
                            url = resultObj.getString("url");

                            review = new Reviews(author, content, reviewId, url);
                            reviewsList.add(review);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (reviewsList.size() == 0) {
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
                        recyclerView.setAdapter(myAdapter);
                        myAdapter.reloadRecycler(reviewsList);
                        myAdapter.notifyDataSetChanged();
                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),reviewsList.size()));
                        recyclerView.setAdapter(myAdapter);
                        myAdapter.reloadRecycler(reviewsList);
                        myAdapter.notifyDataSetChanged();
                    }
                }
            };
}
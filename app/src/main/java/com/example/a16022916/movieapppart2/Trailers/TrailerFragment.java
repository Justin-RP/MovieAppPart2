package com.example.a16022916.movieapppart2.Trailers;

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
import com.example.a16022916.movieapppart2.Video;
import com.example.a16022916.movieapppart2.utilities.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TrailerFragment extends Fragment {

    private static final String DEBUG_TAG = "TrailerFragment";
    //ADDED
    List<Video> videoList;
    RecyclerViewAdapterFragmentTrailer myAdapter;
    View view;
    RecyclerView recyclerView;


    public TrailerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String movieId = getArguments().getString("movieId", "");

        Log.d(DEBUG_TAG, "onCreateView: " + movieId);


        videoList = new ArrayList<>();

        view = inflater.inflate(R.layout.fragment_movie, container, false);
        recyclerView = view.findViewById(R.id.mRecycler_view);
        myAdapter = new RecyclerViewAdapterFragmentTrailer(getContext(), videoList);



        HttpRequest request = new HttpRequest
                ("http://api.themoviedb.org/3/movie/"+movieId+"/videos?api_key=" + BuildConfig.ApiKey);

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
                    videoList.clear();
                    try {
                        JSONObject rootObj = new JSONObject(response);
                        JSONArray resultArr = rootObj.getJSONArray("results");

                        Video video;
                        String id, iso_639_1,iso_3166_1, key, name, site, type;
                        int size;

                        for (int i = 0; i < resultArr.length(); i++) {

                            JSONObject resultObj = resultArr.getJSONObject(i);

                            id = resultObj.getString("id");
                            iso_639_1 = resultObj.getString("iso_639_1");
                            iso_3166_1 = resultObj.getString("iso_3166_1");
                            key = resultObj.getString("key");
                            name = resultObj.getString("name");
                            site = resultObj.getString("site");
                            type = resultObj.getString("type");
                            size = resultObj.getInt("size");

                            video = new Video(id, iso_639_1, iso_3166_1, key, name, site, type, size);
                            videoList.add(video);

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (videoList.size() == 0) {
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 1));
                        recyclerView.setAdapter(myAdapter);
                        myAdapter.reloadRecycler(videoList);
                        myAdapter.notifyDataSetChanged();

                    } else {
                        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), videoList.size()));
                        recyclerView.setAdapter(myAdapter);
                        myAdapter.reloadRecycler(videoList);
                        myAdapter.notifyDataSetChanged();
                    }



                }
            };
}
package com.example.a16022916.movieapppart2.Trailers;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.example.a16022916.movieapppart2.R;
import com.example.a16022916.movieapppart2.Reviews;
import com.example.a16022916.movieapppart2.Video;

import java.util.List;

public class RecyclerViewAdapterFragmentTrailer extends RecyclerView.Adapter<RecyclerViewAdapterFragmentTrailer.MyViewHolder> {

    private static final String DEBUG_TAG = "RVAFTrailer";
    private Context mContext;
    private List<Video> videoList;

    public RecyclerViewAdapterFragmentTrailer(Context mContext, List<Video> videoList) {
        this.mContext = mContext;
        this.videoList = videoList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.video_row,parent,false);
        Log.d(DEBUG_TAG, "onBindViewHolder: " + this.videoList);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final String key;

        key = videoList.get(position).getKey();

        String url = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/"+ key+ "\"frameboarder=\"0\" allowfullscreen></iframe>";

        String size = "w185/";

        holder.webView.loadData( url, "text/html", "utf-8");
        Log.d(DEBUG_TAG, "TrailerUrl: " + url);

    }

    public void reloadRecycler(List<Video> videoList) {
        this.videoList = videoList;
        Log.d(DEBUG_TAG, "reloadRecycler: count:" + videoList.size());
    }


    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        WebView webView;


        public MyViewHolder(View itemView){
            super(itemView);

            webView = itemView.findViewById(R.id.vRowWvVideo);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebChromeClient(new WebChromeClient());
        }
    }
}

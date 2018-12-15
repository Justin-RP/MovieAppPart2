package com.example.a16022916.movieapppart2;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private static final String DEBUG_TAG = "RecyclerViewAdapter";
    private Context mContext;
    private List<Movie> mMovie;

    public RecyclerViewAdapter(Context mContext, List<Movie> mMovie) {
        this.mContext = mContext;
        this.mMovie = mMovie;
        Log.d(DEBUG_TAG, "RecyclerViewAdapter: " + this.mMovie);
    }

    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
      View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.movie_row,parent,false);
        Log.d(DEBUG_TAG, "onBindViewHolder: " + this.mMovie);
        return new MyViewHolder(view);

    }

    public List<Movie> getmMovie() {
        return mMovie;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.tvTitle.setText(mMovie.get(position).getTitle());
        Log.d(DEBUG_TAG, "onBindViewHolder: " + mMovie);
        if(mMovie.get(position).getPoster_path().equalsIgnoreCase("N/A")){
            holder.Image.setImageResource(R.drawable.noimage);
        }else{

            String baseUrl = "http://image.tmdb.org/t/p/";
            String size = "w185/";
            Picasso.with(mContext).load(baseUrl + size + mMovie.get(position).getPoster_path()).into(holder.Image);
            Log.d(DEBUG_TAG, "onBindViewHolder: " + mMovie.get(position).getPoster_path());
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mContext,MovieDetails.class);
                Movie m = mMovie.get(position);
                i.putExtra("movieData",m);
                mContext.startActivity(i);

            }
        });
    }

    public void reloadRecycler(List<Movie> rMovie) {
        mMovie = rMovie;
        Log.d(DEBUG_TAG, "reloadRecycler: count:" + mMovie.size());
    }


    @Override
    public int getItemCount() {
        return mMovie.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvTitle;
        ImageView Image;
        CardView cardView;


        public MyViewHolder(View itemView){
            super(itemView);
            tvTitle = itemView.findViewById(R.id.rowTvTitle);
            Image = itemView.findViewById(R.id.rowCardImg);
            cardView = itemView.findViewById(R.id.rowCardView_id);

        }

    }


}

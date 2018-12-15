package com.example.a16022916.movieapppart2;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.a16022916.movieapppart2.database.FavMovies;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FavMovieAdapter extends RecyclerView.Adapter<FavMovieAdapter.FavMoviesViewHolder>{
    // Constant for date format
    private static final String DATE_FORMAT = "dd/MM/yyy";

    // Member variable to handle item clicks
    final private ItemClickListener mItemClickListener;
    private List<FavMovies> mFavMoviesEntries;
    private Context mContext;
    // Date formatter
    private SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private FavMovieViewModel favMovieViewModel;

    public FavMovieAdapter(Context context, ItemClickListener listener) {
        mContext = context;
        mItemClickListener = listener;
    }


    @Override
    public FavMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.fav_row, parent, false);

        return new FavMoviesViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display data at a specified position in the Cursor.
     *
     * @param holder   The ViewHolder to bind Cursor data to
     * @param position The position of the data in the Cursor
     */
    public void onBindViewHolder(FavMoviesViewHolder holder, final int position) {
        // Determine the values of the wanted data
        final FavMovies favMovies = mFavMoviesEntries.get(position);
        final int myPosition = position;
        String avgVote = favMovies.getAvgVote();
        String strPoster = favMovies.getMoviePoster();
        final String desc = favMovies.getPlot();
        String date = favMovies.getReleaseDate();
        final String title = favMovies.getTitle();

        Activity activity = (Activity) mContext;
        favMovieViewModel = ViewModelProviders.of((FragmentActivity) activity).get(FavMovieViewModel.class);


        //Set values
        holder.title.setText(title);
        holder.avgVote.setText(avgVote);
        holder.date.setText(avgVote);

        holder.avgVote.setText(date);

        if(strPoster.equalsIgnoreCase("N/A")){
            holder.poster.setImageResource(R.drawable.noimage);
        }else{
            String baseUrl = "http://image.tmdb.org/t/p/";
            String size = "w185/";
            Picasso.with(mContext.getApplicationContext()).load(baseUrl + size + strPoster).into(holder.poster);
        }

        holder.ivStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        favMovieViewModel.delete(favMovies);
                    }
                });
            }
        });


        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle(title);
                builder.setMessage(desc);
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mFavMoviesEntries == null) {
            return 0;
        }
        return mFavMoviesEntries.size();
    }

    public List<FavMovies> getmFavMoviesEntries() {
        return mFavMoviesEntries;
    }


    public void setFavMovies(List<FavMovies> favEntries) {
        mFavMoviesEntries = favEntries;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onItemClickListener(int itemId);

    }

    // Inner class for creating ViewHolders
    class FavMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView avgVote, desc, date, title;
        Button btnMore;
        ImageView poster;
        ImageView ivStar;

        public FavMoviesViewHolder(View itemView) {
            super(itemView);
            avgVote = itemView.findViewById(R.id.fTvRating);
            poster = itemView.findViewById(R.id.fImage);
            date = itemView.findViewById(R.id.fTvDate);
            title = itemView.findViewById(R.id.fTvTitle);
            btnMore = itemView.findViewById(R.id.fBtnReadMore);
            ivStar = itemView.findViewById(R.id.fIvStar);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int elementId = mFavMoviesEntries.get(getAdapterPosition()).getId();
            mItemClickListener.onItemClickListener(elementId);
        }
    }
}

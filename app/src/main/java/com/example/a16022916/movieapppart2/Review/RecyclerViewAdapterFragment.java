package com.example.a16022916.movieapppart2.Review;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.a16022916.movieapppart2.R;
import com.example.a16022916.movieapppart2.Reviews;
import com.example.a16022916.movieapppart2.Video;

import java.util.List;

public class RecyclerViewAdapterFragment extends RecyclerView.Adapter<RecyclerViewAdapterFragment.MyViewHolder> {

    private static final String DEBUG_TAG = "RecyclerViewAdapterFrag";
    private Context mContext;
    private List<Reviews> mReviews;

    public RecyclerViewAdapterFragment(Context mContext, List<Reviews> mReviews) {
        this.mContext = mContext;
        this.mReviews = mReviews;
        Log.d(DEBUG_TAG, "RecyclerViewAdapter: " + this.mReviews);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.review_row,parent,false);
        Log.d(DEBUG_TAG, "onBindViewHolder: " + this.mReviews);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final String author, content;

        author = mReviews.get(position).getAuthor();
        content = mReviews.get(position).getContent();

        int length = content.split("(?!^)").length;
        holder.tvContent.setText(content);
        holder.tvAuthor.setText(author);
        if (length > 100) {
            holder.tvContent.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0 , 3));
//            holder.tv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0 , 3));
            holder.btnMore.setVisibility(View.VISIBLE);
        }

        Log.d(DEBUG_TAG, "onBindViewHolder: " + mReviews);

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setCancelable(true);
                builder.setTitle(author);
                builder.setMessage(content);
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

    public void reloadRecycler(List<Reviews> rReviews) {
        mReviews = rReviews;
        Log.d(DEBUG_TAG, "reloadRecycler: count:" + mReviews.size());
    }


    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView tvAuthor, tvContent;
        Button btnMore;


        public MyViewHolder(View itemView){
            super(itemView);

            tvAuthor = itemView.findViewById(R.id.rowTvAuthor);
            tvContent = itemView.findViewById(R.id.rowTvContent);
            btnMore = itemView.findViewById(R.id.cardBtnReadMore);

        }
    }
}

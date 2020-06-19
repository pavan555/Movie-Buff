package com.pavan.moviebuff.Utils.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pavan.moviebuff.R;
import com.pavan.moviebuff.Utils.DTO.Movie;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private Context context;
    private ItemClickListener mClickListener;
    private ArrayList<Movie> movieArrayList;


    public DataAdapter(ArrayList<Movie> movies) {
        this.movieArrayList = movies;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        if (movieArrayList != null) {
            Glide.with(context)
                    .load(movieArrayList.get(position).getPoster())
                    .thumbnail(0.25f)
                    .placeholder(R.drawable.film_poster_placeholder)
                    .centerCrop()
                    .into(holder.itemImage);
            holder.itemText.setText(movieArrayList.get(position).getTitle());//error add in xml first
            holder.itemText.setEllipsize(TextUtils.TruncateAt.MARQUEE);
            holder.itemText.setSingleLine(true);
            holder.itemText.setMarqueeRepeatLimit(5);
            holder.itemText.setSelected(true);
        }
    }

    @Override
    public int getItemCount() {
        if (movieArrayList != null)
            return movieArrayList.size();
        return 0;
    }


    public void clearAndUpdateFiles(ArrayList<Movie> movies) {
        this.movieArrayList.clear();
        this.movieArrayList = movies;
        notifyDataSetChanged();
    }

    public void updateFiles(ArrayList<Movie> movies) {
        this.movieArrayList.addAll(movies);
        notifyDataSetChanged();
    }

    public Integer getMovieId(int pos) {
        return movieArrayList.get(pos).getId();
    }


    /**
     * ---------------------
     * // Click Listener
     * // ----------------------
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
    }

    public Movie getMovie(int position) {
        return movieArrayList.get(position);
    }


    /**
     * Interface itemCLickListener
     */

    public interface ItemClickListener {
        void onItemClick(View view, int position);

        boolean onItemLongClick(View view, int position);
    }

    /**
     * ViewHolder Class
     */

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;
        ImageView itemImage;
        RelativeLayout text;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            itemText = itemView.findViewById(R.id.itemText);
            itemImage = itemView.findViewById(R.id.imageView);
            text = itemView.findViewById(R.id.text);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mClickListener != null)
                        mClickListener.onItemClick(itemView, getAdapterPosition());
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (mClickListener != null)
                        return mClickListener.onItemLongClick(itemView, getAdapterPosition());
                    return false;
                }
            });
        }


    }


}

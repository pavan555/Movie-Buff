package com.pavan.moviebuff.Utils.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textview.MaterialTextView;
import com.pavan.moviebuff.R;
import com.pavan.moviebuff.Utils.DTO.Reviews;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {

    private Context context;
    private ItemClickListener mClickListener;
    private ArrayList<Reviews.Review> reviewArrayList = null;

    public ReviewAdapter(ArrayList<Reviews.Review> reviewArrayList) {
        this.reviewArrayList = reviewArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if (reviewArrayList != null) {
            holder.itemContent.setText(reviewArrayList.get(position).getReview());
            holder.userNameText.setText(Html.fromHtml(reviewArrayList.get(position).getAuthor()));
            if (reviewArrayList.get(position).getReview().length() > 100) {
                holder.readMore.setVisibility(View.VISIBLE);
                holder.readMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(reviewArrayList.get(position).getReviewUrl()));
                        v.getContext().startActivity(intent);
                    }
                });
            } else {
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) holder.userNameText.getLayoutParams();
                params.setMargins(0, 8, 0, 8);
                holder.userNameText.setLayoutParams(params);
                holder.readMore.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (reviewArrayList != null)
            return reviewArrayList.size();
        return 0;
    }


    public void clearAndUpdateFiles(ArrayList<Reviews.Review> reviews) {
        this.reviewArrayList.clear();
        this.reviewArrayList = reviews;
        notifyDataSetChanged();
    }

    public void updateFiles(ArrayList<Reviews.Review> reviews) {
        this.reviewArrayList.addAll(reviews);
        notifyDataSetChanged();
    }


    /**
     * ---------------------
     * // Click Listener
     * // ----------------------
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        mClickListener = itemClickListener;
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

        MaterialTextView itemContent, userNameText;
        MaterialButton readMore;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            itemContent = itemView.findViewById(R.id.content);
            userNameText = itemView.findViewById(R.id.user);
            readMore = itemView.findViewById(R.id.read_more);

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

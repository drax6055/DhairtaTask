package com.dhairya.dhairtatask.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dhairya.dhairtatask.Model.Review;
import com.dhairya.dhairtatask.R;

import java.util.List;

public class ReviewAdapter   extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<Review> reviewList;

    public ReviewAdapter(List<Review> reviewList) {
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewList.get(position);
        holder.reviewerName.setText(review.getReviewerName());
        holder.reviewRating.setText("Rating: " + review.getRating());
        holder.reviewComment.setText(review.getComment());
        holder.reviewDate.setText(review.getDate());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewerName, reviewRating, reviewComment, reviewDate;

        ReviewViewHolder(View itemView) {
            super(itemView);
            reviewerName = itemView.findViewById(R.id.reviewerName);
            reviewRating = itemView.findViewById(R.id.reviewRating);
            reviewComment = itemView.findViewById(R.id.reviewComment);
            reviewDate = itemView.findViewById(R.id.reviewDate);
        }
    }
}

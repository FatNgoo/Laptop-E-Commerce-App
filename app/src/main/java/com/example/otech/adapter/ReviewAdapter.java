package com.example.otech.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.model.Review;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    public interface OnReviewActionListener {
        void onDeleteReview(Review review, int position);
        void onViewProduct(Review review);
    }

    private Context context;
    private ArrayList<Review> reviews;
    private boolean isAdminMode;
    private OnReviewActionListener listener;

    public ReviewAdapter(Context context, ArrayList<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
        this.isAdminMode = false;
    }

    public ReviewAdapter(Context context, ArrayList<Review> reviews, boolean isAdminMode, OnReviewActionListener listener) {
        this.context = context;
        this.reviews = reviews;
        this.isAdminMode = isAdminMode;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);

        holder.tvReviewerName.setText(review.getUserName());
        holder.ratingBar.setRating(review.getRating());
        holder.tvReviewComment.setText(review.getComment());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        holder.tvReviewDate.setText(sdf.format(review.getReviewDate()));

        // Show admin actions if in admin mode
        if (isAdminMode && listener != null) {
            holder.tvProductName.setVisibility(View.VISIBLE);
            holder.tvProductName.setText("Product ID: " + review.getProductId());
            
            holder.layoutAdminActions.setVisibility(View.VISIBLE);
            
            holder.btnViewProduct.setOnClickListener(v -> listener.onViewProduct(review));
            holder.btnDeleteReview.setOnClickListener(v -> listener.onDeleteReview(review, position));
        } else {
            holder.tvProductName.setVisibility(View.GONE);
            holder.layoutAdminActions.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public void updateReviews(ArrayList<Review> newReviews) {
        this.reviews = newReviews;
        notifyDataSetChanged();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView tvReviewerName, tvReviewDate, tvReviewComment, tvProductName;
        RatingBar ratingBar;
        LinearLayout layoutAdminActions;
        MaterialButton btnViewProduct, btnDeleteReview;

        ReviewViewHolder(View itemView) {
            super(itemView);
            tvReviewerName = itemView.findViewById(R.id.txt_reviewer_name);
            tvReviewDate = itemView.findViewById(R.id.txt_review_date);
            tvReviewComment = itemView.findViewById(R.id.txt_review_comment);
            tvProductName = itemView.findViewById(R.id.tvProductName);
            ratingBar = itemView.findViewById(R.id.rating_bar);
            layoutAdminActions = itemView.findViewById(R.id.layoutAdminActions);
            btnViewProduct = itemView.findViewById(R.id.btnViewProduct);
            btnDeleteReview = itemView.findViewById(R.id.btnDeleteReview);
        }
    }
}

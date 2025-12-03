package com.example.otech.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otech.R;
import com.example.otech.model.Review;
import com.example.otech.repository.MockDataStore;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReviewAdminAdapter extends RecyclerView.Adapter<ReviewAdminAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Review> reviews;
    private ArrayList<Review> reviewsFiltered;
    private MockDataStore dataStore;
    private OnReviewDeletedListener listener;

    public interface OnReviewDeletedListener {
        void onReviewDeleted();
    }

    public ReviewAdminAdapter(Context context, ArrayList<Review> reviews, OnReviewDeletedListener listener) {
        this.context = context;
        this.reviews = reviews;
        this.reviewsFiltered = new ArrayList<>(reviews);
        this.dataStore = MockDataStore.getInstance();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review_admin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Review review = reviewsFiltered.get(position);
        
        // User name and initial
        holder.tvUserName.setText(review.getUserName());
        String initial = review.getUserName().substring(0, 1).toUpperCase();
        holder.tvUserInitial.setText(initial);
        
        // Review date
        holder.tvReviewDate.setText(getTimeAgo(review.getReviewDate()));
        
        // Rating stars
        setRatingStars(holder, review.getRating());
        
        // Comment
        holder.tvComment.setText(review.getComment());
        
        // Delete button
        holder.btnDelete.setOnClickListener(v -> showDeleteConfirmDialog(review));
    }

    @Override
    public int getItemCount() {
        return reviewsFiltered.size();
    }

    private void setRatingStars(ViewHolder holder, float rating) {
        ImageView[] stars = {
            holder.ivStar1, holder.ivStar2, holder.ivStar3, 
            holder.ivStar4, holder.ivStar5
        };
        
        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setColorFilter(context.getColor(android.R.color.holo_orange_light));
            } else {
                stars[i].setColorFilter(context.getColor(android.R.color.darker_gray));
            }
        }
    }

    private String getTimeAgo(Date date) {
        long timeInMillis = date.getTime();
        long currentTime = System.currentTimeMillis();
        long diff = currentTime - timeInMillis;

        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (seconds < 60) {
            return "Vừa xong";
        } else if (minutes < 60) {
            return minutes + " phút trước";
        } else if (hours < 24) {
            return hours + " giờ trước";
        } else if (days < 7) {
            return days + " ngày trước";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return sdf.format(date);
        }
    }

    private void showDeleteConfirmDialog(Review review) {
        new AlertDialog.Builder(context)
            .setTitle("Xóa đánh giá")
            .setMessage("Bạn có chắc muốn xóa đánh giá này?")
            .setPositiveButton("Xóa", (dialog, which) -> {
                boolean success = dataStore.deleteReview(review.getId());
                if (success) {
                    reviews.remove(review);
                    reviewsFiltered.remove(review);
                    notifyDataSetChanged();
                    Toast.makeText(context, "Đã xóa đánh giá", Toast.LENGTH_SHORT).show();
                    
                    if (listener != null) {
                        listener.onReviewDeleted();
                    }
                } else {
                    Toast.makeText(context, "Không thể xóa đánh giá", Toast.LENGTH_SHORT).show();
                }
            })
            .setNegativeButton("Hủy", null)
            .show();
    }

    public void filter(float minRating, float maxRating) {
        reviewsFiltered.clear();
        
        if (minRating == 0 && maxRating == 0) {
            reviewsFiltered.addAll(reviews);
        } else {
            for (Review review : reviews) {
                float rating = review.getRating();
                
                if (maxRating == 0) {
                    // Only min rating filter
                    if (rating >= minRating) {
                        reviewsFiltered.add(review);
                    }
                } else {
                    // Range filter
                    if (rating >= minRating && rating <= maxRating) {
                        reviewsFiltered.add(review);
                    }
                }
            }
        }
        
        notifyDataSetChanged();
    }

    public void updateData(ArrayList<Review> newReviews) {
        this.reviews = newReviews;
        this.reviewsFiltered = new ArrayList<>(newReviews);
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvUserInitial, tvReviewDate, tvComment;
        ImageView ivStar1, ivStar2, ivStar3, ivStar4, ivStar5;
        MaterialButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUserInitial = itemView.findViewById(R.id.tvUserInitial);
            tvReviewDate = itemView.findViewById(R.id.tvReviewDate);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivStar1 = itemView.findViewById(R.id.ivStar1);
            ivStar2 = itemView.findViewById(R.id.ivStar2);
            ivStar3 = itemView.findViewById(R.id.ivStar3);
            ivStar4 = itemView.findViewById(R.id.ivStar4);
            ivStar5 = itemView.findViewById(R.id.ivStar5);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}

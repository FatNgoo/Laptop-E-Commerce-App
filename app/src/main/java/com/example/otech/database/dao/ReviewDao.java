package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.otech.model.Review;

import java.util.List;

@Dao
public interface ReviewDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Review review);

    @Update
    void update(Review review);

    @Delete
    void delete(Review review);

    @Query("SELECT * FROM reviews")
    List<Review> getAll();

    @Query("SELECT * FROM reviews WHERE productId = :productId ORDER BY reviewDate DESC")
    List<Review> getByProductId(String productId);

    @Query("SELECT * FROM reviews WHERE userId = :userId AND productId = :productId LIMIT 1")
    Review getUserReviewForProduct(String userId, String productId);

    @Query("DELETE FROM reviews WHERE id = :reviewId")
    void deleteById(String reviewId);

    @Query("DELETE FROM reviews")
    void deleteAll();
    
    @Query("SELECT AVG(rating) FROM reviews WHERE productId = :productId")
    float getAverageRating(String productId);
    
    @Query("SELECT COUNT(*) FROM reviews WHERE productId = :productId")
    int getReviewCount(String productId);
}

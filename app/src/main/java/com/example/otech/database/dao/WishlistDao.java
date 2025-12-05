package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.otech.model.Wishlist;

import java.util.List;

@Dao
public interface WishlistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Wishlist wishlist);

    @Delete
    void delete(Wishlist wishlist);

    @Query("SELECT * FROM wishlist WHERE userId = :userId ORDER BY addedDate DESC")
    List<Wishlist> getByUserId(String userId);

    @Query("SELECT productId FROM wishlist WHERE userId = :userId")
    List<String> getProductIdsByUserId(String userId);

    @Query("SELECT COUNT(*) FROM wishlist WHERE userId = :userId AND productId = :productId")
    int isInWishlist(String userId, String productId);

    @Query("DELETE FROM wishlist WHERE userId = :userId AND productId = :productId")
    void deleteByUserAndProduct(String userId, String productId);

    @Query("DELETE FROM wishlist")
    void deleteAll();
}

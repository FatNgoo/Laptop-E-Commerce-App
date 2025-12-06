package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.otech.model.CartItem;

import java.util.List;

@Dao
public interface CartItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CartItem cartItem);

    @Update
    void update(CartItem cartItem);

    @Delete
    void delete(CartItem cartItem);

    @Query("SELECT * FROM cart_items WHERE userId = :userId")
    List<CartItem> getCartByUserId(String userId);

    @Query("SELECT * FROM cart_items WHERE id = :cartItemId LIMIT 1")
    CartItem getById(String cartItemId);

    @Query("DELETE FROM cart_items WHERE userId = :userId")
    void clearCart(String userId);

    @Query("DELETE FROM cart_items")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM cart_items WHERE userId = :userId")
    int getCartCount(String userId);
    
    @Query("SELECT * FROM cart_items WHERE userId = :userId AND product_id = :productId LIMIT 1")
    CartItem getCartItemByUserAndProduct(String userId, String productId);
}

package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.otech.model.Order;

import java.util.List;

@Dao
public interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Order order);

    @Update
    void update(Order order);

    @Delete
    void delete(Order order);

    @Query("SELECT * FROM orders")
    List<Order> getAll();

    @Query("SELECT * FROM orders WHERE id = :orderId LIMIT 1")
    Order getById(String orderId);

    @Query("SELECT * FROM orders WHERE userId = :userId ORDER BY orderDate DESC")
    List<Order> getByUserId(String userId);

    @Query("SELECT * FROM orders WHERE status = :status")
    List<Order> getByStatus(String status);

    @Query("DELETE FROM orders")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM orders")
    int getCount();
    
    @Query("SELECT COUNT(*) FROM orders WHERE status = :status")
    int getCountByStatus(String status);
}

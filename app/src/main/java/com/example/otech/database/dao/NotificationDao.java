package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.otech.model.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Notification notification);

    @Update
    void update(Notification notification);

    @Delete
    void delete(Notification notification);

    @Query("SELECT * FROM notifications WHERE id = :id LIMIT 1")
    Notification getById(String id);

    @Query("SELECT * FROM notifications WHERE userId = :userId ORDER BY timestamp DESC")
    List<Notification> getByUserId(String userId);

    @Query("SELECT COUNT(*) FROM notifications WHERE userId = :userId AND isRead = 0")
    int getUnreadCount(String userId);

    @Query("UPDATE notifications SET isRead = 1 WHERE userId = :userId")
    void markAllAsRead(String userId);

    @Query("DELETE FROM notifications")
    void deleteAll();
}

package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.otech.model.ViewHistory;

import java.util.List;

@Dao
public interface ViewHistoryDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ViewHistory viewHistory);
    
    @Update
    void update(ViewHistory viewHistory);
    
    @Query("SELECT * FROM view_history WHERE userId = :userId AND productId = :productId LIMIT 1")
    ViewHistory getViewHistory(String userId, String productId);
    
    @Query("SELECT * FROM view_history WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    List<ViewHistory> getRecentlyViewed(String userId, int limit);
    
    @Query("SELECT * FROM view_history WHERE userId = :userId ORDER BY viewCount DESC, timestamp DESC LIMIT :limit")
    List<ViewHistory> getMostViewed(String userId, int limit);
    
    @Query("DELETE FROM view_history WHERE userId = :userId")
    void deleteAllForUser(String userId);
    
    @Query("DELETE FROM view_history WHERE userId = :userId AND productId = :productId")
    void deleteViewHistory(String userId, String productId);
}

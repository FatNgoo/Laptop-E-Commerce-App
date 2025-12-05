package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.otech.model.UserPreference;

@Dao
public interface UserPreferenceDao {
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(UserPreference preference);
    
    @Update
    void update(UserPreference preference);
    
    @Query("SELECT * FROM user_preferences WHERE userId = :userId LIMIT 1")
    UserPreference getUserPreference(String userId);
    
    @Query("DELETE FROM user_preferences WHERE userId = :userId")
    void deleteUserPreference(String userId);
}

package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.otech.model.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(User user);

    @Update
    void update(User user);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    User getById(String userId);

    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    User getByUsername(String username);

    @Query("SELECT * FROM users WHERE username = :username AND password = :password LIMIT 1")
    User login(String username, String password);

    @Query("SELECT * FROM users WHERE role != 'admin'")
    List<User> getAllNonAdminUsers();

    @Query("DELETE FROM users")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM users")
    int getCount();
}

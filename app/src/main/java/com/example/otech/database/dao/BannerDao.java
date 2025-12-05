package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.otech.model.Banner;

import java.util.List;

@Dao
public interface BannerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(Banner banner);

    @Update
    int update(Banner banner);

    @Delete
    int delete(Banner banner);

    @Query("SELECT * FROM banners WHERE id = :id LIMIT 1")
    Banner getById(String id);

    @Query("SELECT * FROM banners WHERE isActive = 1 ORDER BY `order` ASC")
    List<Banner> getAllActiveBanners();

    @Query("SELECT * FROM banners ORDER BY `order` ASC")
    List<Banner> getAllBanners();

    @Query("DELETE FROM banners WHERE id = :id")
    int deleteById(String id);
}

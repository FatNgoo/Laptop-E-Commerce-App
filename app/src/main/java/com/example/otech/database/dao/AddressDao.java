package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.otech.model.Address;

import java.util.List;

@Dao
public interface AddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Address address);

    @Update
    void update(Address address);

    @Delete
    void delete(Address address);

    @Query("SELECT * FROM addresses WHERE userId = :userId")
    List<Address> getByUserId(String userId);

    @Query("SELECT * FROM addresses WHERE userId = :userId AND isDefault = 1 LIMIT 1")
    Address getDefaultAddress(String userId);

    @Query("UPDATE addresses SET isDefault = 0 WHERE userId = :userId")
    void clearDefaultAddresses(String userId);

    @Query("DELETE FROM addresses")
    void deleteAll();
}

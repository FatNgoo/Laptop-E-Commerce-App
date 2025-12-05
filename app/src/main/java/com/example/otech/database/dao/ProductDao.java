package com.example.otech.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.otech.model.Product;

import java.util.List;

@Dao
public interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Product product);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Product> products);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);

    @Query("SELECT * FROM products")
    List<Product> getAll();

    @Query("SELECT * FROM products WHERE id = :productId LIMIT 1")
    Product getById(String productId);

    @Query("SELECT * FROM products WHERE category = :category")
    List<Product> getByCategory(String category);

    @Query("SELECT * FROM products WHERE name LIKE '%' || :query || '%' OR brand LIKE '%' || :query || '%'")
    List<Product> search(String query);

    @Query("SELECT * FROM products ORDER BY soldCount DESC LIMIT :limit")
    List<Product> getBestSellers(int limit);

    @Query("SELECT * FROM products WHERE oldPrice > price ORDER BY RANDOM() LIMIT :limit")
    List<Product> getPromotionProducts(int limit);

    @Query("SELECT * FROM products WHERE brand = :brand")
    List<Product> getByBrand(String brand);

    @Query("DELETE FROM products")
    void deleteAll();
    
    @Query("SELECT COUNT(*) FROM products")
    int getCount();
}

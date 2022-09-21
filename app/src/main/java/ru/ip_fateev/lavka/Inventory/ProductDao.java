package ru.ip_fateev.lavka.Inventory;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProductDao {
    @Query("SELECT * FROM product")
    //LiveData<List<Product>> getAll();
    List<Product> getAll();

    @Query("SELECT * FROM product")
    LiveData<List<Product>> getAllLive();

    @Query("SELECT * FROM product WHERE id = :id")
    Product getById(long id);

    @Insert
    void insert(Product product);

    @Update
    void update(Product product);

    @Delete
    void delete(Product product);
}

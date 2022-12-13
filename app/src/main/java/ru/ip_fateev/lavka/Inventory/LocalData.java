package ru.ip_fateev.lavka.Inventory;

import android.content.Context;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;

@Database(entities = {Product.class}, version = 1)
abstract class LocalDatabase extends RoomDatabase {
    public abstract ProductDao productDao();
}

public class LocalData {

    private LocalDatabase db;
    public LocalData(Context applicationContext, String filename) {
        db =  Room.databaseBuilder(applicationContext,
                LocalDatabase.class,
                filename)
                .fallbackToDestructiveMigration()
                .build();
    }

    public List<Product> getProductList() {
        List<Product> products = db.productDao().getAll();
        return products;
    }

    public void InsertProduct(Product val) {
        db.productDao().insert(val);
    }

    public void UpdateProduct(Product val) {
        db.productDao().update(val);
    }

    public LiveData<List<Product>> getProductListLive(LifecycleOwner activity, Observer<List<Product>> observer){
        LiveData<List<Product>> products = db.productDao().getAllLive();
        products.observe(activity, observer);
        return products;
    }
}

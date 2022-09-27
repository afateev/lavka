package ru.ip_fateev.lavka.Inventory;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Product implements Serializable {

    @PrimaryKey
    public long id;

    public String name;

    public Double price;

    public String barcode;

    public String getId() {
        return id + "";
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price + "";
    }

    public String getBarcode() {
        return barcode;
    }
}
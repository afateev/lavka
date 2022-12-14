package ru.ip_fateev.lavka.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Product (
    @PrimaryKey
    val id: Long,
    val name: String,
    val price: Double,
    val barcode: String,
) : java.io.Serializable
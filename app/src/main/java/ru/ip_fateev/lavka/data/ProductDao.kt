package ru.ip_fateev.lavka.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert
    suspend fun insert(product: Product)

    @Update
    suspend fun update(product: Product)

    @Query("SELECT * FROM product ORDER BY id ASC")
    fun getFlow(): Flow<List<Product>>

    @Query("SELECT * FROM product ORDER BY id ASC")
    suspend fun get(): List<Product>

    @Query("SELECT * FROM product WHERE id = :id")
    fun getFlow(id: Long): Flow<List<Product>>

    @Query("SELECT * FROM product WHERE id = :id")
    suspend fun get(id: Long): List<Product>
}
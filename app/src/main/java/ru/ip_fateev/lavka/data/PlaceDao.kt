package ru.ip_fateev.lavka.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaceDao {
    @Insert
    suspend fun insert(place: Place)

    @Query("SELECT * FROM place ORDER BY id ASC")
    fun getFlow(): Flow<List<Place>>

    @Query("SELECT * FROM place ORDER BY id ASC")
    suspend fun get(): List<Place>
}
package ru.ip_fateev.lavka.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PositionDao {
    @Insert
    suspend fun insert(position: Position)

    @Query("SELECT * FROM position WHERE docId = :docId ORDER BY number ASC")
    fun getFlow(docId: Long): Flow<List<Position>>

    @Query("SELECT * FROM position WHERE docId = :docId ORDER BY number ASC")
    suspend fun get(docId: Long): List<Position>
}
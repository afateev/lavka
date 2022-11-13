package ru.ip_fateev.lavka.documents

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PositionDao {
    @Insert
    suspend fun insert(position: Position)

    @Query("SELECT * FROM position WHERE docId = :docId ORDER BY number ASC")
    fun get(docId: Long): Flow<List<Position>>
}
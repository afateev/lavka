package ru.ip_fateev.lavka.documents

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Insert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM 'transaction' WHERE docId = :docId ORDER BY id ASC")
    fun get(docId: Long): Flow<List<Transaction>>
}
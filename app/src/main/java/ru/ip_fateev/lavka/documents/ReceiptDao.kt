package ru.ip_fateev.lavka.documents

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

//https://developer.android.com/codelabs/android-room-with-a-view-kotlin#5

@Dao
interface ReceiptDao {
    @Insert
    suspend fun insert(receipt: Receipt)

    @Query("SELECT * FROM receipt WHERE type = :type AND state = :state ORDER BY id ASC LIMIT 1")
    fun getOne(type: ReceiptType, state: ReceiptState): Flow<Receipt>

    @Query("SELECT * FROM receipt WHERE type = :type AND state != :state ORDER BY id ASC LIMIT 1")
    fun getActiveOne(type: ReceiptType, state: ReceiptState = ReceiptState.CLOSED): Flow<Receipt>

    @Query("SELECT * FROM receipt WHERE id = :id ORDER BY id DESC LIMIT 1")
    fun get(id: Long): Flow<Receipt>

    @Query("SELECT state FROM receipt WHERE id = :id ORDER BY id DESC LIMIT 1")
    fun getState(id: Long): Flow<ReceiptState>

    @Query("UPDATE receipt SET state = :state WHERE id = :id")
    suspend fun setSate(id: Long, state: ReceiptState)
}
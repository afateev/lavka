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
    fun getOneFlow(type: ReceiptType, state: ReceiptState): Flow<Receipt>

    @Query("SELECT * FROM receipt WHERE type = :type AND state not in (:states) ORDER BY id ASC LIMIT 1")
    fun getActiveOneFlow(type: ReceiptType, states: Array<ReceiptState> = arrayOf(ReceiptState.DELAYED, ReceiptState.CLOSED)): Flow<Receipt>

    @Query("SELECT * FROM receipt WHERE type = :type AND state not in (:states) ORDER BY id ASC LIMIT 1")
    fun getActiveOne(type: ReceiptType, states: Array<ReceiptState> = arrayOf(ReceiptState.DELAYED, ReceiptState.CLOSED)): Receipt?

    @Query("SELECT * FROM receipt WHERE id = :id ORDER BY id DESC LIMIT 1")
    fun getFlow(id: Long): Flow<Receipt>

    @Query("SELECT * FROM receipt WHERE id = :id ORDER BY id DESC LIMIT 1")
    suspend fun get(id: Long): Receipt

    @Query("SELECT state FROM receipt WHERE id = :id ORDER BY id DESC LIMIT 1")
    fun getState(id: Long): Flow<ReceiptState>

    @Query("UPDATE receipt SET state = :state WHERE id = :id")
    suspend fun setSate(id: Long, state: ReceiptState)
}
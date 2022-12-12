package ru.ip_fateev.lavka.data

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

    @Query("UPDATE receipt SET dateTime = :dateTime WHERE id = :id")
    suspend fun setDateTime(id: Long, dateTime: Long)

    @Query("UPDATE receipt SET place = :place WHERE id = :id")
    suspend fun setPlace(id: Long, place: Long)

    @Query("UPDATE receipt SET user = :user WHERE id = :id")
    suspend fun setUser(id: Long, user: Long)
}
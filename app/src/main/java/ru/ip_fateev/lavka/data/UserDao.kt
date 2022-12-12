package ru.ip_fateev.lavka.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Query("SELECT * FROM user ORDER BY id ASC")
    fun getFlow(): Flow<List<User>>

    @Query("SELECT * FROM user ORDER BY id ASC")
    suspend fun get(): List<User>
}
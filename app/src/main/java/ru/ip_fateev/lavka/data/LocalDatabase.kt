package ru.ip_fateev.lavka.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Receipt::class, Position::class, Transaction::class, Place::class], version = 1)
public abstract class LocalDatabase : RoomDatabase() {
    abstract fun receiptDao(): ReceiptDao
    abstract fun positionDao(): PositionDao
    abstract fun transactionDao(): TransactionDao
    abstract fun placeDao(): PlaceDao

    companion object {
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun instance(context: Context): LocalDatabase {
            return INSTANCE?: synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, LocalDatabase::class.java, "data").build()
                INSTANCE = instance
                instance
            }
        }
    }
}
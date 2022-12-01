package ru.ip_fateev.lavka.documents

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Transaction (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val docId: Long,
    val type: TransactionType,
    val amount: Double,
    val rrn: String,
)
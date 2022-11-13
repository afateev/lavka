package ru.ip_fateev.lavka.documents

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Position (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val docId: Long,
    val number: Int,
    val productId: Long,
    val productName: String,
)
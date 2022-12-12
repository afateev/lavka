package ru.ip_fateev.lavka.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//https://developer.android.com/codelabs/android-room-with-a-view-kotlin#4

@Entity
data class Receipt (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val uuid: UUID,
    val dateTime: Long,
    val type: ReceiptType,
    val state: ReceiptState,
    val place: Long,
    val user: Long,
)
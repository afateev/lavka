package ru.ip_fateev.lavka.documents

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

//https://developer.android.com/codelabs/android-room-with-a-view-kotlin#4

@Entity
data class Receipt (
    @PrimaryKey(autoGenerate = true) val id: Long,
    val uuid: UUID,
    val type: ReceiptType,
    val state: ReceiptState
        )
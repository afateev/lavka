package ru.ip_fateev.lavka.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User (
    @PrimaryKey
    val id: Long,
    val name: String,
)
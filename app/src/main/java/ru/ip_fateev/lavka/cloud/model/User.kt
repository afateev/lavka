package ru.ip_fateev.lavka.cloud.model

data class User (
    var id: Long? = null,
    var name: String? = null,
    val result: Boolean = false
)
package ru.ip_fateev.lavka.cloud.model

data class UserList (
    var user_list: List<User>? = null,
    val result: Boolean = false
)
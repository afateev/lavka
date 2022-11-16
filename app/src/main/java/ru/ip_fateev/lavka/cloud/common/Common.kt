package ru.ip_fateev.lavka.cloud.common

import ru.ip_fateev.lavka.cloud.Api
import ru.ip_fateev.lavka.cloud.RetrofitClient

object Common {
    private val BASE_URL = "https://ip-fateev.ru/api/"
    val api: Api
        get() = RetrofitClient.getClient(BASE_URL).create(Api::class.java)
}
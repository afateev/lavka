package ru.ip_fateev.lavka.cloud

import retrofit2.Call
import retrofit2.http.GET
import ru.ip_fateev.lavka.cloud.model.ProductList

interface Api {
    @GET("product/list")
    fun getProductList(): Call<ProductList>
}
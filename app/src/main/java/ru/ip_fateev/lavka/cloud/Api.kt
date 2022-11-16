package ru.ip_fateev.lavka.cloud

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import ru.ip_fateev.lavka.cloud.model.Product
import ru.ip_fateev.lavka.cloud.model.ProductList

interface Api {
    @GET("product/list")
    fun getProductList(): Call<ProductList>

    @GET("product/{product_id}")
    fun getProduct(@Path("product_id") id: Long): Call<Product>
}
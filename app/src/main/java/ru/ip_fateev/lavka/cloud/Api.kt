package ru.ip_fateev.lavka.cloud

import retrofit2.Call
import retrofit2.http.*
import ru.ip_fateev.lavka.cloud.model.Product
import ru.ip_fateev.lavka.cloud.model.ProductList
import ru.ip_fateev.lavka.cloud.model.Receipt
import ru.ip_fateev.lavka.cloud.model.UserList

interface Api {
    @GET("product/list")
    fun getProductList(): Call<ProductList>

    @GET("product/{product_id}")
    fun getProduct(@Path("product_id") id: Long): Call<Product>

    @GET("user/list")
    fun getUserList(): Call<UserList>

    @POST("lavka/docs/add")
    fun postReceipt(@Body receipt: Receipt): Call<Receipt>
}
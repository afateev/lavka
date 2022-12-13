package ru.ip_fateev.lavka.cloud.model

data class ProductList (
    var id_list: List<Long>? = null,
    var product_list: List<Product>? = null,
    val result: Boolean = false
)
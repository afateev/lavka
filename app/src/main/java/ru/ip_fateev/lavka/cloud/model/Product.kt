package ru.ip_fateev.lavka.cloud.model

data class Product (
    var product_id: Long? = null,
    var name: String? = null,
    var barcode: String? = null,
    var price: Double? = null,
    val result: Boolean = false
)
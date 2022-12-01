package ru.ip_fateev.lavka.cloud.model

import com.google.gson.annotations.SerializedName
import java.util.*

enum class ReceiptType(value: Int) {
    @SerializedName("none")
    NONE(0),
    @SerializedName("sell")
    SELL(1);
}

data class Position (
    var productId: Long? = null,
    var productName: String? = null,
    var price: Double? = null,
    var count: Long? = null,
)

data class Receipt (
    var id: UUID? = null,
    var type: ReceiptType? = null,
    var deviceUid: UUID? = null,
    var timestamp: Date? = null,
    var positions: List<Position>? = null,
    val result: Boolean? = null
    )
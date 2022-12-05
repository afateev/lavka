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
    var quantity: Long? = null,
)

enum class TransactionType(value: Int) {
    @SerializedName("none")
    NONE(0),
    @SerializedName("cash")
    CASH(1),
    @SerializedName("cashcange")
    CASHCHANGE(2),
    @SerializedName("card")
    CARD(3);
}

data class Transaction (
    val type: TransactionType,
    val amount: Double,
    val rrn: String,
)

data class Receipt (
    var id: UUID? = null,
    var type: ReceiptType? = null,
    var deviceUid: UUID? = null,
    var timestamp: Date? = null,
    var positions: List<Position>? = null,
    var transactions: List<Transaction>? = null,
    val result: Boolean? = null
    )
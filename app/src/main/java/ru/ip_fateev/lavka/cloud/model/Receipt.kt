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
    @SerializedName("cashchange")
    CASHCHANGE(2),
    @SerializedName("card")
    CARD(3);
}

data class Transaction (
    val type: TransactionType? = null,
    val amount: Double? = null,
    val rrn: String? = null,
)

data class OfdData (
    var t: Long? = null,    // время в формате ГГГГММДДTММСС
    var s: Double? = null,  // сумма
    var fn: Long? = null,   // номер фискального накопителя
    var i: Long? = null,    // номер фискального документа
    var fp: Long? = null,   // фискальный признак
    var n: Long? = null,    // операция 1 - приход, 2 - возврат прихода
)

data class Receipt (
    var uuid: UUID? = null,
    var type: ReceiptType? = null,
    var deviceUid: UUID? = null,
    var timestamp: String? = null,
    var positions: List<Position>? = null,
    var transactions: List<Transaction>? = null,
    var ofdData: OfdData? = null,
    val result: Boolean? = null
    )
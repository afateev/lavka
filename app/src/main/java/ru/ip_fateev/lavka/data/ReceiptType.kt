package ru.ip_fateev.lavka.data

enum class ReceiptType(value: Int) {
    NONE(0),
    SELL(1);

    companion object {
        private val VALUES = values()
        fun getByValue(value: Int): ReceiptType {
            var res = VALUES.firstOrNull { it.ordinal == value }
            if (res == null) {
                res = NONE
            }
            return res
        }
    }
}
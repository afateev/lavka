package ru.ip_fateev.lavka.data

enum class TransactionType(value: Int) {
    NONE(0),
    CASH(1),
    CASHCHANGE(2),
    CARD(3);

    companion object {
        private val VALUES = values()
        fun getByValue(value: Int): TransactionType {
            var res = VALUES.firstOrNull { it.ordinal == value }
            if (res == null) {
                res = NONE
            }
            return res
        }
    }
}
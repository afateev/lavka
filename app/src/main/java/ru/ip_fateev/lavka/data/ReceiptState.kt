package ru.ip_fateev.lavka.data

enum class ReceiptState(value: Int) {
    NEW(0),
    PAID(1),
    DELAYED(2),
    CLOSED(3);



    companion object {
        private val VALUES = ReceiptState.values()
        fun getByValue(value: Int): ReceiptState {
            var res = VALUES.firstOrNull { it.ordinal == value }
            if (res == null) {
                res = NEW
            }
            return res
        }
    }
}
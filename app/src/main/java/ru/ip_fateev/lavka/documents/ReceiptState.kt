package ru.ip_fateev.lavka.documents

enum class ReceiptState(value: Int) {
    NEW(0),
    PAYMENT(1),
    CLOSED(2);



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
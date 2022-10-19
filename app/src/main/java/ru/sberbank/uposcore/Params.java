package ru.sberbank.uposcore;

public enum Params {
    OPERATION(0),
    AMOUNT(1),
    AMOUNT_OTHER(2),
    REQUEST_ID(3),
    DEPARTMENT(4),
    CURRENCY(5),
    CARD_TYPE(6),
    TRACK2(7),
    RRN(8),
    PAYMENT_ATTRIBUTE_TAG(9),
    PAYMENT_ATTRIBUTE_VALUE(10),
    CONTROLLER_IP_ADDRESS(11),
    CONTROLLER_IP_PORT(12),
    FLAGS(13),
    EXTRA_DATA(14),
    CHEQUE_FLAGS(15),
    PIN_FLAGS(16),
    OWN_CARD_FLAGS(17),
    CASHIER_FIO(18),
    RECEIVED_CARD(19),
    EXPECTED_CARD(20),
    PASSPORT_DATA(21),
    SCREEN_TEXT(22),
    SCREEN_TEXT_MODE(23),
    BASKET_ID(24),
    JNI_PROCESS_CALL_ID(25);
    
    private int value;

    private Params(int i) {
        this.value = i;
    }

    public int toInt() {
        return this.value;
    }

    public static Params fromInt(int i) {
        Params[] values = values();
        for (int i2 = 0; i2 < values.length; i2++) {
            if (i == values[i2].value) {
                return values[i2];
            }
        }
        throw new IllegalArgumentException("Params value out of index!");
    }
}

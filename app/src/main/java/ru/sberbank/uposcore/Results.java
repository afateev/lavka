package ru.sberbank.uposcore;

public enum Results {
    ERROR(0),
    TID(1),
    MID(2),
    CHEQUE(3),
    PAN(4),
    RRN(5),
    AUTH_CODE(6),
    TSN(7),
    EXP_DATE(8),
    DATE(9),
    TIME(10),
    IS_OWN(11),
    CARD_ID(12),
    PIL_OP_TYPE(13),
    AMOUNT(14),
    AMOUNT_C(15),
    FLAGS(16),
    LLT_ID(17),
    REQUEST_ID(18),
    MESSAGE(19),
    CARD_NAME(20),
    HOLDENAME(21),
    LOYALTY_NUMBER(22),
    ENCRYPTED_DATA(23),
    HASH(24),
    HASH_ALGO(25),
    TOKEN_IS_OWN(26),
    FFI(27),
    AID(28),
    TLV_DATA(29),
    JNI_PROCESS_CALL_ID(30),
    HASH256(31);
    
    private int value;

    private Results(int i) {
        this.value = i;
    }

    public int toInt() {
        return this.value;
    }

    public static Results fromInt(int i) {
        Results[] values = values();
        for (int i2 = 0; i2 < values.length; i2++) {
            if (i == values[i2].value) {
                return values[i2];
            }
        }
        throw new IllegalArgumentException("Results value out of index!");
    }
}

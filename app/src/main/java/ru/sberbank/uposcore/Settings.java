package ru.sberbank.uposcore;

public enum Settings {
    CURRENT_DIR(0),
    EXTERN_DIR(1),
    CASH_REGISTER_NAME(2),
    CASH_REGISTER_VERSION(3),
    UPOS_CORE_VERSION(4),
    CASH_REGISTER_MODEL(5),
    CASH_REGISTER_SERIAL(6),
    FISKAL_SERIAL(7),
    FISKAL_MODEL(8),
    NATIVE_LIB_DIR(9),
    FISKAL_ACTIVATION_DATE(10);
    
    private int value;

    private Settings(int i) {
        this.value = i;
    }

    public int toInt() {
        return this.value;
    }

    public static Settings fromInt(int i) {
        Settings[] values = values();
        for (int i2 = 0; i2 < values.length; i2++) {
            if (i == values[i2].value) {
                return values[i2];
            }
        }
        throw new IllegalArgumentException("Settings value out of index!");
    }
}

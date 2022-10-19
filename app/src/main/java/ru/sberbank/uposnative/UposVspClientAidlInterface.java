package ru.sberbank.uposnative;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;
import ru.sberbank.uposnative.UposLoggerCallbackListener;
import ru.sberbank.uposnative.UposVspClientCallbackListener;

public interface UposVspClientAidlInterface extends IInterface {
    void addTestConfiguration() throws RemoteException;

    void cashout(String str, String str2) throws RemoteException;

    void doSomething(String str) throws RemoteException;

    void getApplicationLog(UposLoggerCallbackListener uposLoggerCallbackListener) throws RemoteException;

    String getConnectionSettings(int i) throws RemoteException;

    int getPid() throws RemoteException;

    void getTechnicalAccount() throws RemoteException;

    void getTid() throws RemoteException;

    void getTimeout() throws RemoteException;

    void getUposLog(UposLoggerCallbackListener uposLoggerCallbackListener) throws RemoteException;

    void killApp() throws RemoteException;

    void purchaseWithCashback(String str, String str2, String str3) throws RemoteException;

    void readCardNumber() throws RemoteException;

    void registerUposClientCallbackListener(UposVspClientCallbackListener uposVspClientCallbackListener) throws RemoteException;

    void setDefaultTerminal(boolean z) throws RemoteException;

    void setMasterCallData(byte[] bArr, String str) throws RemoteException;

    void setServiceInformation(String str, boolean z) throws RemoteException;

    void startCoreReconciliation(String str) throws RemoteException;

    void startCoreServiceMenu(String str) throws RemoteException;

    void startOperation(byte[] bArr) throws RemoteException;

    void startOperationWithAdditionalData(byte[] bArr, String str) throws RemoteException;

    void startPayment(String str, String str2) throws RemoteException;

    void startReconciliation() throws RemoteException;

    void startRefund(String str, String str2) throws RemoteException;

    void startReversal(String str, String str2) throws RemoteException;

    void startServiceMenu() throws RemoteException;

    void unregisterUposClientCallbackListener(UposVspClientCallbackListener uposVspClientCallbackListener) throws RemoteException;

    public static abstract class Stub extends Binder implements UposVspClientAidlInterface {
        private static final String DESCRIPTOR = "ru.sberbank.uposnative.UposVspClientAidlInterface";
        static final int TRANSACTION_addTestConfiguration = 11;
        static final int TRANSACTION_cashout = 5;
        static final int TRANSACTION_doSomething = 27;
        static final int TRANSACTION_getApplicationLog = 12;
        static final int TRANSACTION_getConnectionSettings = 24;
        static final int TRANSACTION_getPid = 16;
        static final int TRANSACTION_getTechnicalAccount = 25;
        static final int TRANSACTION_getTid = 18;
        static final int TRANSACTION_getTimeout = 26;
        static final int TRANSACTION_getUposLog = 13;
        static final int TRANSACTION_killApp = 17;
        static final int TRANSACTION_purchaseWithCashback = 6;
        static final int TRANSACTION_readCardNumber = 19;
        static final int TRANSACTION_registerUposClientCallbackListener = 8;
        static final int TRANSACTION_setDefaultTerminal = 7;
        static final int TRANSACTION_setMasterCallData = 22;
        static final int TRANSACTION_setServiceInformation = 23;
        static final int TRANSACTION_startCoreReconciliation = 20;
        static final int TRANSACTION_startCoreServiceMenu = 21;
        static final int TRANSACTION_startOperation = 14;
        static final int TRANSACTION_startOperationWithAdditionalData = 15;
        static final int TRANSACTION_startPayment = 1;
        static final int TRANSACTION_startReconciliation = 4;
        static final int TRANSACTION_startRefund = 2;
        static final int TRANSACTION_startReversal = 3;
        static final int TRANSACTION_startServiceMenu = 10;
        static final int TRANSACTION_unregisterUposClientCallbackListener = 9;

        public IBinder asBinder() {
            return this;
        }

        public Stub() {
            attachInterface(this, "ru.sberbank.uposnative.UposVspClientAidlInterface");
        }

        public static UposVspClientAidlInterface asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof UposVspClientAidlInterface)) {
                return new Proxy(iBinder);
            }
            return (UposVspClientAidlInterface) queryLocalInterface;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            if (i != 1598968902) {
                boolean z = false;
                switch (i) {
                    case 1:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        startPayment(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 2:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        startRefund(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 3:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        startReversal(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 4:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        startReconciliation();
                        parcel2.writeNoException();
                        return true;
                    case 5:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        cashout(parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 6:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        purchaseWithCashback(parcel.readString(), parcel.readString(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 7:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        setDefaultTerminal(z);
                        parcel2.writeNoException();
                        return true;
                    case 8:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        registerUposClientCallbackListener(UposVspClientCallbackListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 9:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        unregisterUposClientCallbackListener(UposVspClientCallbackListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 10:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        startServiceMenu();
                        parcel2.writeNoException();
                        return true;
                    case 11:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        addTestConfiguration();
                        parcel2.writeNoException();
                        return true;
                    case 12:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        getApplicationLog(UposLoggerCallbackListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 13:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        getUposLog(UposLoggerCallbackListener.Stub.asInterface(parcel.readStrongBinder()));
                        parcel2.writeNoException();
                        return true;
                    case 14:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        startOperation(parcel.createByteArray());
                        parcel2.writeNoException();
                        return true;
                    case 15:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        startOperationWithAdditionalData(parcel.createByteArray(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 16:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        int pid = getPid();
                        parcel2.writeNoException();
                        parcel2.writeInt(pid);
                        return true;
                    case 17:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        killApp();
                        parcel2.writeNoException();
                        return true;
                    case 18:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        getTid();
                        parcel2.writeNoException();
                        return true;
                    case 19:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        readCardNumber();
                        parcel2.writeNoException();
                        return true;
                    case 20:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        startCoreReconciliation(parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 21:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        startCoreServiceMenu(parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 22:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        setMasterCallData(parcel.createByteArray(), parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    case 23:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        String readString = parcel.readString();
                        if (parcel.readInt() != 0) {
                            z = true;
                        }
                        setServiceInformation(readString, z);
                        parcel2.writeNoException();
                        return true;
                    case 24:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        String connectionSettings = getConnectionSettings(parcel.readInt());
                        parcel2.writeNoException();
                        parcel2.writeString(connectionSettings);
                        return true;
                    case 25:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        getTechnicalAccount();
                        parcel2.writeNoException();
                        return true;
                    case 26:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        getTimeout();
                        parcel2.writeNoException();
                        return true;
                    case 27:
                        parcel.enforceInterface("ru.sberbank.uposnative.UposVspClientAidlInterface");
                        doSomething(parcel.readString());
                        parcel2.writeNoException();
                        return true;
                    default:
                        return super.onTransact(i, parcel, parcel2, i2);
                }
            } else {
                parcel2.writeString("ru.sberbank.uposnative.UposVspClientAidlInterface");
                return true;
            }
        }

        private static class Proxy implements UposVspClientAidlInterface {
            private IBinder mRemote;

            public String getInterfaceDescriptor() {
                return "ru.sberbank.uposnative.UposVspClientAidlInterface";
            }

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public void startPayment(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startRefund(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startReversal(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startReconciliation() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    this.mRemote.transact(4, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void cashout(String str, String str2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    this.mRemote.transact(5, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void purchaseWithCashback(String str, String str2, String str3) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeString(str);
                    obtain.writeString(str2);
                    obtain.writeString(str3);
                    this.mRemote.transact(6, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setDefaultTerminal(boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(7, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void registerUposClientCallbackListener(UposVspClientCallbackListener uposVspClientCallbackListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeStrongBinder(uposVspClientCallbackListener != null ? uposVspClientCallbackListener.asBinder() : null);
                    this.mRemote.transact(8, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void unregisterUposClientCallbackListener(UposVspClientCallbackListener uposVspClientCallbackListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeStrongBinder(uposVspClientCallbackListener != null ? uposVspClientCallbackListener.asBinder() : null);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startServiceMenu() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void addTestConfiguration() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    this.mRemote.transact(11, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getApplicationLog(UposLoggerCallbackListener uposLoggerCallbackListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeStrongBinder(uposLoggerCallbackListener != null ? uposLoggerCallbackListener.asBinder() : null);
                    this.mRemote.transact(12, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getUposLog(UposLoggerCallbackListener uposLoggerCallbackListener) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeStrongBinder(uposLoggerCallbackListener != null ? uposLoggerCallbackListener.asBinder() : null);
                    this.mRemote.transact(13, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startOperation(byte[] bArr) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeByteArray(bArr);
                    this.mRemote.transact(14, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startOperationWithAdditionalData(byte[] bArr, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str);
                    this.mRemote.transact(15, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public int getPid() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    this.mRemote.transact(16, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readInt();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void killApp() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    this.mRemote.transact(17, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getTid() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    this.mRemote.transact(18, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void readCardNumber() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    this.mRemote.transact(19, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startCoreReconciliation(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeString(str);
                    this.mRemote.transact(20, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void startCoreServiceMenu(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeString(str);
                    this.mRemote.transact(21, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setMasterCallData(byte[] bArr, String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeByteArray(bArr);
                    obtain.writeString(str);
                    this.mRemote.transact(22, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setServiceInformation(String str, boolean z) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeString(str);
                    obtain.writeInt(z ? 1 : 0);
                    this.mRemote.transact(23, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public String getConnectionSettings(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeInt(i);
                    this.mRemote.transact(24, obtain, obtain2, 0);
                    obtain2.readException();
                    return obtain2.readString();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getTechnicalAccount() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    this.mRemote.transact(25, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void getTimeout() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    this.mRemote.transact(26, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void doSomething(String str) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken("ru.sberbank.uposnative.UposVspClientAidlInterface");
                    obtain.writeString(str);
                    this.mRemote.transact(27, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}

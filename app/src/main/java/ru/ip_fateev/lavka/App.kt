package ru.ip_fateev.lavka

import android.app.Application
import ru.ip_fateev.lavka.DataSyncService.Companion.startService
import ru.ip_fateev.lavka.Inventory.LocalData
//import ru.sberbank.uposnative.bridge.BridgeListener
//import ru.sberbank.uposnative.bridge.impl.BridgeListenerImpl

class App : Application() {
    companion object {
        private var instance: App? = null

        fun getInstance(): App? {
            return instance
        }

        fun sendDeviceName(str:String){

        }
    }

    private var inventory: LocalData? = null
    //private val bridgeListener: BridgeListener = BridgeListenerImpl(this)

    /*fun getBridgeListener(): BridgeListener {
        return bridgeListener
    }

    fun setBridgeListener(bridgeListener2: BridgeListener) {
        //TODO
        //Intrinsics.checkParameterIsNotNull(bridgeListener2, "<set-?>")
        //bridgeListener = bridgeListener2
    }*/

    /*TODO
    override fun attachBaseContext(context: Context?) {
        Intrinsics.checkParameterIsNotNull(context, "base")
        super.attachBaseContext(context)
        MultiDex.install(this)
    }*/

    override fun onCreate() {
        super.onCreate()
        instance = this
        inventory = LocalData(applicationContext, "data_test1")
        //DataSyncService.startService(this, "DataSync Service running...")
        UposService.startService(this, "UPOS Service running...")
        //initFabric(this);
    }

    fun initFabric(application: Application?) {
        //TODO см AndroidApplication
    }

    fun getInventory(): LocalData? {
        return inventory
    }
}
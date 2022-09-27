package ru.ip_fateev.lavka

import android.app.Application
import ru.ip_fateev.lavka.Inventory.LocalData

class App : Application() {
    companion object {
        private var instance: App? = null

        fun getInstance(): App? {
            return instance
        }
    }

    private var inventory: LocalData? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        inventory = LocalData(applicationContext, "data_test1")
        DataSyncService.startService(this, "DataSync Service running...")
    }

    fun getInventory(): LocalData? {
        return inventory
    }
}
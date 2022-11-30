package ru.ip_fateev.lavka

import android.app.Application
import ru.ip_fateev.lavka.DataSyncService.Companion.startService
import ru.ip_fateev.lavka.Inventory.LocalData
import ru.ip_fateev.lavka.documents.LocalDatabase

class App : Application() {
    companion object {
        private var instance: App? = null

        fun getInstance(): App? {
            return instance
        }
    }

    private var inventory: LocalData? = null
    lateinit var database: LocalDatabase
    lateinit var localRepository: LocalRepository

    override fun onCreate() {
        super.onCreate()
        instance = this
        inventory = LocalData(applicationContext, "data_test1")
        database = LocalDatabase.instance(this)
        localRepository = LocalRepository(database.receiptDao(), database.positionDao(), database.transactionDao())
        //DataSyncService.startService(this, "DataSync Service running...")
        UposService.startService(this, "UPOS Service running...")
    }

    fun getInventory(): LocalData? {
        return inventory
    }

    fun getRepository(): LocalRepository {
        return localRepository
    }
}
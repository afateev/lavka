package ru.ip_fateev.lavka

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import ru.ip_fateev.lavka.Inventory.LocalData
import ru.ip_fateev.lavka.data.LocalDatabase
import ru.ip_fateev.lavka.data.Place
import ru.ip_fateev.lavka.data.User
import java.util.*

class App : Application() {
    companion object {
        val NOTIFICATION_CHANNEL_ID_STATE = "State notification channel"

        private var instance: App? = null

        fun getInstance(): App {
            return instance!!
        }

        fun getPlace(): MutableLiveData<Place?> {
            return instance?.place!!
        }

        fun getUser(): MutableLiveData<User?> {
            return instance?.user!!
        }

        fun getDevId(): UUID? {
            return getInstance().deviceUuidFactory.getDeviceUuid()
        }
    }

    lateinit var deviceUuidFactory: DeviceUuidFactory
    private var inventory: LocalData? = null
    lateinit var database: LocalDatabase
    lateinit var localRepository: LocalRepository
    var place = MutableLiveData<Place?>(null)
    var user = MutableLiveData<User?>(null)

    override fun onCreate() {
        super.onCreate()

        deviceUuidFactory = DeviceUuidFactory(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID_STATE, "Уведомления о состоянии",
                NotificationManager.IMPORTANCE_LOW)
            serviceChannel.setSound(null, null)
            serviceChannel.setShowBadge(false)
            val manager = ContextCompat.getSystemService(applicationContext, NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }

        instance = this
        inventory = LocalData(applicationContext, "data_test1")
        database = LocalDatabase.instance(this)
        localRepository = LocalRepository(
            database.receiptDao(),
            database.positionDao(),
            database.transactionDao(),
            database.placeDao(),
            database.userDao()
        )

        localRepository.getPlacesLive().observeForever {
            val p = findPlace("Администрация", it)
            if (p != null) {
                place.postValue(p)
            }
        }

        localRepository.getUsersLive().observeForever {
            val v = findUser(0, it)
            if (v != null) {
                user.postValue(v)
            }
        }

        initPlaces()

        DataSyncService.startService(this)
        UposService.startService(this, "UPOS Service running...")
    }

    fun getInventory(): LocalData? {
        return inventory
    }

    fun getRepository(): LocalRepository {
        return localRepository
    }

    private fun initPlaces() {
        MainScope().launch {
            val places = localRepository.getPlaces()
            if (findPlace("Администрация", places) == null) {
                localRepository.insertPlace(Place(id = 0, name = "Администрация"))
            }

            if (findPlace("Склад", places) == null) {
                localRepository.insertPlace(Place(id = 0, name = "Склад"))
            }

            if (findPlace("Шукшина", places) == null) {
                localRepository.insertPlace(Place(id = 0, name = "Шукшина"))
            }

            if (findPlace("Российская", places) == null) {
                localRepository.insertPlace(Place(id = 0, name = "Российская"))
            }
        }
    }

    private fun findPlace(name: String, places: List<Place>): Place? {
        places.forEach { p ->
            if (p.name == name) {
                return p
            }
        }
        return null
    }

    private fun findUser(id: Long, users: List<User>): User? {
        users.forEach { v ->
            if (v.id == id) {
                return v
            }
        }
        return null
    }
}
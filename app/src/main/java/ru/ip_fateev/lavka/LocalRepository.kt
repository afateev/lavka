package ru.ip_fateev.lavka

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import ru.ip_fateev.lavka.documents.*

class LocalRepository(
    private val receiptDao: ReceiptDao,
    private val positionDao: PositionDao
) {
    val sellReceipt: LiveData<Receipt> = receiptDao.get(ReceiptType.SELL).asLiveData()
    suspend fun insertReceipt(receipt: Receipt){ receiptDao.insert(receipt) }

    fun getPositions(docId: Long): LiveData<List<Position>> { return positionDao.get(docId).asLiveData() }
    suspend fun insertPosition(position: Position){ positionDao.insert(position) }
}
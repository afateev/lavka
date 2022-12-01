package ru.ip_fateev.lavka

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import ru.ip_fateev.lavka.documents.*

class LocalRepository(
    private val receiptDao: ReceiptDao,
    private val positionDao: PositionDao,
    private val transactionDao: TransactionDao
) {
    fun getActiveSellReceipt(): LiveData<Receipt> { return receiptDao.getActiveOne(ReceiptType.SELL).asLiveData() }
    fun getReceipt(id: Long): LiveData<Receipt> { return receiptDao.get(id).asLiveData() }
    suspend fun insertReceipt(receipt: Receipt){ receiptDao.insert(receipt) }

    fun getReceiptState(id: Long): LiveData<ReceiptState> { return receiptDao.getState(id).asLiveData() }
    suspend fun setReceiptState(id: Long, state: ReceiptState) { receiptDao.setSate(id, state) }

    fun getPositions(docId: Long): LiveData<List<Position>> { return positionDao.get(docId).asLiveData() }
    suspend fun insertPosition(position: Position){ positionDao.insert(position) }

    fun getTransactions(docId: Long): LiveData<List<Transaction>> { return transactionDao.get(docId).asLiveData() }
    suspend fun insertTransaction(transaction: Transaction) { transactionDao.insert(transaction) }
}
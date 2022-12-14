package ru.ip_fateev.lavka

import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import ru.ip_fateev.lavka.data.*

class LocalRepository(
    private val productDao: ProductDao,
    private val receiptDao: ReceiptDao,
    private val positionDao: PositionDao,
    private val transactionDao: TransactionDao,
    private val placeDao: PlaceDao,
    private val userDao: UserDao
) {
    fun getProductsLive(): LiveData<List<Product>> { return productDao.getFlow().asLiveData() }
    suspend fun getProducts(): List<Product> { return productDao.get() }
    suspend fun insertProduct(product: Product) { return productDao.insert(product) }
    suspend fun updateProduct(product: Product) { return productDao.update(product) }

    fun getActiveSellReceipt(): LiveData<Receipt> { return receiptDao.getActiveOneFlow(ReceiptType.SELL).asLiveData() }
    fun getReceiptLive(id: Long): LiveData<Receipt> { return receiptDao.getFlow(id).asLiveData() }
    fun getOneReceiptLive(type: ReceiptType, state: ReceiptState): LiveData<Receipt> { return receiptDao.getOneFlow(type, state).asLiveData()}
    suspend fun getReceipt(id: Long): Receipt { return receiptDao.get(id) }
    suspend fun insertReceipt(receipt: Receipt){ receiptDao.insert(receipt) }
    suspend fun setReceiptPlaceAndUser(id: Long, place: Long, user: Long) { receiptDao.setPlace(id, place); receiptDao.setUser(id, user) }

    fun getReceiptState(id: Long): LiveData<ReceiptState> { return receiptDao.getState(id).asLiveData() }
    suspend fun setReceiptState(id: Long, state: ReceiptState) { receiptDao.setSate(id, state) }
    suspend fun setReceiptDateTime(id: Long, dateTime: Long) { receiptDao.setDateTime(id, dateTime) }

    fun getPositionsLive(docId: Long): LiveData<List<Position>> { return positionDao.getFlow(docId).asLiveData() }
    suspend fun getPositions(docId: Long): List<Position> { return positionDao.get(docId) }
    suspend fun insertPosition(position: Position){ positionDao.insert(position) }

    fun getTransactionsLive(docId: Long): LiveData<List<Transaction>> { return transactionDao.getFlow(docId).asLiveData() }
    suspend fun getTransactions(docId: Long): List<Transaction> { return transactionDao.get(docId) }
    suspend fun insertTransaction(transaction: Transaction) { transactionDao.insert(transaction) }

    fun getPlacesLive(): LiveData<List<Place>> { return placeDao.getFlow().asLiveData() }
    suspend fun getPlaces(): List<Place> { return placeDao.get() }
    suspend fun insertPlace(place: Place) { placeDao.insert(place) }

    fun getUsersLive(): LiveData<List<User>> { return userDao.getFlow().asLiveData() }
    suspend fun getUsers(): List<User> { return userDao.get() }
    suspend fun insertUser(user: User) { userDao.insert(user) }
    suspend fun updateUser(user: User) { userDao.update(user) }
}
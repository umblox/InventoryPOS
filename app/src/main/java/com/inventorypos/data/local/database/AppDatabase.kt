package com.inventorypos.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.inventorypos.data.local.dao.*
import com.inventorypos.data.local.entity.*

@Database(
    entities = [
        ProductEntity::class,
        CategoryEntity::class,
        UserEntity::class,
        TransactionEntity::class,
        TransactionItemEntity::class,
        CustomerEntity::class,
        StockLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
    abstract fun categoryDao(): CategoryDao
    abstract fun userDao(): UserDao
    abstract fun transactionDao(): TransactionDao
    abstract fun customerDao(): CustomerDao
    abstract fun stockDao(): StockDao
}

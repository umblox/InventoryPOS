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
        SupplierEntity::class,
        TransactionEntity::class,
        TransactionItemEntity::class,
        CustomerEntity::class,
        StockLogEntity::class,
        ProductSupplierEntity::class,
        UserPermissionEntity::class
    ],
    version = 2,
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
    abstract fun supplierDao(): SupplierDao
    abstract fun productSupplierDao(): ProductSupplierDao
    abstract fun userPermissionDao(): UserPermissionDao
}

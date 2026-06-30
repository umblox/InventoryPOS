package com.inventorypos.data.local.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        var dbInstance: AppDatabase? = null
        
        dbInstance = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "inventory_pos.db"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    CoroutineScope(Dispatchers.IO).launch {
                        dbInstance?.userDao()?.insert(
                            com.inventorypos.data.local.entity.UserEntity(
                                username = "administrator",
                                passwordHash = "123456", 
                                fullName = "Administrator (Owner)",
                                role = com.inventorypos.data.local.entity.UserRole.SUPER_ADMIN,
                                isActive = true,
                                createdAt = java.util.Date()
                            )
                        )
                    }
                }
            })
            .build()
            
        return dbInstance
    }

    // ===== DAO PROVIDERS =====
    
    @Provides
    fun provideUserPermissionDao(db: AppDatabase) = db.userPermissionDao()
    
    @Provides
    fun provideProductDao(db: AppDatabase) = db.productDao()
    
    @Provides
    fun provideCategoryDao(db: AppDatabase) = db.categoryDao()
    
    @Provides
    fun provideUserDao(db: AppDatabase) = db.userDao()
    
    @Provides
    fun provideTransactionDao(db: AppDatabase) = db.transactionDao()
    
    @Provides
    fun provideCustomerDao(db: AppDatabase) = db.customerDao()
    
    @Provides
    fun provideStockDao(db: AppDatabase) = db.stockDao()
    
    // ➕ TAMBAHAN: SupplierDao (dibutuhkan AppModule)
    @Provides
    fun provideSupplierDao(db: AppDatabase) = db.supplierDao()
    
    // ➕ TAMBAHAN: ProductSupplierDao (dibutuhkan Smart PO)
    @Provides
    fun provideProductSupplierDao(db: AppDatabase) = db.productSupplierDao()
}

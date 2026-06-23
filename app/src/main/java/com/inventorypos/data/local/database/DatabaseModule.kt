package com.inventorypos.data.local.database

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "inventory_pos.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    
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
}

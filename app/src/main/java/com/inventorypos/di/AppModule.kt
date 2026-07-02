package com.inventorypos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import com.inventorypos.data.local.dao.*
import com.inventorypos.data.local.database.AppDatabase
import com.inventorypos.data.preferences.AuthPreferences
import com.inventorypos.domain.repository.*
import com.inventorypos.data.repository.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSupplierRepository(
        supplierDao: SupplierDao,
        productSupplierDao: ProductSupplierDao
    ): SupplierRepository {
        return SupplierRepositoryImpl(supplierDao, productSupplierDao)
    }

    @Provides
    @Singleton
    fun provideProductRepository(productDao: ProductDao): ProductRepository {
        return ProductRepositoryImpl(productDao)
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        userDao: UserDao,
        authPreferences: AuthPreferences
    ): AuthRepository {
        return AuthRepositoryImpl(userDao, authPreferences)
    }

    @Provides
    @Singleton
    fun provideTransactionRepository(
        transactionDao: TransactionDao,
        productDao: ProductDao,
        stockDao: StockDao
    ): TransactionRepository {
        return TransactionRepositoryImpl(transactionDao, productDao, stockDao)
    }

    @Provides
    @Singleton
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

    @Provides
    @Singleton
    fun provideCustomerRepository(customerDao: CustomerDao): CustomerRepository {
        return CustomerRepositoryImpl(customerDao)
    }

    @Provides
    @Singleton
    fun provideStockRepository(stockDao: StockDao): StockRepository {
        return StockRepositoryImpl(stockDao)
    }

    // ➕ TAMBAHAN: PurchaseOrderRepository untuk fitur Smart PO & Receiving
    @Provides
    @Singleton
    fun providePurchaseOrderRepository(poDao: PurchaseOrderDao): PurchaseOrderRepository {
        return PurchaseOrderRepositoryImpl(poDao)
    }
}

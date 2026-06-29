package com.inventorypos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// IMPORT DAO
import com.inventorypos.data.local.dao.CategoryDao
import com.inventorypos.data.local.dao.ProductDao
import com.inventorypos.data.local.dao.UserDao
import com.inventorypos.data.local.dao.ProductSupplierDao

// IMPORT LAINNYA
import com.inventorypos.data.preferences.AuthPreferences

// IMPORT DOMAIN (Interface / Aturan)
import com.inventorypos.domain.repository.CategoryRepository
import com.inventorypos.domain.repository.ProductRepository
import com.inventorypos.domain.repository.AuthRepository

// IMPORT DATA (Implementasi / Logika Database)
import com.inventorypos.data.repository.CategoryRepositoryImpl 
import com.inventorypos.data.repository.ProductRepositoryImpl
import com.inventorypos.data.repository.AuthRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
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
    fun provideProductSupplierDao(database: AppDatabase): ProductSupplierDao {
        return database.productSupplierDao()
    }
}

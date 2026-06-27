package com.inventorypos.di

import com.inventorypos.data.local.dao.CategoryDao
import com.inventorypos.data.local.dao.ProductDao
import com.inventorypos.domain.repository.CategoryRepository
import com.inventorypos.data.repository.CategoryRepositoryImpl 
import com.inventorypos.domain.repository.ProductRepository

// PERBAIKAN: Kita kembalikan ini ke domain karena file aslinya terdaftar di sana
import com.inventorypos.domain.repository.ProductRepositoryImpl

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

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
        userDao: com.inventorypos.data.local.dao.UserDao,
        authPreferences: com.inventorypos.data.preferences.AuthPreferences
    ): com.inventorypos.domain.repository.AuthRepository {
        return com.inventorypos.data.repository.AuthRepositoryImpl(userDao, authPreferences)
    }
}

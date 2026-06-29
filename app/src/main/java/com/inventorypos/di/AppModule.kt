package com.inventorypos.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// IMPORT DAO
import com.inventorypos.data.local.dao.*
// IMPORT DATABASE
import com.inventorypos.data.local.database.AppDatabase

// IMPORT LAINNYA
import com.inventorypos.data.preferences.AuthPreferences

// IMPORT DOMAIN (Interface)
import com.inventorypos.domain.repository.*

// IMPORT DATA (Implementasi)
import com.inventorypos.data.repository.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    // 1. PROVIDE DAOs (Dibutuhkan untuk membuat Repository)
    @Provides
    @Singleton
    fun provideProductSupplierDao(database: AppDatabase): ProductSupplierDao {
        return database.productSupplierDao()
    }

    @Provides
    @Singleton
    fun provideSupplierDao(database: AppDatabase): SupplierDao {
        return database.supplierDao()
    }

    // 2. PROVIDE REPOSITORIES
    @Provides
    @Singleton
    fun provideSupplierRepository(supplierDao: SupplierDao): SupplierRepository {
        return SupplierRepositoryImpl(supplierDao)
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
}

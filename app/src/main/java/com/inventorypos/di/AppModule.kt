package com.inventorypos.di

import com.inventorypos.data.local.dao.CategoryDao
import com.inventorypos.data.local.dao.ProductDao
import com.inventorypos.domain.repository.CategoryRepository
// Perhatikan import Impl sekarang mengambil dari folder data
import com.inventorypos.data.repository.CategoryRepositoryImpl 
import com.inventorypos.domain.repository.ProductRepository
import com.inventorypos.data.repository.ProductRepositoryImpl
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
}

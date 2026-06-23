package com.inventorypos.di

import com.inventorypos.data.local.dao.ProductDao
import com.inventorypos.domain.repository.ProductRepository
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
    fun provideProductRepository(
        productDao: ProductDao
    ): ProductRepository {
        return ProductRepositoryImpl(productDao)
    }
}

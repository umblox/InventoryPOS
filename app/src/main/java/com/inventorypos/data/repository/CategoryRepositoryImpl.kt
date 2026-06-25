package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.CategoryDao
import com.inventorypos.domain.model.Category
import com.inventorypos.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class CategoryRepositoryImpl(
    private val categoryDao: CategoryDao
) : CategoryRepository {
    override fun getAllCategories(): Flow<List<Category>> {
        // Untuk sementara pakai emptyFlow, nanti diganti dengan panggil ke categoryDao
        return emptyFlow() 
    }
}

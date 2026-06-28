package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.CategoryDao
import com.inventorypos.data.local.entity.CategoryEntity
import com.inventorypos.domain.model.Category
import com.inventorypos.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getAllCategories(): Flow<List<Category>> {
        // Menarik data asli dari DAO dan memetakannya ke Domain Model
        return categoryDao.getAllActive().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getById(id)?.toDomain()
    }

    override suspend fun insertCategory(category: Category): Long {
        return categoryDao.insert(category.toEntity())
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.update(category.toEntity())
    }

    override suspend fun deleteCategory(id: Long) {
        categoryDao.softDelete(id)
    }
}

// === FUNGSI MAPPER EXTENSION ===
// Mengubah dari Format Database (Entity) ke Format Layar (Domain Model)
fun CategoryEntity.toDomain(): Category {
    return Category(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        icon = this.icon,
        parentId = this.parentId,
        sortOrder = this.sortOrder,
        isActive = this.isActive
    )
}

// Mengubah dari Format Layar (Domain Model) kembali ke Format Database (Entity)
fun Category.toEntity(): CategoryEntity {
    return CategoryEntity(
        id = this.id,
        name = this.name,
        description = this.description,
        color = this.color,
        icon = this.icon,
        parentId = this.parentId,
        sortOrder = this.sortOrder,
        isActive = this.isActive
    )
}

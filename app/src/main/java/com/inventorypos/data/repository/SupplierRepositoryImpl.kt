package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.SupplierDao
import com.inventorypos.data.local.entity.SupplierEntity
import com.inventorypos.domain.model.Supplier
import com.inventorypos.domain.repository.SupplierRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class SupplierRepositoryImpl @Inject constructor(
    private val supplierDao: SupplierDao
) : SupplierRepository {
    
    override fun getAllSuppliers(): Flow<List<Supplier>> = 
        supplierDao.getAllActive().map { list -> list.map { it.toDomain() } }
    
    override suspend fun getSupplierById(id: Long): Supplier? = 
        supplierDao.getById(id)?.toDomain()
    
    override suspend fun addSupplier(supplier: Supplier): Long = 
        supplierDao.insert(supplier.toEntity())
    
    override suspend fun updateSupplier(supplier: Supplier) {
        supplierDao.update(supplier.toEntity().copy(updatedAt = Date()))
    }
    
    override suspend fun deleteSupplier(id: Long) {
        supplierDao.softDelete(id, Date())
    }

    private fun SupplierEntity.toDomain() = Supplier(id, name, phone, email, address, isActive)
    private fun Supplier.toEntity() = SupplierEntity(id, name, phone, email, address, isActive)
}


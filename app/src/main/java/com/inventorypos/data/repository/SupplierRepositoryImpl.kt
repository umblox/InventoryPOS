package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.ProductSupplierDao
import com.inventorypos.data.local.dao.SupplierDao
import com.inventorypos.data.local.entity.ProductSupplierEntity
import com.inventorypos.data.local.entity.SupplierEntity
import com.inventorypos.domain.model.Supplier
import com.inventorypos.domain.model.SupplierOffer
import com.inventorypos.domain.repository.SupplierRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Date
import javax.inject.Inject

class SupplierRepositoryImpl @Inject constructor(
    private val supplierDao: SupplierDao,
    private val productSupplierDao: ProductSupplierDao
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

    // ➕ TAMBAHAN: Product-Supplier relation

    override suspend fun getSuppliersForProduct(productId: Long): List<SupplierOffer> {
        return productSupplierDao.getSuppliersForProduct(productId).map {
            SupplierOffer(
                supplierId = it.supplier.id,
                supplierName = it.supplier.name,
                buyPrice = it.buyPrice,
                isPrimary = it.isPrimary
            )
        }
    }

    override suspend fun addSupplierToProduct(
        productId: Long,
        supplierId: Long,
        buyPrice: Double,
        isPrimary: Boolean
    ) {
        if (isPrimary) {
            productSupplierDao.clearPrimarySupplier(productId)
        }
        productSupplierDao.insertProductSupplier(
            ProductSupplierEntity(
                productId = productId,
                supplierId = supplierId,
                buyPrice = buyPrice,
                isPrimary = isPrimary,
                lastUpdated = Date()
            )
        )
    }

    override suspend fun removeSupplierFromProduct(productId: Long, supplierId: Long) {
        productSupplierDao.deleteProductSupplier(productId, supplierId)
    }

    override suspend fun setPrimarySupplier(productId: Long, supplierId: Long) {
        productSupplierDao.updatePrimarySupplier(productId, supplierId)
    }

    private fun SupplierEntity.toDomain() = Supplier(id, name, phone, email, address, isActive)
    private fun Supplier.toEntity() = SupplierEntity(id, name, phone, email, address, isActive)
}

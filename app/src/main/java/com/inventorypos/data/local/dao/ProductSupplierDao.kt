package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.ProductSupplierEntity
import com.inventorypos.data.local.entity.SupplierWithPrice

@Dao
interface ProductSupplierDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductSupplier(relation: ProductSupplierEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductSuppliers(relations: List<ProductSupplierEntity>)

    @Query("""
        SELECT s.*, ps.buyPrice, ps.isPrimary 
        FROM suppliers s 
        INNER JOIN product_suppliers ps ON s.id = ps.supplierId 
        WHERE ps.productId = :productId AND s.isActive = 1
    """)
    suspend fun getSuppliersForProduct(productId: Long): List<SupplierWithPrice>

    @Query("""
        SELECT s.*, ps.buyPrice, ps.isPrimary 
        FROM suppliers s 
        INNER JOIN product_suppliers ps ON s.id = ps.supplierId 
        WHERE ps.productId = :productId AND s.isActive = 1
    """)
    fun getSuppliersForProductFlow(productId: Long): kotlinx.coroutines.flow.Flow<List<SupplierWithPrice>>

    @Query("DELETE FROM product_suppliers WHERE productId = :productId AND supplierId = :supplierId")
    suspend fun deleteProductSupplier(productId: Long, supplierId: Long)

    @Query("UPDATE product_suppliers SET isPrimary = 0 WHERE productId = :productId")
    suspend fun clearPrimarySupplier(productId: Long)

    @Query("UPDATE product_suppliers SET isPrimary = 1 WHERE productId = :productId AND supplierId = :supplierId")
    suspend fun setPrimarySupplier(productId: Long, supplierId: Long)

    @Query("SELECT * FROM product_suppliers WHERE productId = :productId")
    suspend fun getAllForProduct(productId: Long): List<ProductSupplierEntity>

    @Transaction
    suspend fun updatePrimarySupplier(productId: Long, supplierId: Long) {
        clearPrimarySupplier(productId)
        setPrimarySupplier(productId, supplierId)
    }
}

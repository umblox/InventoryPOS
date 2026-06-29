package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.ProductSupplierEntity
import com.inventorypos.data.local.entity.SupplierWithPrice

@Dao
interface ProductSupplierDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductSupplier(relation: ProductSupplierEntity)

    // Mengambil semua supplier yang menyuplai produk X beserta harganya
    @Query("""
        SELECT s.*, ps.buyPrice, ps.isPrimary 
        FROM suppliers s 
        INNER JOIN product_suppliers ps ON s.id = ps.supplierId 
        WHERE ps.productId = :productId AND s.isActive = 1
    """)
    suspend fun getSuppliersForProduct(productId: Long): List<SupplierWithPrice>
}


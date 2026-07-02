package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.PurchaseOrderEntity
import com.inventorypos.data.local.entity.PurchaseOrderItemEntity
import com.inventorypos.data.local.entity.PurchaseOrderWithItems // <--- TAMBAHAN IMPORT INI
import kotlinx.coroutines.flow.Flow

@Dao
interface PurchaseOrderDao {
    @Insert
    suspend fun insertPurchaseOrder(po: PurchaseOrderEntity): Long

    @Insert
    suspend fun insertPurchaseOrderItems(items: List<PurchaseOrderItemEntity>)

    @Transaction
    suspend fun createPurchaseOrderWithItems(po: PurchaseOrderEntity, items: List<PurchaseOrderItemEntity>) {
        val poId = insertPurchaseOrder(po)
        val itemsWithPoId = items.map { it.copy(poId = poId) }
        insertPurchaseOrderItems(itemsWithPoId)
    }

    @Query("SELECT * FROM purchase_orders ORDER BY createdAt DESC")
    fun getAllPurchaseOrders(): Flow<List<PurchaseOrderEntity>>

    @Transaction // <--- TAMBAHAN: Wajib menggunakan @Transaction saat mengembalikan relasi seperti PurchaseOrderWithItems
    @Query("SELECT * FROM purchase_orders WHERE id = :id")
    suspend fun getPurchaseOrderById(id: Long): PurchaseOrderWithItems?

    @Update
    suspend fun updatePurchaseOrder(po: PurchaseOrderEntity)

    @Update
    suspend fun updatePurchaseOrderItems(items: List<PurchaseOrderItemEntity>)
}

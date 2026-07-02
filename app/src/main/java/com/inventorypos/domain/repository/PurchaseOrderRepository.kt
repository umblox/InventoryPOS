package com.inventorypos.domain.repository

import com.inventorypos.data.local.entity.PurchaseOrderEntity
import com.inventorypos.data.local.entity.PurchaseOrderItemEntity
import com.inventorypos.data.local.entity.PurchaseOrderWithItems
import kotlinx.coroutines.flow.Flow

interface PurchaseOrderRepository {
    suspend fun createPurchaseOrders(orders: Map<PurchaseOrderEntity, List<PurchaseOrderItemEntity>>)
    fun getAllPurchaseOrders(): Flow<List<PurchaseOrderEntity>>
    
    // --- FUNGSI BARU UNTUK RECEIVING ---
    suspend fun getPurchaseOrderById(id: Long): PurchaseOrderWithItems?
    suspend fun receivePurchaseOrder(poId: Long, receivingBatch: Map<Long, Int>)
}

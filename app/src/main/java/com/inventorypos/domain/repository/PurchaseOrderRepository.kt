package com.inventorypos.domain.repository

import com.inventorypos.data.local.entity.PurchaseOrderEntity
import com.inventorypos.data.local.entity.PurchaseOrderItemEntity
import kotlinx.coroutines.flow.Flow

interface PurchaseOrderRepository {
    suspend fun createPurchaseOrders(orders: Map<PurchaseOrderEntity, List<PurchaseOrderItemEntity>>)
    fun getAllPurchaseOrders(): Flow<List<PurchaseOrderEntity>>
}


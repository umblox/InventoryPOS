package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.PurchaseOrderDao
import com.inventorypos.data.local.entity.PurchaseOrderEntity
import com.inventorypos.data.local.entity.PurchaseOrderItemEntity
import com.inventorypos.domain.repository.PurchaseOrderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PurchaseOrderRepositoryImpl @Inject constructor(
    private val poDao: PurchaseOrderDao
) : PurchaseOrderRepository {
    
    override suspend fun createPurchaseOrders(orders: Map<PurchaseOrderEntity, List<PurchaseOrderItemEntity>>) {
        // Menyimpan semua nota PO yang sudah dikelompokkan
        orders.forEach { (po, items) ->
            poDao.createPurchaseOrderWithItems(po, items)
        }
    }

    override fun getAllPurchaseOrders(): Flow<List<PurchaseOrderEntity>> {
        return poDao.getAllPurchaseOrders()
    }
}


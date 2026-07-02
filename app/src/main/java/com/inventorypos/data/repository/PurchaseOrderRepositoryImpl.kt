package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.ProductDao
import com.inventorypos.data.local.dao.PurchaseOrderDao
import com.inventorypos.data.local.dao.StockDao
import com.inventorypos.data.local.entity.*
import com.inventorypos.domain.repository.PurchaseOrderRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date
import javax.inject.Inject

class PurchaseOrderRepositoryImpl @Inject constructor(
    private val poDao: PurchaseOrderDao,
    private val productDao: ProductDao, // <--- Injeksi Product
    private val stockDao: StockDao      // <--- Injeksi Stock
) : PurchaseOrderRepository {
    
    override suspend fun createPurchaseOrders(orders: Map<PurchaseOrderEntity, List<PurchaseOrderItemEntity>>) {
        orders.forEach { (po, items) -> poDao.createPurchaseOrderWithItems(po, items) }
    }

    override fun getAllPurchaseOrders(): Flow<List<PurchaseOrderEntity>> = poDao.getAllPurchaseOrders()

    override suspend fun getPurchaseOrderById(id: Long): PurchaseOrderWithItems? {
        return poDao.getPurchaseOrderById(id)
    }

    // FUNGSI SUPER POWER: Menerima barang, update PO, dan otomatis Stock In!
    override suspend fun receivePurchaseOrder(poId: Long, receivingBatch: Map<Long, Int>) {
        val poWithItems = poDao.getPurchaseOrderById(poId) ?: return
        val updatedItems = mutableListOf<PurchaseOrderItemEntity>()
        
        // Looping barang yang baru saja datang dari truk kurir
        receivingBatch.forEach { (itemId, qtyArrivedNow) ->
            if (qtyArrivedNow > 0) {
                val item = poWithItems.items.find { it.id == itemId } ?: return@forEach
                val newTotalReceived = item.quantityReceived + qtyArrivedNow // Ditambah, agar mendukung partial receive
                
                updatedItems.add(item.copy(quantityReceived = newTotalReceived))
                
                // 1. UPDATE STOK PRODUK
                val product = productDao.getById(item.productId)
                if (product != null) {
                    val newStock = product.stock + qtyArrivedNow
                    productDao.update(product.copy(stock = newStock, updatedAt = Date()))
                    
                    // 2. CATAT KE BUKU STOK (LOG MUTASI)
                    stockDao.insertLog(
                        StockLogEntity(
                            productId = product.id,
                            type = StockLogType.IN, 
                            quantity = qtyArrivedNow,
                            previousStock = product.stock,
                            newStock = newStock,
                            reference = "TERIMA PO: ${poWithItems.purchaseOrder.poNumber}",
                            notes = "Penerimaan barang dari supplier",
                            userId = 0L, // Idealnya ambil ID Kasir yang login
                            createdAt = Date()
                        )
                    )
                }
            }
        }
        
        if (updatedItems.isNotEmpty()) {
            poDao.updatePurchaseOrderItems(updatedItems)
            
            // 3. KALKULASI ULANG STATUS PO (COMPLETED / PARTIAL)
            val refreshedPo = poDao.getPurchaseOrderById(poId)!!
            val isFullyReceived = refreshedPo.items.all { it.quantityReceived >= it.quantityOrdered }
            val isPartiallyReceived = refreshedPo.items.any { it.quantityReceived > 0 }
            
            val newStatus = when {
                isFullyReceived -> PoStatus.COMPLETED
                isPartiallyReceived -> PoStatus.PARTIAL
                else -> refreshedPo.purchaseOrder.status
            }
            
            poDao.updatePurchaseOrder(refreshedPo.purchaseOrder.copy(status = newStatus))
        }
    }
}

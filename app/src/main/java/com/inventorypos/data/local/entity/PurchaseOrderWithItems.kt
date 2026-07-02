package com.inventorypos.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class PurchaseOrderWithItems(
    @Embedded val purchaseOrder: PurchaseOrderEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "poId"
    )
    val items: List<PurchaseOrderItemEntity>
)


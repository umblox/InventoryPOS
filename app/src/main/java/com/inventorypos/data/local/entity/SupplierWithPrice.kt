package com.inventorypos.data.local.entity

import androidx.room.Embedded

// Ini bukan tabel, 
//tapi wadah untuk menampung hasil JOIN
data class SupplierWithPrice(
    @Embedded val supplier: SupplierEntity,
    val buyPrice: Double,
    val isPrimary: Boolean
)


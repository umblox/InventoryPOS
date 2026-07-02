package com.inventorypos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "purchase_orders")
data class PurchaseOrderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val poNumber: String, // Contoh: PO-20260702-001
    val supplierId: Long,
    val supplierName: String, // Disimpan langsung agar nama supplier di nota tidak berubah meski profil supplier di-edit
    val status: PoStatus = PoStatus.PENDING,
    val totalAmount: Double,
    val notes: String? = null,
    val createdAt: Date = Date(),
    val expectedDate: Date? = null
)

enum class PoStatus {
    PENDING,    // Menunggu barang datang
    PARTIAL,    // Barang datang tapi kurang/belum semua
    COMPLETED,  // Barang datang lengkap (Masuk ke Stock In)
    CANCELLED   // PO dibatalkan
}


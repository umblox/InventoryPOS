package com.inventorypos.domain.model

data class SupplierOffer(
    val supplierId: Long,
    val supplierName: String,
    val buyPrice: Double,
    val isPrimary: Boolean
)

data class SmartPoItem(
    val product: Product,
    val primarySupplier: SupplierOffer?,
    val cheapestSupplier: SupplierOffer?,
    val recommendedSupplier: SupplierOffer?, // Kesimpulan sistem
    val potentialSavings: Double // Selisih harga jika pindah ke termurah
)


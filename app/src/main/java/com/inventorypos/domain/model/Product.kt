package com.inventorypos.domain.model

data class Product(
    val id: Long = 0,
    val name: String,
    val sku: String,
    val barcode: String? = null,
    val categoryId: Long? = null,
    val categoryName: String? = null,
    val description: String? = null,
    val buyPrice: Double,
    val sellPrice: Double,
    val wholesalePrice: Double? = null,
    val stock: Int = 0,
    val minStock: Int = 5,
    val unit: String = "pcs",
    val imageUrl: String? = null,
    val isActive: Boolean = true
)


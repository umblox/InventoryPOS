package com.inventorypos.data.local.entity

import androidx.room.Embedded

data class ProductWithCategory(
    @Embedded 
    val product: ProductEntity,
    
    // Ini akan menampung hasil dari JOIN (categories.name AS categoryName)
    val categoryName: String? 
)

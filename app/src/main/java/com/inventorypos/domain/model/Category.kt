package com.inventorypos.domain.model

data class Category(
    val id: Long = 0,
    val name: String,
    val description: String? = null,
    val color: String? = null,
    val icon: String? = null,
    val parentId: Long? = null,
    val sortOrder: Int = 0,
    val isActive: Boolean = true
)

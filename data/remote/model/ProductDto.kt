package com.inventorypos.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProductDto(
    @SerializedName("id")
    val id: Long = 0,

    @SerializedName("name")
    val name: String,

    @SerializedName("sku")
    val sku: String,

    @SerializedName("barcode")
    val barcode: String? = null,

    @SerializedName("category_id")
    val categoryId: Long? = null,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("buy_price")
    val buyPrice: Double,

    @SerializedName("sell_price")
    val sellPrice: Double,

    @SerializedName("wholesale_price")
    val wholesalePrice: Double? = null,

    @SerializedName("stock")
    val stock: Int = 0,

    @SerializedName("min_stock")
    val minStock: Int = 5,

    @SerializedName("unit")
    val unit: String = "pcs",

    @SerializedName("image_url")
    val imageUrl: String? = null,

    @SerializedName("is_active")
    val isActive: Boolean = true
)

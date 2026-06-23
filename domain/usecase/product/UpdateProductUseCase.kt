package com.inventorypos.domain.usecase.product

import com.inventorypos.domain.model.Product
import com.inventorypos.domain.repository.ProductRepository
import javax.inject.Inject

class UpdateProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(product: Product) {
        repository.updateProduct(product)
    }
}

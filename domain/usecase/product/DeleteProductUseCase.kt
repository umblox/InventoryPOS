package com.inventorypos.domain.usecase.product

import com.inventorypos.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteProduct(id)
    }
}

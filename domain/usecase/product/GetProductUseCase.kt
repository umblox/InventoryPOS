package com.inventorypos.domain.usecase.product

import com.inventorypos.domain.model.Product
import com.inventorypos.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> = repository.getAllProducts()
}

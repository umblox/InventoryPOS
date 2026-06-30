package com.inventorypos.domain.usecase.product

import com.inventorypos.domain.model.SmartPoItem
import com.inventorypos.domain.model.SupplierOffer
import com.inventorypos.domain.repository.ProductRepository
import com.inventorypos.domain.repository.SupplierRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSmartPoListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val supplierRepository: SupplierRepository
) {
    operator fun invoke(): Flow<List<SmartPoItem>> {
        return productRepository.getLowStockProducts().map { lowStockProducts ->
            lowStockProducts.map { product ->
                val offers = supplierRepository.getSuppliersForProduct(product.id)

                val primary = offers.find { it.isPrimary }
                val cheapest = offers.minByOrNull { it.buyPrice }

                var recommended = primary
                var savings = 0.0

                if (cheapest != null && primary != null) {
                    if (cheapest.buyPrice < primary.buyPrice) {
                        recommended = cheapest
                        savings = (primary.buyPrice - cheapest.buyPrice) * (product.minStock - product.stock + 10)
                    }
                } else if (cheapest != null && primary == null) {
                    recommended = cheapest
                }

                SmartPoItem(
                    product = product,
                    primarySupplier = primary,
                    cheapestSupplier = cheapest,
                    recommendedSupplier = recommended,
                    potentialSavings = savings
                )
            }
        }
    }
}

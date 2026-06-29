package com.inventorypos.domain.usecase.product

import com.inventorypos.data.local.dao.ProductSupplierDao
import com.inventorypos.domain.model.SmartPoItem
import com.inventorypos.domain.model.SupplierOffer
import com.inventorypos.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetSmartPoListUseCase @Inject constructor(
    private val productRepository: ProductRepository,
    private val productSupplierDao: ProductSupplierDao // Injeksi langsung DAO untuk kecepatan membaca relasi
) {
    // Fungsi ini menghasilkan List yang otomatis ter-update jika stok berubah
    operator fun invoke(): Flow<List<SmartPoItem>> {
        // 1. Ambil semua produk yang stoknya menipis
        return productRepository.getLowStockProducts().map { lowStockProducts ->
            
            // 2. Analisis setiap produk satu per satu
            lowStockProducts.map { product ->
                
                // Cari siapa saja yang menyuplai produk ini
                val suppliers = productSupplierDao.getSuppliersForProduct(product.id)
                
                val offers = suppliers.map { 
                    SupplierOffer(it.supplier.id, it.supplier.name, it.buyPrice, it.isPrimary) 
                }

                // Tentukan siapa Primary dan siapa yang Termurah
                val primary = offers.find { it.isPrimary }
                val cheapest = offers.minByOrNull { it.buyPrice }

                var recommended = primary
                var savings = 0.0

                // === LOGIKA KEPUTUSAN CERDAS ===
                if (cheapest != null && primary != null) {
                    if (cheapest.buyPrice < primary.buyPrice) {
                        recommended = cheapest // Alihkan ke yang lebih murah!
                        savings = primary.buyPrice - cheapest.buyPrice
                    }
                } else if (cheapest != null && primary == null) {
                    recommended = cheapest // Jika tidak ada primary, ambil yang termurah
                }

                // Kembalikan hasil analisis
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


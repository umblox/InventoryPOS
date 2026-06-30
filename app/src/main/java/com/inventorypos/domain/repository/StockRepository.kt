package com.inventorypos.domain.repository

import com.inventorypos.data.local.entity.StockLogEntity
import com.inventorypos.data.local.entity.StockLogType
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    fun getLogsByProduct(productId: Long): Flow<List<StockLogEntity>>
    fun getRecentLogs(): Flow<List<StockLogEntity>>
    fun getLogsByType(type: StockLogType): Flow<List<StockLogEntity>>
    suspend fun insertLog(log: StockLogEntity): Long
}


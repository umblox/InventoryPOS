package com.inventorypos.data.repository

import com.inventorypos.data.local.dao.StockDao
import com.inventorypos.data.local.entity.StockLogEntity
import com.inventorypos.data.local.entity.StockLogType
import com.inventorypos.domain.repository.StockRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class StockRepositoryImpl @Inject constructor(
    private val stockDao: StockDao
) : StockRepository {

    override fun getLogsByProduct(productId: Long): Flow<List<StockLogEntity>> {
        return stockDao.getLogsByProduct(productId)
    }

    override fun getRecentLogs(): Flow<List<StockLogEntity>> {
        return stockDao.getRecentLogs()
    }

    override fun getLogsByType(type: StockLogType): Flow<List<StockLogEntity>> {
        return stockDao.getLogsByType(type)
    }

    override suspend fun insertLog(log: StockLogEntity): Long {
        return stockDao.insertLog(log)
    }
}


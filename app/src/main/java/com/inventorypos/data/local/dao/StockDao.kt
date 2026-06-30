package com.inventorypos.data.local.dao

import androidx.room.*
import com.inventorypos.data.local.entity.StockLogEntity
import com.inventorypos.data.local.entity.StockLogType
import kotlinx.coroutines.flow.Flow

@Dao
interface StockDao {
    @Query("SELECT * FROM stock_logs WHERE productId = :productId ORDER BY createdAt DESC")
    fun getLogsByProduct(productId: Long): Flow<List<StockLogEntity>>
    
    @Query("SELECT * FROM stock_logs ORDER BY createdAt DESC LIMIT 100")
    fun getRecentLogs(): Flow<List<StockLogEntity>>
    
    @Query("SELECT * FROM stock_logs WHERE type = :type ORDER BY createdAt DESC")
    fun getLogsByType(type: StockLogType): Flow<List<StockLogEntity>>
    
    @Insert
    suspend fun insertLog(log: StockLogEntity): Long
    
    // DIPERBAIKI: Konversi milidetik ke detik (/ 1000) dan sesuaikan zona waktu
    @Query("""
        SELECT SUM(quantity) FROM stock_logs 
        WHERE productId = :productId AND type = 'IN' AND date(createdAt / 1000, 'unixepoch', 'localtime') = date('now', 'localtime')
    """)
    suspend fun getTodayStockIn(productId: Long): Int?
    
    // DIPERBAIKI: Konversi milidetik ke detik (/ 1000) dan sesuaikan zona waktu
    @Query("""
        SELECT SUM(quantity) FROM stock_logs 
        WHERE productId = :productId AND type = 'OUT' AND date(createdAt / 1000, 'unixepoch', 'localtime') = date('now', 'localtime')
    """)
    suspend fun getTodayStockOut(productId: Long): Int?
    
    @Query("""
        SELECT sl.*, p.name as productName 
        FROM stock_logs sl
        INNER JOIN products p ON sl.productId = p.id
        ORDER BY sl.createdAt DESC LIMIT 100
    """)
    fun getRecentLogsWithProduct(): Flow<List<StockLogWithProduct>>
}

data class StockLogWithProduct(
    @Embedded val log: StockLogEntity,
    val productName: String
)

package com.inventorypos.data.local.database

import androidx.room.TypeConverter
import com.inventorypos.data.local.entity.PaymentMethod
import com.inventorypos.data.local.entity.PaymentStatus
import com.inventorypos.data.local.entity.SyncStatus
import com.inventorypos.data.local.entity.UserRole
import java.util.Date

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }
    
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
    
    @TypeConverter
    fun fromUserRole(role: UserRole): String = role.name
    
    @TypeConverter
    fun toUserRole(value: String): UserRole = UserRole.valueOf(value)
    
    @TypeConverter
    fun fromPaymentMethod(method: PaymentMethod): String = method.name
    
    @TypeConverter
    fun toPaymentMethod(value: String): PaymentMethod = PaymentMethod.valueOf(value)
    
    @TypeConverter
    fun fromPaymentStatus(status: PaymentStatus): String = status.name
    
    @TypeConverter
    fun toPaymentStatus(value: String): PaymentStatus = PaymentStatus.valueOf(value)
    
    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String = status.name
    
    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus = SyncStatus.valueOf(value)
}

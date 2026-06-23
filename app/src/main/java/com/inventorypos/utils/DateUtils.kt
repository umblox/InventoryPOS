package com.inventorypos.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
    private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    
    fun formatDate(date: Date?): String {
        return date?.let { dateFormat.format(it) } ?: "-"
    }
    
    fun formatTime(date: Date?): String {
        return date?.let { timeFormat.format(it) } ?: "-"
    }
    
    fun formatDateTime(date: Date?): String {
        return date?.let { dateTimeFormat.format(it) } ?: "-"
    }
    
    fun toIsoString(date: Date): String {
        return isoFormat.format(date)
    }
    
    fun fromIsoString(iso: String): Date? {
        return try {
            isoFormat.parse(iso)
        } catch (e: Exception) {
            null
        }
    }
    
    fun getStartOfDay(): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        return cal.time
    }
    
    fun getEndOfDay(): Date {
        val cal = Calendar.getInstance()
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        cal.set(Calendar.MILLISECOND, 999)
        return cal.time
    }
}

package com.inventorypos.utils

import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.common.BitMatrix

object BarcodeUtils {
    fun generateBarcode(content: String, width: Int = 300, height: Int = 100): BitMatrix? {
        return try {
            MultiFormatWriter().encode(
                content,
                BarcodeFormat.CODE_128,
                width,
                height
            )
        } catch (e: Exception) {
            null
        }
    }
    
    fun generateQRCode(content: String, size: Int = 300): BitMatrix? {
        return try {
            MultiFormatWriter().encode(
                content,
                BarcodeFormat.QR_CODE,
                size,
                size
            )
        } catch (e: Exception) {
            null
        }
    }
    
    fun generateSKU(prefix: String = "PRD", number: Long): String {
        return "$prefix-${String.format("%06d", number)}"
    }
}

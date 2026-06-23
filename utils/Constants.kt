package com.inventorypos.utils

object Constants {
    const val DATABASE_NAME = "inventory_pos.db"
    const val PREFERENCES_NAME = "inventory_pos_prefs"
    
    const val KEY_USER_ID = "user_id"
    const val KEY_USER_NAME = "user_name"
    const val KEY_USER_ROLE = "user_role"
    const val KEY_IS_LOGGED_IN = "is_logged_in"
    const val KEY_BIOMETRIC_ENABLED = "biometric_enabled"
    
    const val DEFAULT_CURRENCY = "Rp"
    const val DATE_FORMAT = "dd MMM yyyy"
    const val TIME_FORMAT = "HH:mm"
    const val DATETIME_FORMAT = "dd MMM yyyy, HH:mm"
    
    const val MIN_PASSWORD_LENGTH = 6
    const val MAX_IMAGE_SIZE_MB = 5
    
    const val SYNC_INTERVAL_MINUTES = 15L
}

package com.inventorypos.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.inventorypos.utils.Constants
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.PREFERENCES_NAME)

@Singleton
class UserPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore = context.dataStore

    companion object {
        val KEY_USER_ID = longPreferencesKey("user_id")
        val KEY_USER_NAME = stringPreferencesKey("user_name")
        val KEY_USER_ROLE = stringPreferencesKey("user_role")
        val KEY_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val KEY_BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
    }

    val isLoggedIn: Flow<Boolean> = dataStore.data.map { it[KEY_IS_LOGGED_IN] ?: false }
    val userId: Flow<Long> = dataStore.data.map { it[KEY_USER_ID] ?: 0L }
    val userName: Flow<String> = dataStore.data.map { it[KEY_USER_NAME] ?: "" }
    val userRole: Flow<String> = dataStore.data.map { it[KEY_USER_ROLE] ?: "" }

    suspend fun saveUserSession(userId: Long, userName: String, userRole: String) {
        dataStore.edit { preferences ->
            preferences[KEY_USER_ID] = userId
            preferences[KEY_USER_NAME] = userName
            preferences[KEY_USER_ROLE] = userRole
            preferences[KEY_IS_LOGGED_IN] = true
        }
    }

    suspend fun clearSession() {
        dataStore.edit { it.clear() }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        dataStore.edit { it[KEY_BIOMETRIC_ENABLED] = enabled }
    }
}

package com.inventorypos.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

// Ekstensi untuk membuat DataStore
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "auth_prefs")

@Singleton
class AuthPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    private val USER_ID = longPreferencesKey("user_id")
    private val USERNAME = stringPreferencesKey("username")

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { it[IS_LOGGED_IN] ?: false }
    val loggedInUserId: Flow<Long> = context.dataStore.data.map { it[USER_ID] ?: -1L }
    
    suspend fun saveSession(userId: Long, username: String) {
        context.dataStore.edit { prefs ->
            prefs[IS_LOGGED_IN] = true
            prefs[USER_ID] = userId
            prefs[USERNAME] = username
        }
    }

    suspend fun clearSession() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}

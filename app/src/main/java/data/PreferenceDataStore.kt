package data


import interfaces.Session
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val USER_IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
private val Context.userPreferencesDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "SESSION"
)

class PreferenceDataStore(context: Context) : Session {

    private val dataStore: DataStore<Preferences> = context.userPreferencesDataStore

    override suspend fun putBoolean(key: Preferences.Key<Boolean>, value: Boolean) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun getBoolean(key: Preferences.Key<Boolean>): Flow<Boolean?> {
        return dataStore.data.map { it[key] }
    }

    override suspend fun putString(key: Preferences.Key<String>, value: String) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun getString(key: Preferences.Key<String>): Flow<String?> {
        return dataStore.data.map { it[key] }
    }

    override suspend fun putLong(key: Preferences.Key<Long>, value: Long) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun getLong(key: Preferences.Key<Long>): Flow<Long?> {
        return dataStore.data.map { it[key] }
    }

    override suspend fun putInt(key: Preferences.Key<Int>, value: Int) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun getInt(key: Preferences.Key<Int>): Flow<Int?> {
        return dataStore.data.map { it[key] }
    }

    override suspend fun putDouble(key: Preferences.Key<Double>, value: Double) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun getDouble(key: Preferences.Key<Double>): Flow<Double?> {
        return dataStore.data.map { it[key] }
    }

    override suspend fun putFloat(key: Preferences.Key<Float>, value: Float) {
        dataStore.edit { it[key] = value }
    }

    override suspend fun getFloat(key: Preferences.Key<Float>): Flow<Float?> {
        return dataStore.data.map { it[key] }
    }

    override suspend fun clearLoggedInSession() {
        dataStore.edit { it.clear() }
    }
}
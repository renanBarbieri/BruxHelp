package br.com.bruxismhelper.platform.store

import android.content.Context
import androidx.datastore.preferences.core.Preferences.Key
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.bruxismhelper.emptyString
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


private val Context.dataStore by preferencesDataStore("user_preferences")

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext context: Context,
    private val gson: Gson
) {
    private val preferencesStore = context.dataStore

    //region read
    private inline fun <reified T> read(key: Key<T>, defaultValue: T): Flow<T> {
        return preferencesStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }
    }

    fun read(key: String, defaultValue: Int = -1): Flow<Int> {
        return read(intPreferencesKey(key), defaultValue)
    }

    fun read(key: String, defaultValue: String = emptyString()): Flow<String> {
        return read(stringPreferencesKey(key), defaultValue)
    }

    fun read(key: String, defaultValue: Boolean = false): Flow<Boolean> {
        return read(booleanPreferencesKey(key), defaultValue)
    }

    fun <T> read(key: String, defaultValue: T? = null): Flow<T> {
        val typeToken: TypeToken<T> = object : TypeToken<T>(javaClass){}
        val defaultValueString = gson.toJson(defaultValue)
        return read(stringPreferencesKey(key), defaultValueString).map { gson.fromJson(it, typeToken.type) }
    }
    //endregion

    //region write
    private suspend inline fun <reified T> write(key: Key<T>, value: T) {
        preferencesStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun write(key: String, value: Int) {
        preferencesStore.edit { settings ->
            settings[intPreferencesKey(key)] = value
        }
    }

    suspend fun write(key: String, value: String) {
        preferencesStore.edit { settings ->
            settings[stringPreferencesKey(key)] = value
        }
    }

    suspend fun write(key: String, value: Boolean) {
        preferencesStore.edit { settings ->
            settings[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun <T>write(key: String, value: T) {
        preferencesStore.edit { settings ->
            val valueString = gson.toJson(value)
            settings[stringPreferencesKey(key)] = valueString
        }
    }
    //endregion
}
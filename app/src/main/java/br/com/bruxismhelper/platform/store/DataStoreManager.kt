package br.com.bruxismhelper.platform.store

import android.content.Context
import androidx.datastore.preferences.core.Preferences.Key
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import br.com.bruxismhelper.emptyString
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DataStoreManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    private val Context.dataStore by preferencesDataStore("user_preferences")

    //region read
    private inline fun <reified T> read(key: Key<T>, defaultValue: T): Flow<T> {
        return context.dataStore.data.map { it[key] ?: defaultValue }
    }

    fun readInt(key: String, defaultValue: Int = -1): Flow<Int> {
        return read(intPreferencesKey(key), defaultValue)
    }

    fun readString(key: String, defaultValue: String = emptyString()): Flow<String> {
        return read(stringPreferencesKey(key), defaultValue)
    }

    fun readBoolean(key: String, defaultValue: Boolean = false): Flow<Boolean> {
        return read(booleanPreferencesKey(key), defaultValue)
    }

    fun <T> readObject(key: String, defaultValue: T? = null, type: Class<T>): Flow<T> {
        val defaultValueString = gson.toJson(defaultValue)
        return readString(key, defaultValueString).map { gson.fromJson(it, type) }
    }

    inline fun <reified T> readObject(key: String, defaultValue: T? = null): Flow<T> {
        return readObject(key, defaultValue, T::class.java)
    }
    //endregion

    //region write
    private suspend inline fun <reified T> write(key: Key<T>, value: T) {
        context.dataStore.edit { it[key] = value }
    }

    suspend fun writeInt(key: String, value: Int) {
        write(intPreferencesKey(key), value)
    }

    suspend fun writeString(key: String, value: String) {
        write(stringPreferencesKey(key), value)
    }

    suspend fun writeBoolean(key: String, value: Boolean) {
        write(booleanPreferencesKey(key), value)
    }

    suspend fun <T>writeObject(key: String, value: T) {
        val valueString = gson.toJson(value)
        writeString(key, valueString)
    }
    //endregion
}
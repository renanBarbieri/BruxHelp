package br.com.bruxismhelper.shared.repository.source

import br.com.bruxismhelper.platform.store.DataStoreManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(private val dataStoreManager: DataStoreManager) {
    private val KEY_USER_ID = "user_id"

    suspend fun saveUserRegisterId(userId: String) {
        dataStoreManager.write(KEY_USER_ID, userId)
    }

    fun getUserRegisterId(): Flow<String> {
        return dataStoreManager.read<String>(KEY_USER_ID)
    }
}
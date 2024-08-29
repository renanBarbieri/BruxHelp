package br.com.bruxismhelper.shared.repository.source

import br.com.bruxismhelper.platform.store.DataStoreManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserLocalDataSource @Inject constructor(private val dataStoreManager: DataStoreManager) {
    private val KEY_USER_ID = "user_id"
    private val KEY_USER_AGREEMENT = "user_agreement"

    suspend fun saveUserRegisterId(userId: String) {
        dataStoreManager.writeString(KEY_USER_ID, userId)
    }

    fun getUserRegisterId(): Flow<String> {
        return dataStoreManager.readString(KEY_USER_ID)
    }

    suspend fun setUserAgreement(agreed: Boolean = true) {
        dataStoreManager.writeBoolean(KEY_USER_AGREEMENT, agreed)
    }
}
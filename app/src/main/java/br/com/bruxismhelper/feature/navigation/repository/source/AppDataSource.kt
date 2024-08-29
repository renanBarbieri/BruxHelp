package br.com.bruxismhelper.feature.navigation.repository.source

import br.com.bruxismhelper.platform.store.DataStoreManager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

private const val KEY_REGISTER_SCREEN_SHOWN = "register_screen"
private const val KEY_AGREEMENT_SCREEN_SHOWN = "agreement_screen"
private const val KEY_ALARM_FIRED = "alarm_fired"

class AppDataSource @Inject constructor(private val dataStoreManager: DataStoreManager) {

    suspend fun setRegisterScreenShown() {
        dataStoreManager.writeBoolean(KEY_REGISTER_SCREEN_SHOWN, true)
    }

    fun isRegisterScreenShown(): Flow<Boolean> {
        return dataStoreManager.readBoolean(KEY_REGISTER_SCREEN_SHOWN)
    }

    suspend fun setAlarmFired(isFired: Boolean) {
        dataStoreManager.writeBoolean(KEY_ALARM_FIRED, isFired)
    }

    fun isAlarmFired(): Flow<Boolean> {
        return dataStoreManager.readBoolean(KEY_ALARM_FIRED)
    }

    suspend fun setAgreementScreenShown() {
        dataStoreManager.writeBoolean(KEY_AGREEMENT_SCREEN_SHOWN, true)
    }

    fun isAgreementScreenShown(): Flow<Boolean> {
        return dataStoreManager.readBoolean(KEY_AGREEMENT_SCREEN_SHOWN)
    }
}
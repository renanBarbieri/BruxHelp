package br.com.bruxismhelper.feature.navigation.repository

import br.com.bruxismhelper.feature.navigation.domain.repository.NavigationRepository
import br.com.bruxismhelper.feature.navigation.repository.source.AppDataSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NavigationRepositoryImpl @Inject constructor(
    private val appLocalDataSource: AppDataSource
): NavigationRepository {
    override suspend fun setRegisterScreenShown() {
        appLocalDataSource.setRegisterScreenShown()
    }

    override suspend fun isRegisterScreenShown(): Flow<Boolean> {
        return appLocalDataSource.isRegisterScreenShown()
    }

    override suspend fun isAlarmFired(): Flow<Boolean> {
        return appLocalDataSource.isAlarmFired()
    }

    override suspend fun setAlarmFired() {
        appLocalDataSource.setAlarmFired(true)
    }

    override suspend fun setBruxismFormAnswered() {
        appLocalDataSource.setAlarmFired(false)
    }
}
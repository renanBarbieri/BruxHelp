package br.com.bruxismhelper.feature.navigation.repository

import br.com.bruxismhelper.feature.navigation.domain.repository.NavigationRepository
import br.com.bruxismhelper.feature.navigation.repository.source.AppDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.single
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
}
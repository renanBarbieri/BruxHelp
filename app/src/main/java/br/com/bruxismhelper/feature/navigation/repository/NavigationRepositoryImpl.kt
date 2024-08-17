package br.com.bruxismhelper.feature.navigation.repository

import br.com.bruxismhelper.feature.navigation.domain.repository.NavigationRepository
import br.com.bruxismhelper.feature.navigation.repository.source.AppDataSource
import kotlinx.coroutines.flow.last
import javax.inject.Inject

class NavigationRepositoryImpl @Inject constructor(
    private val appLocalDataSource: AppDataSource
): NavigationRepository {
    override suspend fun setRegisterScreenShown() {
        appLocalDataSource.setRegisterScreenShown()
    }

    override suspend fun isRegisterScreenShown(): Boolean {
        return appLocalDataSource.isRegisterScreenShown().last()
    }

    override suspend fun isAlarmFired(): Boolean {
        return appLocalDataSource.isAlarmFired().last()
    }
}
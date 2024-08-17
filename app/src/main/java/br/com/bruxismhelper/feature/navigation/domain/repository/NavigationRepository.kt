package br.com.bruxismhelper.feature.navigation.domain.repository

interface NavigationRepository {
    suspend fun setRegisterScreenShown()
    suspend fun isRegisterScreenShown(): Boolean
    suspend fun isAlarmFired(): Boolean
}
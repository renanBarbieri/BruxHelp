package br.com.bruxismhelper.feature.navigation.domain.repository

import kotlinx.coroutines.flow.Flow

interface NavigationRepository {
    suspend fun setRegisterScreenShown()
    suspend fun isRegisterScreenShown(): Flow<Boolean>
    suspend fun isAlarmFired(): Flow<Boolean>
}
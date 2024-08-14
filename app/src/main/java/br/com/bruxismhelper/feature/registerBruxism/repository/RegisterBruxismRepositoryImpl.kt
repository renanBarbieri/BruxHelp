package br.com.bruxismhelper.feature.registerBruxism.repository

import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm
import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import br.com.bruxismhelper.feature.registerBruxism.repository.source.RegisterBruxismRemoteDataSource
import br.com.bruxismhelper.shared.repository.source.UserLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RegisterBruxismRepositoryImpl @Inject constructor(
    private val remoteDataSource: RegisterBruxismRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val mapper: RegisterBruxismRepositoryMapper
): RegisterBruxismRepository {
    override suspend fun submitForm(registerForm: RegisterBruxismForm): Result<Unit> {
        val registeredUser = runBlocking(Dispatchers.IO) {
            userLocalDataSource.getUserRegisterId().first()
        }

        val result = remoteDataSource.submitForm(
            userDocumentPath = registeredUser,
            fieldsMap = mapper.mapFromDomain(registerForm)
        )

        return result.map {}
    }
}
package br.com.bruxismhelper.feature.registerBruxism.repository

import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm
import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import br.com.bruxismhelper.feature.registerBruxism.repository.source.RegisterBruxismRemoteDataSource
import br.com.bruxismhelper.shared.repository.source.UserLocalDataSource
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

class RegisterBruxismRepositoryImpl @Inject constructor(
    private val remoteDataSource: RegisterBruxismRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val mapper: RegisterBruxismRepositoryMapper
) : RegisterBruxismRepository {
    override suspend fun submitForm(registerForm: RegisterBruxismForm): Result<Unit> {
        val registeredUser = runBlocking(Dispatchers.IO) {
            userLocalDataSource.getUserRegisterId().first()
        }

        Firebase.crashlytics.setUserId(registeredUser)

        val result = if (registeredUser.isBlank()) Result.failure(UserNotFound())
        else remoteDataSource.submitForm(
            userDocumentPath = registeredUser,
            fieldsMap = mapper.mapFromDomain(registerForm)
        )

        return result.map {}
    }
}

class UserNotFound : Throwable("User not found")
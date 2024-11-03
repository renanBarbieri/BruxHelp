package br.com.bruxismhelper.feature.registerBruxism.repository

import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm
import br.com.bruxismhelper.feature.registerBruxism.domain.model.ResponseBruxismForm
import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import br.com.bruxismhelper.feature.registerBruxism.repository.source.RegisterBruxismRemoteDataSource
import br.com.bruxismhelper.shared.repository.source.UserLocalDataSource
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RegisterBruxismRepositoryImpl @Inject constructor(
    private val remoteDataSource: RegisterBruxismRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val mapper: RegisterBruxismRepositoryMapper
) : RegisterBruxismRepository {

    override suspend fun submitForm(registerForm: RegisterBruxismForm): Result<Unit> = withContext(Dispatchers.IO){
        val registeredUser = userLocalDataSource.getUserRegisterId().first()

        Firebase.crashlytics.setUserId(registeredUser)

        val result = if (registeredUser.isBlank()) Result.failure(UserNotFound())
        else remoteDataSource.submitForm(
            userDocumentPath = registeredUser,
            fieldsMap = mapper.mapFromDomain(registerForm)
        )

        return@withContext result.map {}
    }

    override suspend fun getResponses(): Result<List<ResponseBruxismForm>> = withContext(Dispatchers.IO){
        val registeredUser = userLocalDataSource.getUserRegisterId().first()
        Firebase.crashlytics.setUserId(registeredUser)

        val result = if (registeredUser.isBlank()) Result.failure(UserNotFound())
        else remoteDataSource.getResponsesFromUser(userDocumentPath = registeredUser)

        return@withContext result.map { mapper.mapToDomain(it) }
    }
}

class UserNotFound : Throwable("User not found")
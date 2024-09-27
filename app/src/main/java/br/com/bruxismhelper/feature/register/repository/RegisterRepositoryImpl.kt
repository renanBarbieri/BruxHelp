package br.com.bruxismhelper.feature.register.repository

import br.com.bruxismhelper.feature.register.domain.model.Dentist
import br.com.bruxismhelper.feature.register.domain.model.RegisterForm
import br.com.bruxismhelper.feature.register.domain.repository.RegisterRepository
import br.com.bruxismhelper.feature.register.repository.source.RegisterRemoteDataSource
import br.com.bruxismhelper.shared.repository.source.UserLocalDataSource
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(
    private val remoteDataSource: RegisterRemoteDataSource,
    private val userLocalDataSource: UserLocalDataSource,
    private val mapper: RegisterRepositoryMapper
): RegisterRepository {

    override fun getAvailableDentists(): List<Dentist> {
        return listOf(Dentist(name = "Dra. Nathália Celestino"), Dentist(name = "Outro") )
    }

    override suspend fun submitForm(registerForm: RegisterForm): Result<Unit> {
        val formRequest = remoteDataSource.submitForm(fieldsMap = mapper.mapFromDomain(registerForm))

        formRequest.getOrNull()?.let {
            userLocalDataSource.saveUserRegisterId(it)
            Firebase.crashlytics.setUserId(it)
        }

        return formRequest.map {  }
    }
}
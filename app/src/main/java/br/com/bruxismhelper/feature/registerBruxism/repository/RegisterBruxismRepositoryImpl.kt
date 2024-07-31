package br.com.bruxismhelper.feature.registerBruxism.repository

import br.com.bruxismhelper.feature.registerBruxism.domain.model.RegisterBruxismForm
import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import javax.inject.Inject

class RegisterBruxismRepositoryImpl @Inject constructor(): RegisterBruxismRepository {
    override suspend fun submitForm(registerForm: RegisterBruxismForm) {
        TODO("Not yet implemented")
    }
}
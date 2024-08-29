package br.com.bruxismhelper.feature.agreement.repository

import br.com.bruxismhelper.feature.agreement.domain.repository.AgreementRepository
import br.com.bruxismhelper.shared.repository.source.UserLocalDataSource
import javax.inject.Inject

class AgreementRepositoryImpl @Inject constructor(
    private val userLocalDataSource: UserLocalDataSource
): AgreementRepository {

    override suspend fun setAsAgreed() {
        userLocalDataSource.setUserAgreement()
    }
}
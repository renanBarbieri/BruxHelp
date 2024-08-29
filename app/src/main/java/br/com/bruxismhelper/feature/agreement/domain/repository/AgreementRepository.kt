package br.com.bruxismhelper.feature.agreement.domain.repository

interface AgreementRepository {
    suspend fun setAsAgreed()
}
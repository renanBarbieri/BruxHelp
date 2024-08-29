package br.com.bruxismhelper.feature.agreement.di

import br.com.bruxismhelper.feature.agreement.domain.repository.AgreementRepository
import br.com.bruxismhelper.feature.agreement.repository.AgreementRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class AgreementModule {

    @Binds
    abstract fun bindRepository(impl: AgreementRepositoryImpl): AgreementRepository
}
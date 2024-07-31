package br.com.bruxismhelper.feature.registerBruxism.di

import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import br.com.bruxismhelper.feature.registerBruxism.repository.RegisterBruxismRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class BruxismRepositoryModule {

    @Binds
    abstract fun bindRepository(impl: RegisterBruxismRepositoryImpl): RegisterBruxismRepository
}
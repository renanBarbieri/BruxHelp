package br.com.bruxismhelper.feature.registerBruxism.di

import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import br.com.bruxismhelper.feature.registerBruxism.repository.RegisterBruxismRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class BruxismRepositoryModule {

    @Binds
    abstract fun bindRepository(impl: RegisterBruxismRepositoryImpl): RegisterBruxismRepository
}
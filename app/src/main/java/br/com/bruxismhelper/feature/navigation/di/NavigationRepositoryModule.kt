package br.com.bruxismhelper.feature.navigation.di

import br.com.bruxismhelper.feature.navigation.domain.repository.NavigationRepository
import br.com.bruxismhelper.feature.navigation.repository.NavigationRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class NavigationRepositoryModule {

    @Binds
    abstract fun bindRepository(impl: NavigationRepositoryImpl): NavigationRepository
}
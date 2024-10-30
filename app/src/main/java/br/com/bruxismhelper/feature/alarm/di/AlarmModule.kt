package br.com.bruxismhelper.feature.alarm.di

import br.com.bruxismhelper.feature.alarm.AlarmSchedulerFacade
import br.com.bruxismhelper.feature.alarm.AlarmSchedulerFacadeImpl
import br.com.bruxismhelper.feature.alarm.repository.AlarmRepository
import br.com.bruxismhelper.feature.alarm.repository.AlarmRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AlarmModule {

    @Binds
    abstract fun bindAlarmFacade(impl: AlarmSchedulerFacadeImpl): AlarmSchedulerFacade

    @Binds
    abstract fun bindRepository(impl: AlarmRepositoryImpl): AlarmRepository

}
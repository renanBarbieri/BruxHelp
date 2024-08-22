package br.com.bruxismhelper.feature.alarm.di

import br.com.bruxismhelper.feature.alarm.AlarmSchedulerFacade
import br.com.bruxismhelper.feature.alarm.AlarmSchedulerFacadeImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class AlarmModule {

    @Binds
    abstract fun bindAlarmFacade(impl: AlarmSchedulerFacadeImpl): AlarmSchedulerFacade
}
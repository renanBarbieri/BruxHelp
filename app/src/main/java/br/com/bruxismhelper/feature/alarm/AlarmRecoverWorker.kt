package br.com.bruxismhelper.feature.alarm

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit

@HiltWorker
class AlarmRecoverWorker @AssistedInject constructor(
    @Assisted private val alarmSchedulerFacade: AlarmSchedulerFacade,
    @ApplicationContext appContext: Context,
    @Assisted workerParams: WorkerParameters,
): CoroutineWorker(appContext, workerParams)  {

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }

    companion object {
        const val RECOVER_WORK_NAME = "AlarmRecoverWorker"

        val recoverWorkRequest = PeriodicWorkRequestBuilder<AlarmRecoverWorker>(1, TimeUnit.DAYS).build()
    }

    @AssistedFactory
    interface AlarmWorkerFactory {
        fun create(
            alarmSchedulerFacade: AlarmSchedulerFacade,
            workerParams: WorkerParameters,
        ): AlarmRecoverWorker
    }
}
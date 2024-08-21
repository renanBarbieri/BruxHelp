package br.com.bruxismhelper.feature.alarm

import android.app.Application
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AlarmSchedulerFacadeImpl @Inject constructor(
    @ApplicationContext private val app: Application,
) : AlarmSchedulerFacade {
    override fun executeOnce() {
        WorkManager.getInstance(app)
            .enqueue(OneTimeWorkRequestBuilder<AlarmSchedulerWorkManager>().build())
    }
}
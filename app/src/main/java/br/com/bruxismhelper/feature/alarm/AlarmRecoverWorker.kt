package br.com.bruxismhelper.feature.alarm

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import br.com.bruxismhelper.feature.alarm.data.DayAlarmTimeHelper
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltWorker
class AlarmRecoverWorker @AssistedInject constructor(
    @Assisted private val alarmSchedulerFacade: AlarmSchedulerFacade,
    @Assisted private val dayAlarmTimeHelper: DayAlarmTimeHelper,
    @ApplicationContext appContext: Context,
    @Assisted workerParams: WorkerParameters,
): CoroutineWorker(appContext, workerParams)  {

    override suspend fun doWork(): Result {
        val lastTimeScheduled = alarmSchedulerFacade.getNextCalendarAlarmScheduled() ?: run {
            //TODO Get firestore last response
            return Result.failure()
        }
        val previousAlarmCalendar = getPreviousAlarmCalendar()

        if (lastTimeScheduled.before(previousAlarmCalendar)) {
            Firebase.crashlytics.log("Recovering alarm")
            //FORCE TO SEND NOTIFICATION?
            alarmSchedulerFacade.scheduleNextAlarm(null)
        }

        return Result.success()
    }

    private fun getPreviousAlarmCalendar(): Calendar {
        val nowCalendar = Calendar.getInstance()
        val previousAlarmTime = dayAlarmTimeHelper.getDateAlarmPreviousTime(nowCalendar)
        return (nowCalendar.clone() as Calendar).apply {
            set(Calendar.HOUR_OF_DAY, previousAlarmTime.hour)
            set(Calendar.MINUTE, previousAlarmTime.minute)
        }
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
package br.com.bruxismhelper.feature.alarm

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import br.com.bruxismhelper.feature.alarm.data.DayAlarmTimeHelper
import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import logcat.logcat
import java.util.Calendar
import java.util.concurrent.TimeUnit

@HiltWorker
class AlarmRecoverWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val alarmSchedulerFacade: AlarmSchedulerFacade,
    private val dayAlarmTimeHelper: DayAlarmTimeHelper,
    private val registerBruxismRepository: RegisterBruxismRepository,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        logcat { "Doing my work" }
        val lastTimeScheduled =
            alarmSchedulerFacade.getNextCalendarAlarmScheduled() ?: getLastBruxismResponseCalendar()

        if(lastTimeScheduled == null) {
            logcat { "No alarm scheduled. This is probably first run" }
            return Result.success()
        }

        val previousAlarmCalendar = getPreviousAlarmCalendar()

        if (lastTimeScheduled.before(previousAlarmCalendar)) {
            logcat { "Recovering alarm" }
            Firebase.crashlytics.log("Recovering alarm")
            //FORCE TO SEND NOTIFICATION?
            alarmSchedulerFacade.scheduleNextAlarm(null)
        }

        return Result.success()
    }

    private suspend fun getLastBruxismResponseCalendar(): Calendar? =
        registerBruxismRepository.getResponses().getOrNull()
            ?.maxByOrNull { it.createdAt }?.createdAt

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

        val recoverWorkRequest =
            PeriodicWorkRequestBuilder<AlarmRecoverWorker>(1, TimeUnit.DAYS).build()
    }

    @AssistedFactory
    interface AlarmRecoverWorkerFactory {
        fun create(
            context: Context,
            workerParams: WorkerParameters,
        ): AlarmRecoverWorker
    }
}
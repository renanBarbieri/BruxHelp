package br.com.bruxismhelper.feature.alarm

import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.alarm.data.DayAlarmTimeHelper
import br.com.bruxismhelper.feature.registerBruxism.domain.repository.RegisterBruxismRepository
import br.com.bruxismhelper.platform.firebase.logMessage
import br.com.bruxismhelper.platform.firebase.logNonFatalException
import br.com.bruxismhelper.platform.notification.NotificationHelper
import br.com.bruxismhelper.platform.notification.data.AppChannel
import br.com.bruxismhelper.platform.notification.data.NotificationChannelProp
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

@HiltWorker
class AlarmRecoverWorker @AssistedInject constructor(
    @Assisted val appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val alarmSchedulerFacade: AlarmSchedulerFacade,
    private val dayAlarmTimeHelper: DayAlarmTimeHelper,
    private val registerBruxismRepository: RegisterBruxismRepository,
    private val notificationHelper: NotificationHelper,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        try {
            logMessage { "AlarmRecoverWorker running" }

            val lastTimeScheduled = alarmSchedulerFacade.getNextCalendarAlarmScheduled() 
                ?: getLastBruxismResponseCalendar()

            if(lastTimeScheduled == null) {
                logMessage { "No alarm scheduled. This is probably first run" }
                // Agendar primeiro alarme aqui
                alarmSchedulerFacade.scheduleNextAlarm(null)
                return Result.success()
            }

            val previousAlarmCalendar = getPreviousAlarmCalendar()
            val now = Calendar.getInstance()

            logMessage { "Last scheduled: ${lastTimeScheduled.toHumanString()}" }
            logMessage { "Previous alarm: ${previousAlarmCalendar.toHumanString()}" }
            logMessage { "Now: ${now.toHumanString()}" }

            if (lastTimeScheduled.before(previousAlarmCalendar) || lastTimeScheduled.before(now)) {
                logNonFatalException { "Recovering alarm. Last time scheduled: ${lastTimeScheduled.toHumanString()}" }

                alarmSchedulerFacade.scheduleNextAlarm(null)
                notificationHelper.sendNotification(
                    id = 1234,
                    context = appContext,
                    title = appContext.getString(R.string.alert_title),
                    body = appContext.getString(R.string.alert_message),
                    notificationChannelProp = NotificationChannelProp(
                        AppChannel.BRUXISM,
                        NotificationManager.IMPORTANCE_DEFAULT
                    )
                )
            }

            return Result.success()
        } catch (e: Exception) {
            logNonFatalException { "Error in AlarmRecoverWorker: ${e.message}" }
            return Result.retry()
        }
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

    private fun Calendar.toHumanString(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        // Format the Calendar instance to a string
        return dateFormat.format(time)
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
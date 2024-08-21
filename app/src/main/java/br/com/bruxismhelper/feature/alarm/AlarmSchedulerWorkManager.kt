package br.com.bruxismhelper.feature.alarm

import android.app.NotificationManager
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.alarm.data.AlarmItem
import br.com.bruxismhelper.feature.alarm.data.AlarmType
import br.com.bruxismhelper.feature.alarm.data.DayAlarmTime
import br.com.bruxismhelper.platform.notification.data.AppChannel
import br.com.bruxismhelper.platform.notification.data.NotificationChannelProp
import dagger.hilt.android.qualifiers.ApplicationContext
import logcat.logcat
import java.util.Calendar
import javax.inject.Inject

@HiltWorker
internal class AlarmSchedulerWorkManager @Inject constructor(
    @ApplicationContext private val appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        DayAlarmTime.entries.forEach {
            logcat { "scheduling ${it.name}" }
            val alarmItem = AlarmItem(
                id = 1234, //Fixed id
                channelProp = NotificationChannelProp(
                    AppChannel.BRUXISM,
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                timeInMillis = getRealTimeInMillis(it.timeMillis),
                title = appContext.getString(R.string.alert_title),
                message = appContext.getString(R.string.alert_message),
            )

            AlarmSchedulerHelper.schedule(
                appContext,
                alarmItem,
                AlarmType.Repeat(AlarmType.Repeat.RepeatInterval.INTERVAL_DAY)
            )
        }

        scheduleForOneMinute()

        return Result.success()
    }

    private fun scheduleForOneMinute(){
        logcat { "scheduling alarm for one minute" }

        val alarmItem = AlarmItem(
            channelProp = NotificationChannelProp(
                AppChannel.BRUXISM,
                NotificationManager.IMPORTANCE_DEFAULT
            ),
            timeInMillis = Calendar.getInstance().apply {
                add(Calendar.MINUTE, 2)

                logcat { "calendar time: $time" }
            }.timeInMillis,
            title = appContext.getString(R.string.alert_title),
            message = appContext.getString(R.string.alert_message),
        )

        AlarmSchedulerHelper.schedule(
            appContext,
            alarmItem,
            AlarmType.Repeat(AlarmType.Repeat.RepeatInterval.INTERVAL_DAY)
        )
    }

    private fun getRealTimeInMillis(alarmTime: Int): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.MILLISECOND, alarmTime)
        }

        logcat { "calendar time: ${calendar.time}" }

        return calendar.timeInMillis
    }
}
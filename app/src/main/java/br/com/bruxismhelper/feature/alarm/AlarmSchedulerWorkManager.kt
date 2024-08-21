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
import javax.inject.Inject

@HiltWorker
internal class AlarmSchedulerWorkManager @Inject constructor(
    @ApplicationContext private val appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        DayAlarmTime.entries.forEach {
            val alarmItem = AlarmItem(
                channelProp = NotificationChannelProp(
                    AppChannel.BRUXISM,
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                timeInMillis = it.timeMillis,
                title = appContext.getString(R.string.alert_title),
                message = appContext.getString(R.string.alert_message),
            )

            AlarmSchedulerHelper.schedule(
                appContext,
                alarmItem,
                AlarmType.Repeat(AlarmType.Repeat.RepeatInterval.INTERVAL_DAY)
            )
        }

        return Result.success()
    }
}
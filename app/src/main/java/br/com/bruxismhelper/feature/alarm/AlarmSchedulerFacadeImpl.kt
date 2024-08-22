package br.com.bruxismhelper.feature.alarm

import android.app.NotificationManager
import android.content.Context
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

internal class AlarmSchedulerFacadeImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
) : AlarmSchedulerFacade {
    override fun scheduleNextAlarm(currentAlarmTime: DayAlarmTime?) {

        val alarmToSchedule = DayAlarmTime.getNextTime(currentAlarmTime, Calendar.getInstance().timeInMillis)

        logcat { "scheduling ${alarmToSchedule.name}" }
        val alarmItem = AlarmItem(
            id = alarmToSchedule.ordinal,
            channelProp = NotificationChannelProp(
                AppChannel.BRUXISM,
                NotificationManager.IMPORTANCE_DEFAULT
            ),
            timeInMillis = alarmToSchedule.timeMillis,
            title = appContext.getString(R.string.alert_title),
            message = appContext.getString(R.string.alert_message),
        )

        getRealTimeInMillis(alarmToSchedule.timeMillis)

        AlarmSchedulerHelper.schedule(
            appContext,
            alarmItem,
            AlarmType.Default
        )
    }

    private fun getRealTimeInMillis(alarmTime: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.MILLISECOND, alarmTime.toInt())
        }

        logcat { "calendar time: ${calendar.time}" }

        return calendar.timeInMillis
    }

    private fun DayAlarmTime.Companion.getNextTime(
        currentAlarmTime: DayAlarmTime?,
        currentTimeInMillis: Long
    ): DayAlarmTime {
        return currentAlarmTime?.let {
            DayAlarmTime.getNextTime(it)
        } ?: DayAlarmTime.getNextTime(currentTimeInMillis)
    }
}



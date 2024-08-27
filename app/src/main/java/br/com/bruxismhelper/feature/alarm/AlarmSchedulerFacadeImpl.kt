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

        AlarmSchedulerHelper.schedule(
            appContext,
            alarmItem,
            AlarmType.Default
        )
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



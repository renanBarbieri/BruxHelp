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

        val alarmTime =
            DayAlarmTime.getNextTime(currentAlarmTime, Calendar.getInstance())

        logcat { "scheduling ${alarmTime.name}" }
        val alarmItem = AlarmItem(
            id = alarmTime.ordinal,
            channelProp = NotificationChannelProp(
                AppChannel.BRUXISM,
                NotificationManager.IMPORTANCE_DEFAULT
            ),
            timeInMillis = alarmTime.timeInMillisAfterNow(),
            title = appContext.getString(R.string.alert_title),
            message = appContext.getString(R.string.alert_message),
        )

        AlarmSchedulerHelper.schedule(
            appContext,
            alarmItem,
            AlarmType.Exact
        )
    }

    private fun DayAlarmTime.Companion.getNextTime(
        currentAlarmTime: DayAlarmTime?,
        nowCalendar: Calendar,
    ): DayAlarmTime {
        return currentAlarmTime?.let {
            DayAlarmTime.getNextTime(it)
        } ?: DayAlarmTime.getNextTime(
            nowCalendar.get(Calendar.HOUR_OF_DAY),
            nowCalendar.get(Calendar.MINUTE)
        )
    }

    private fun DayAlarmTime.timeInMillisAfterNow(): Long {
        val calendar = Calendar.getInstance()

        logcat { "calendar.timeInMillis = ${calendar.timeInMillis}" }

        if (calendar.hourMinutesInMinutes() > alarmTimeInMinutes) {
            logcat { "Adding 1 day to calendar;" }
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)

        logcat { "calendar.timeInMillis = ${calendar.timeInMillis}" }
        return calendar.timeInMillis
    }

    private fun Calendar.hourMinutesInMinutes() =
        get(Calendar.HOUR_OF_DAY) * 60 + get(Calendar.MINUTE)
}



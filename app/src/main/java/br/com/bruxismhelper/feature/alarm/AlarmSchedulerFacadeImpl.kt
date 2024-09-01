package br.com.bruxismhelper.feature.alarm

import android.app.NotificationManager
import android.content.Context
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.alarm.data.AlarmItem
import br.com.bruxismhelper.feature.alarm.data.AlarmType
import br.com.bruxismhelper.feature.alarm.data.DayAlarmTimeHelper
import br.com.bruxismhelper.platform.notification.data.AppChannel
import br.com.bruxismhelper.platform.notification.data.NotificationChannelProp
import dagger.hilt.android.qualifiers.ApplicationContext
import logcat.logcat
import java.util.Calendar
import javax.inject.Inject

internal class AlarmSchedulerFacadeImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val dayAlarmTimeHelper: DayAlarmTimeHelper,
) : AlarmSchedulerFacade {
    override fun scheduleNextAlarm(currentAlarmId: Int?) {
        val currentDayAlarm = currentAlarmId?.let { dayAlarmTimeHelper.getDayAlarmTimeByOrdinal(it) }

        val alarmTime =
            dayAlarmTimeHelper.getDayAlarmNextTime(currentDayAlarm, Calendar.getInstance())

        logcat { "scheduling ${alarmTime.name}" }
        val alarmItem = AlarmItem(
            id = alarmTime.ordinal,
            channelProp = NotificationChannelProp(
                AppChannel.BRUXISM,
                NotificationManager.IMPORTANCE_DEFAULT
            ),
            timeInMillis = dayAlarmTimeHelper.timeInMillisAfterNow(alarmTime, Calendar.getInstance()),
            title = appContext.getString(R.string.alert_title),
            message = appContext.getString(R.string.alert_message),
        )

        AlarmSchedulerHelper.schedule(
            appContext,
            alarmItem,
            AlarmType.Exact
        )
    }
}
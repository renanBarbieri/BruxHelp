package br.com.bruxismhelper.feature.alarm

import android.app.NotificationManager
import android.content.Context
import br.com.bruxismhelper.R
import br.com.bruxismhelper.emptyString
import br.com.bruxismhelper.feature.alarm.data.AlarmItem
import br.com.bruxismhelper.feature.alarm.data.AlarmType
import br.com.bruxismhelper.feature.alarm.data.DayAlarmTimeHelper
import br.com.bruxismhelper.platform.notification.data.AppChannel
import br.com.bruxismhelper.platform.notification.data.NotificationChannelProp
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import logcat.logcat
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AlarmSchedulerFacadeImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val alarmSchedulerHelper: AlarmSchedulerHelper,
    private val dayAlarmTimeHelper: DayAlarmTimeHelper,
) : AlarmSchedulerFacade {
    override fun scheduleNextAlarm(currentAlarmId: Int?, nowCalendar: Calendar) {
        val currentDayAlarm = currentAlarmId?.let { dayAlarmTimeHelper.getDayAlarmTimeByOrdinal(it) }
        Firebase.crashlytics.log(currentDayAlarm?.name ?: emptyString())

        val alarmTime =
            dayAlarmTimeHelper.getDayAlarmNextTime(currentDayAlarm, nowCalendar)

        logcat { "scheduling ${alarmTime.name}" }
        val alarmItem = AlarmItem(
            id = alarmTime.ordinal,
            channelProp = NotificationChannelProp(
                AppChannel.BRUXISM,
                NotificationManager.IMPORTANCE_DEFAULT
            ),
            timeInMillis = dayAlarmTimeHelper.timeInMillisAfterNow(alarmTime, nowCalendar),
            title = appContext.getString(R.string.alert_title),
            message = appContext.getString(R.string.alert_message),
        )

        alarmSchedulerHelper.schedule(
            appContext,
            alarmItem,
            AlarmType.Exact
        )
    }
}
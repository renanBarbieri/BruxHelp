package br.com.bruxismhelper.feature.alarm

import android.app.NotificationManager
import android.content.Context
import br.com.bruxismhelper.R
import br.com.bruxismhelper.emptyString
import br.com.bruxismhelper.feature.alarm.data.AlarmItem
import br.com.bruxismhelper.feature.alarm.data.AlarmType
import br.com.bruxismhelper.feature.alarm.data.DayAlarmTimeHelper
import br.com.bruxismhelper.feature.alarm.repository.AlarmRepository
import br.com.bruxismhelper.platform.notification.data.AppChannel
import br.com.bruxismhelper.platform.notification.data.NotificationChannelProp
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import logcat.logcat
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AlarmSchedulerFacadeImpl @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val alarmSchedulerHelper: AlarmSchedulerHelper,
    private val dayAlarmTimeHelper: DayAlarmTimeHelper,
    private val alarmRepository: AlarmRepository,
) : AlarmSchedulerFacade {
    override suspend fun scheduleNextAlarm(currentAlarmId: Int?, nowCalendar: Calendar) =
        withContext(Dispatchers.IO) {
            val currentDayAlarm =
                currentAlarmId?.let { dayAlarmTimeHelper.getDayAlarmTimeByOrdinal(it) }
            Firebase.crashlytics.log(currentDayAlarm?.name ?: emptyString())

            val alarmTime =
                dayAlarmTimeHelper.getDayAlarmNextTime(currentDayAlarm, nowCalendar)

            val calendarAfterNow = dayAlarmTimeHelper.calendarAfterNow(alarmTime, nowCalendar)
            Firebase.crashlytics.recordException(Throwable("Next alarm scheduled: ${calendarAfterNow.time}"))

            logcat { "scheduling ${alarmTime.name}" }
            val alarmItem = AlarmItem(
                id = alarmTime.ordinal,
                channelProp = NotificationChannelProp(
                    AppChannel.BRUXISM,
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                timeInMillis = calendarAfterNow.timeInMillis,
                title = appContext.getString(R.string.alert_title),
                message = appContext.getString(R.string.alert_message),
            )

            alarmSchedulerHelper.schedule(
                appContext,
                alarmItem,
                AlarmType.Exact
            )

            alarmRepository.saveNextAlarmItem(alarmItem)
        }
}
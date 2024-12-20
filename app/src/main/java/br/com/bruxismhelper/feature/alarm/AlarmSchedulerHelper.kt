package br.com.bruxismhelper.feature.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import br.com.bruxismhelper.feature.alarm.data.AlarmItem
import br.com.bruxismhelper.feature.alarm.data.AlarmType
import logcat.logcat
import javax.inject.Inject

private const val INTENT_KEY_ALARM_ITEM = "AlarmScheduler.ALARM_ITEM"

internal class AlarmSchedulerHelper @Inject constructor() {

    /**
     * Schedules a push notification using [AlarmManager]
     *
     * @param context A Context of the application package
     * @param item A [AlarmItem] with properties to configure alarm
     * @throws ScheduleExactAlarmNotAllowedException if is Android 12 or newer and
     * setScheduleExactAlarms is not allowed
     */
    @Throws(ScheduleExactAlarmNotAllowedException::class)
    fun schedule(context: Context, item: AlarmItem, type: AlarmType) {
        logcat { "Scheduling alarm: id=${item.id}, time=${item.timeInMillis}, type=$type" }
        
        val alarmManager = context.getAlarmManager()

        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(INTENT_KEY_ALARM_ITEM, item)
            addFlags(Intent.FLAG_RECEIVER_FOREGROUND)
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            item.id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val triggerTime = item.timeInMillis

        when (type) {
            is AlarmType.Repeat -> {
                logcat { "Scheduling repeating alarm with interval: ${type.interval.intervalMillis}" }
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    type.interval.intervalMillis,
                    pendingIntent
                )
            }

            is AlarmType.Exact -> {
                if (canScheduleAlarms(alarmManager)) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        pendingIntent
                    )
                } else {
                    throw ScheduleExactAlarmNotAllowedException()
                }
            }

            is AlarmType.AlarmClock -> {
                alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(triggerTime, pendingIntent), pendingIntent)
            }

            is AlarmType.Default -> {
                alarmManager.setAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    triggerTime,
                    pendingIntent
                )
            }
        }

        logcat { "Alarm scheduled successfully: id=${item.id}" }
    }

    private fun canScheduleAlarms(alarmManager: AlarmManager): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            alarmManager.canScheduleExactAlarms()
        } else {
            true
        }
    }

    /**
     * Cancel a [AlarmItem] schedule.
     * **IMPORTANT**: The AlarmItem properties must be exactly the same of scheduled AlarmItem
     *
     * @param context
     * @param alarmItem
     */
    @Suppress("unused")
    fun cancel(context: Context, alarmItem: AlarmItem) {
        val alarmManager = context.getAlarmManager()
        val intent = Intent(context, AlarmReceiver::class.java).apply {
            putExtra(INTENT_KEY_ALARM_ITEM, alarmItem)
        }

        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                alarmItem.id,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }

    private fun Context.getAlarmManager(): AlarmManager = getSystemService(AlarmManager::class.java)

    /**
     * Get the AlarmItem modem from intent extra param
     *
     * @param intent The Intent to be extracted.
     *
     * @return [AlarmItem] extracted from intent extras or null if has no AlarmItem on extras
     */
    @Suppress("DEPRECATION")
    fun extractAlarmItemFromExtra(intent: Intent): AlarmItem? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(INTENT_KEY_ALARM_ITEM, AlarmItem::class.java)
        } else {
            intent.getSerializableExtra(INTENT_KEY_ALARM_ITEM) as AlarmItem
        }
    }

    class ScheduleExactAlarmNotAllowedException : Exception() {
        override val message: String
            get() = "Sending exact alarms was not allowed by user"
    }
}
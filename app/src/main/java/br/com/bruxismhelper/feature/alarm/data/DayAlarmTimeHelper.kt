package br.com.bruxismhelper.feature.alarm.data

import logcat.logcat
import java.util.Calendar
import javax.inject.Inject

class DayAlarmTimeHelper @Inject constructor() {

    /**
     * Determines the next [DayAlarmTime] after the given [currentAlarmTime] or the current time represented by [nowCalendar].
     * * If [currentAlarmTime] is provided, it finds the next alarm time in the [DayAlarmTime] enum.
     * Otherwise, it calculates the next alarm time based on the current hour and minute from [nowCalendar].
     *
     * @param currentAlarmTime The current alarm time (optional).
     * @param nowCalendar A Calendar instance representing the current time.
     * @return The next [DayAlarmTime] after the given time.
     */
    fun getDayAlarmNextTime(
        currentAlarmTime: DayAlarmTime?,
        nowCalendar: Calendar,
    ): DayAlarmTime {
        return currentAlarmTime?.let {
            getDayAlarmNextTime(it)
        } ?: getDayAlarmNextTime(
            nowCalendar.get(Calendar.HOUR_OF_DAY),
            nowCalendar.get(Calendar.MINUTE)
        )
    }

    /**
     * Retrieves the [DayAlarmTime] enum value corresponding to the given ordinal.
     *
     * @param ordinal The ordinal value of the desired [DayAlarmTime].
     * @return The [DayAlarmTime] enum value with the matching ordinal, or throws an exception if no match is found.
     */
    fun getDayAlarmTimeByOrdinal(ordinal: Int) = DayAlarmTime.entries.first { it.ordinal == ordinal }

    private fun getDayAlarmNextTime(currentAlarmTime: DayAlarmTime): DayAlarmTime {
        var stopOnNext = false

        for(entry in DayAlarmTime.entries) {
            if(stopOnNext) return entry
            stopOnNext = entry.name == currentAlarmTime.name
        }

        return DayAlarmTime.FIRST
    }

    private fun getDayAlarmNextTime(currentHour: Int, currentMinute: Int): DayAlarmTime {
        val currentTimeInMinutes = currentHour * 60 + currentMinute
        DayAlarmTime.entries.forEach {
            if(currentTimeInMinutes <= it.alarmTimeInMinutes) return it
        }

        return DayAlarmTime.FIRST
    }

    fun getDateAlarmPreviousTime(nowCalendar: Calendar): DayAlarmTime {
        val currentTimeInMinutes = nowCalendar.get(Calendar.HOUR_OF_DAY) * 60 + nowCalendar.get(Calendar.MINUTE)
        var returnTime = DayAlarmTime.FIRST
        DayAlarmTime.entries.forEach {
            if(currentTimeInMinutes > it.alarmTimeInMinutes)
                returnTime = it
            else
                return@forEach
        }

        return returnTime
    }

    /**
     * Calculates the calendar time from the given [nowCalendar] to the specified [dayAlarmTime].
     *
     * If the current time (represented by [nowCalendar]) is already past the [dayAlarmTime],
     * the function will calculate the time until the alarm on the next day.
     *
     * @param dayAlarmTime The target alarm time.
     * @param nowCalendar A Calendar instance representing the current time.
     * @return The calendar time from [nowCalendar] to the [dayAlarmTime].
     */
    fun calendarAfterNow(dayAlarmTime: DayAlarmTime, nowCalendar: Calendar): Calendar {
        val calendar = nowCalendar.clone() as Calendar
        logcat { "calendar.timeInMillis = ${calendar.timeInMillis}" }

        if (hourMinutesInMinutes(calendar) > dayAlarmTime.alarmTimeInMinutes) {
            logcat { "Adding 1 day to calendar;" }
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        calendar.set(Calendar.HOUR_OF_DAY, dayAlarmTime.hour)
        calendar.set(Calendar.MINUTE, dayAlarmTime.minute)

        return calendar
    }

    private fun hourMinutesInMinutes(calendar: Calendar) =
        calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)
}
package br.com.bruxismhelper.feature.alarm.data

import java.util.Calendar

enum class DayAlarmTime(val hour: Int, val minute: Int) {
    FIRST(8,0),
    SECOND(9,20),
    THIRD(10,40),
    FOURTH(11, 30),
    FIFTH(13,30),
    SIXTH(14,40),
    SEVENTH(16,0),
    EIGHTH(17,20),
    NINTH(18,40),
    TENTH(20,0);

    val alarmTimeInMinutes = hour * 60 + minute

    companion object {
        fun getByOrdinal(ordinal: Int) = entries.first { it.ordinal == ordinal }

        fun getNextTime(currentAlarmTime: DayAlarmTime): DayAlarmTime {
            var stopOnNext = false

            for(entry in entries) {
                if(stopOnNext) return entry
                stopOnNext = entry.name == currentAlarmTime.name
            }

            return FIRST
        }

        fun getNextTime(currentHour: Int, currentMinute: Int): DayAlarmTime {
            val currentTimeInMinutes = currentHour * 60 + currentMinute
            entries.forEach {
                if(currentTimeInMinutes < it.alarmTimeInMinutes) return it
            }

            return FIRST
        }
    }
}

private fun epochMillisOf(hour: Int, minute: Int): Long {
    return Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, hour)
        set(Calendar.MINUTE, minute)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
}
package br.com.bruxismhelper.feature.alarm.data

import java.util.Calendar

enum class DayAlarmTime(val timeMillis: Long) {
    FIRST(epochMillisOf(8,0)),// 08:00
    SECOND(epochMillisOf(9,20)),// 09:20
    THIRD(epochMillisOf(10,40)),// 10:40
    FOURTH(epochMillisOf(11, 30)),// 11:30
    FIFTH(epochMillisOf(13,30)),// 13:30
    SIXTH(epochMillisOf(14,40)),// 14:40
    SEVENTH(epochMillisOf(16,0)),// 16:00
    EIGHTH(epochMillisOf(17,20)),// 17:20
    NINTH(epochMillisOf(18,40)),// 18:40
    TENTH(epochMillisOf(20,0));// 20:00

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

        fun getNextTime(currentTimeInMillis: Long): DayAlarmTime {
            entries.forEach {
                if(currentTimeInMillis < it.timeMillis) return it
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
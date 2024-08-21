package br.com.bruxismhelper.feature.alarm.data

import android.app.AlarmManager

sealed class AlarmType {
    class Exact: AlarmType()
    class Other: AlarmType()
    class Repeat(val interval: RepeatInterval): AlarmType() {
        enum class RepeatInterval(val intervalMillis: Long) {
            INTERVAL_DAY(AlarmManager.INTERVAL_DAY)
        }
    }
}
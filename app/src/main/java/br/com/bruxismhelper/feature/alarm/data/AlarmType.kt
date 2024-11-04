package br.com.bruxismhelper.feature.alarm.data

import android.app.AlarmManager

internal sealed class AlarmType {

    internal data object Exact : AlarmType()

    internal data object Default : AlarmType()

    internal class Repeat(val interval: RepeatInterval): AlarmType() {
        enum class RepeatInterval(val intervalMillis: Long) {
            INTERVAL_DAY(AlarmManager.INTERVAL_DAY)
        }
    }

    internal data object AlarmClock : AlarmType()
}
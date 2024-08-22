package br.com.bruxismhelper.feature.alarm

import br.com.bruxismhelper.feature.alarm.data.DayAlarmTime

interface AlarmSchedulerFacade {
    fun scheduleNextAlarm(currentAlarmTime: DayAlarmTime?)
}
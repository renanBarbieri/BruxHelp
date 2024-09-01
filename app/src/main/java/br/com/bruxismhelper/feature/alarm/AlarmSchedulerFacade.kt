package br.com.bruxismhelper.feature.alarm

interface AlarmSchedulerFacade {
    fun scheduleNextAlarm(currentAlarmId: Int?)
}
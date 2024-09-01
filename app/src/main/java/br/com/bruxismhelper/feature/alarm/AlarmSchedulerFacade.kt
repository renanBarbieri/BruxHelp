package br.com.bruxismhelper.feature.alarm

import java.util.Calendar

interface AlarmSchedulerFacade {
    fun scheduleNextAlarm(currentAlarmId: Int?, nowCalendar: Calendar = Calendar.getInstance())
}
package br.com.bruxismhelper.feature.alarm

import java.util.Calendar

interface AlarmSchedulerFacade {
    suspend fun scheduleNextAlarm(currentAlarmId: Int?, nowCalendar: Calendar = Calendar.getInstance())

    suspend fun getNextCalendarAlarmScheduled(): Calendar?
}
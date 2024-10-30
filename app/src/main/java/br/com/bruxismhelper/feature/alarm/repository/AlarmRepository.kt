package br.com.bruxismhelper.feature.alarm.repository

import br.com.bruxismhelper.feature.alarm.data.AlarmItem

internal interface AlarmRepository {
    suspend fun saveNextAlarmItem(alarmItem: AlarmItem)

    suspend fun getNextAlarmItem(): AlarmItem?
}


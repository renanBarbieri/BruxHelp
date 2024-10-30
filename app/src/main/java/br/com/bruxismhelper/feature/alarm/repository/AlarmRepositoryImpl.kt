package br.com.bruxismhelper.feature.alarm.repository

import br.com.bruxismhelper.feature.alarm.data.AlarmItem
import br.com.bruxismhelper.platform.store.DataStoreManager
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

private const val KEY_ALARM_TIME = "alarm_time"

internal class AlarmRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager
): AlarmRepository {

    override suspend fun saveNextAlarmItem(alarmItem: AlarmItem) {
        dataStoreManager.writeObject(KEY_ALARM_TIME, alarmItem)
    }

    override suspend fun getNextAlarmItem(): AlarmItem? {
        return dataStoreManager.readObject<AlarmItem>(KEY_ALARM_TIME).firstOrNull()
    }
}
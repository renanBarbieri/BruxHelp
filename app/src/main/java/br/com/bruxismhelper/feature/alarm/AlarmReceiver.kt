package br.com.bruxismhelper.feature.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.bruxismhelper.feature.navigation.repository.source.AppDataSource
import br.com.bruxismhelper.platform.firebase.logMessage
import br.com.bruxismhelper.platform.firebase.logNonFatalException
import br.com.bruxismhelper.platform.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

@AndroidEntryPoint
internal class AlarmReceiver : BroadcastReceiver() {

    @Inject lateinit var notificationHelper: NotificationHelper
    @Inject lateinit var appDataSource: AppDataSource
    @Inject lateinit var alarmScheduler: AlarmSchedulerFacade
    @Inject lateinit var alarmSchedulerHelper: AlarmSchedulerHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        logMessage { "AlarmReceiver.onReceive" }
        if (intent == null || context == null) {
            logMessage { "Intent or context is null" }
            return
        }

        try {
            val alarmItem = alarmSchedulerHelper.extractAlarmItemFromExtra(intent)
            if (alarmItem == null) {
                logMessage { "No alarm item found in intent" }
                return
            }

            logMessage { "Processing alarm: $alarmItem" }
            setAlarmFired(alarmItem.id)

            notificationHelper.sendNotification(
                id = 1234,
                context = context,
                title = alarmItem.title,
                body = alarmItem.message,
                notificationChannelProp = alarmItem.channelProp
            )
        } catch (e: Exception) {
            logNonFatalException { "Error processing alarm: ${e.message}" }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setAlarmFired(
        alarmId: Int,
        context: CoroutineContext = EmptyCoroutineContext
    ){
        val pendingResult = goAsync()

        GlobalScope.launch(context) {
            try {
                logMessage { "Scheduling next alarm after alarm id=$alarmId" }
                alarmScheduler.scheduleNextAlarm(alarmId)
                appDataSource.setAlarmFired(true)
            } catch (e: Exception) {
                logNonFatalException { "Error scheduling next alarm: ${e.message}" }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
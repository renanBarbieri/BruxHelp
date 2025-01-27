package br.com.bruxismhelper.feature.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import br.com.bruxismhelper.feature.navigation.repository.source.AppDataSource
import br.com.bruxismhelper.platform.notification.NotificationHelper
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import logcat.logcat
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
        logcat { "receiving" }
        if (intent == null || context == null) return

        alarmSchedulerHelper.extractAlarmItemFromExtra(intent)?.let {
            logcat { "alarm extracted: $it" }

            setAlarmFired(it.id)

            notificationHelper.sendNotification(
                id = 1234, //Same id for all notifications to update it, instead of create a new one
                context = context,
                title = it.title,
                body = it.message,
                notificationChannelProp = it.channelProp
            )
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setAlarmFired(
        alarmId: Int,
        context: CoroutineContext = EmptyCoroutineContext
    ){
        val pendingResult = goAsync()

        alarmScheduler.scheduleNextAlarm(alarmId)

        GlobalScope.launch(context) {
            appDataSource.setAlarmFired(true)
            pendingResult.finish()
        }
    }
}
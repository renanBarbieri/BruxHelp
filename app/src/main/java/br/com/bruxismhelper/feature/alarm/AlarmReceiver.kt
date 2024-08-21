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

    override fun onReceive(context: Context?, intent: Intent?) {
        logcat { "receiving" }
        if (intent == null || context == null) return

        AlarmSchedulerHelper.extractAlarmItemFromExtra(intent)?.let {
            logcat { "alarm extracted: $it" }

            setAlarmFired()

            notificationHelper.sendNotification(
                context = context,
                title = it.title,
                body = it.message,
                notificationChannelProp = it.channelProp
            )
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun setAlarmFired(context: CoroutineContext = EmptyCoroutineContext){
        val pendingResult = goAsync()

        GlobalScope.launch(context) {
            appDataSource.setAlarmFired(true)
            pendingResult.finish()
        }
    }
}
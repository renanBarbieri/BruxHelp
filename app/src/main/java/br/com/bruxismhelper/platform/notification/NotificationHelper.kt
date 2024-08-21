package br.com.bruxismhelper.platform.notification

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import br.com.bruxismhelper.R
import br.com.bruxismhelper.platform.notification.data.NotificationChannelProp
import javax.inject.Inject
import kotlin.random.Random

class NotificationHelper @Inject constructor() {

    /**
     * Send a push notification to user
     *
     * @param context The Context in which this notification should start the activity.
     * @param title Notification title
     * @param body Notification content text
     * @param extras Extra params to use when user clicks on notification
     * @param id An identifier for this notification unique within your application. Random integer by default.
     * @param requestCode Private request code for the sender. 0 by default
     * @param notificationChannelProp Channel properties for notification. For more details, see [NotificationChannelProp]
     */
    fun sendNotification(
        context: Context,
        title: String,
        body: String,
        extras: Map<String, String> = emptyMap(),
        id: Int = Random.nextInt(),
        requestCode: Int = 0,
        notificationChannelProp: NotificationChannelProp = NotificationChannelProp.DefaultChannelProp
    ) {
        val intent = context.buildIntentOrReturnNull(extras)
        val pendingIntent =
            PendingIntent.getActivity(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

        val notification = buildNotification(context, title, body, pendingIntent, notificationChannelProp)

        context.getNotificationManager(notificationChannelProp).notify(id, notification)
    }

    private fun Context.buildIntentOrReturnNull(extras: Map<String, String>): Intent? {
        return packageManager.getLaunchIntentForPackage(packageName)?.apply {
            extras.forEach { putExtra(it.key, it.value) }
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    private fun buildNotification(
        context: Context,
        title: String,
        body: String,
        pendingIntent: PendingIntent,
        notificationChannelProp: NotificationChannelProp
    ): Notification {
        return NotificationCompat.Builder(
            context,
            notificationChannelProp.appChannel.channelId.name
        ).apply {
            setContentTitle(title)
            setContentText(body)
            setAutoCancel(true)
            setContentIntent(pendingIntent)

            setSmallIcon(R.drawable.ic_push_notification)
            setLargeIcon(
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.ic_push_notification
                )
            )
            color = ContextCompat.getColor(context, R.color.push_notification_background)
        }.build()
    }

    @SuppressLint("NewApi")
    private fun Context.getNotificationManager(notificationChannelProp: NotificationChannelProp): NotificationManager {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createChannel(notificationChannelProp,this))
        }

        return notificationManager
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel(
        notificationChannelProp: NotificationChannelProp,
        context: Context
    ): NotificationChannel =
        NotificationChannel(
            notificationChannelProp.appChannel.channelId.name,
            context.getString(notificationChannelProp.appChannel.channelName),
            notificationChannelProp.importance
        ).apply {
            description = context.getString(notificationChannelProp.appChannel.description)
        }

}




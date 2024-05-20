package br.com.bruxismhelper.platform.notification.data

import android.app.NotificationManager
import java.io.Serializable

data class NotificationChannelProp(
    val appChannel: AppChannel,
    val importance: Int
): Serializable {
    companion object {
        val DefaultChannelProp = NotificationChannelProp(
            AppChannel.DEFAULT,
            NotificationManager.IMPORTANCE_DEFAULT
        )
    }
}
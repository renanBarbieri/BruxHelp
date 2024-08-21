package br.com.bruxismhelper.feature.alarm.data

import br.com.bruxismhelper.platform.notification.data.NotificationChannelProp
import com.google.gson.annotations.Expose
import java.io.Serializable
import kotlin.random.Random

data class AlarmItem(
    @Expose val id: Int = Random.nextInt(),
    @Expose val channelProp: NotificationChannelProp,
    @Expose val timeInMillis: Long,
    @Expose val title: String,
    @Expose val message: String = "",
) : Serializable
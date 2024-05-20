package br.com.bruxismhelper.platform.notification.data

import androidx.annotation.StringRes
import br.com.bruxismhelper.R

enum class AppChannel(
    val channelId: ChannelID,
    @StringRes val channelName: Int,
    @StringRes val description: Int
) {
    DEFAULT(
        ChannelID.DEFAULT,
        R.string.notification_channel_name_default,
        R.string.notification_channel_description_default
    ),
    BRUXISM(
        ChannelID.BRUXISM,
        R.string.notification_channel_name_bruxism,
        R.string.notification_channel_description_bruxism
    );

    enum class ChannelID {
        DEFAULT,
        BRUXISM;

        companion object {
            fun fromString(channelID: String): ChannelID? {
                return entries.firstOrNull() { it.name == channelID }
            }
        }
    }
}
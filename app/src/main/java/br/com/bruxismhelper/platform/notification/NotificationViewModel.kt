package br.com.bruxismhelper.platform.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bruxismhelper.platform.notification.data.AppChannel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor() : ViewModel() {
    private val apiGreaterThanTiramisu = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    /**
     * represents the denied state of notification permission
     */
    val notificationViewEvent = MutableStateFlow(NotificationViewEvent())

    /**
     * Checks app notification permission status and features notification channels status.
     * If some of it is disabled or muted, posts an event on [notificationViewEvent] with respective status
     *
     * @param areNotificationDisabled System status of app notification permission
     * @param shouldShowNotificationRationale System status of rationale exhibition
     * @param notificationChannels All channels registered on system
     */
    fun checkNotificationStatus(
        areNotificationDisabled: Boolean,
        shouldShowNotificationRationale: Boolean,
        notificationChannels: List<NotificationChannel>?
    ) {
        viewModelScope.launch {
            val featureChannelBlocked = getFeatureChannelBlocked(notificationChannels)

            if (featureChannelBlocked != null) {
                notificationViewEvent.emit(
                    NotificationViewEvent(
                        NotificationFlowState.DENIED_REDIRECT_TO_SETTINGS,
                        featureChannelBlocked
                    )
                )
            } else if (areNotificationDisabled && shouldShowNotificationRationale) {
                notificationViewEvent.emit(
                    NotificationViewEvent(NotificationFlowState.DENIED_SHOW_EXPLANATION)
                )
            }
        }
    }

    /**
     * Verifies the cause of denied post notification and posts an event on [notificationViewEvent] with respective status
     *
     * @param shouldShowNotificationRationale System status of rationale exhibition
     * @param whitebookChannel Feature respective channel
     */
    fun verifyDeniedReasonForFeature(
        shouldShowNotificationRationale: Boolean,
        whitebookChannel: AppChannel
    ) {
        viewModelScope.launch {
            if(whitebookChannel != AppChannel.DEFAULT) {
                notificationViewEvent.emit(
                    NotificationViewEvent(
                        NotificationFlowState.DENIED_FEATURE_DEPENDENCY,
                        whitebookChannel.channelId
                    )
                )
            } else {
                checkRationaleDisplayStatus(shouldShowNotificationRationale)
            }
        }
    }

    fun clearNotificationViewEvent() {
        viewModelScope.launch {
            notificationViewEvent.emit(
                NotificationViewEvent(
                    state = NotificationFlowState.NONE
                )
            )
        }
    }

    private suspend fun checkRationaleDisplayStatus(hasShowedRationale: Boolean) {
        viewModelScope.launch {
            if (apiGreaterThanTiramisu) {
                if (hasShowedRationale.not()) {
                    notificationViewEvent.emit(
                        NotificationViewEvent(NotificationFlowState.DENIED_SHOW_EXPLANATION)
                    )
                } else {
                    notificationViewEvent.emit(
                        NotificationViewEvent(NotificationFlowState.DENIED_REDIRECT_TO_SETTINGS)
                    )
                }
            } else {
                notificationViewEvent.emit(
                    NotificationViewEvent(NotificationFlowState.DENIED_SHOW_EXPLANATION)
                )
            }
        }
    }

    private fun getFeatureChannelBlocked(channels: List<NotificationChannel>?): AppChannel.ChannelID? {
        val legacyDefaultChannel = "default-channel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channels?.forEach {
                if ((it.name != AppChannel.ChannelID.DEFAULT.name || it.name != legacyDefaultChannel)
                    && it.importance == NotificationManager.IMPORTANCE_NONE
                ) {
                    return AppChannel.ChannelID.fromString(it.id)
                }
            }
        }

        return null
    }
}

data class NotificationViewEvent(
    val state: NotificationFlowState = NotificationFlowState.NONE,
    val channelBlocked: AppChannel.ChannelID? = null
)

enum class NotificationFlowState {
    DENIED_SHOW_EXPLANATION,
    DENIED_REDIRECT_TO_SETTINGS,
    DENIED_FEATURE_DEPENDENCY,
    NONE
}
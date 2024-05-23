package br.com.bruxismhelper.platform.notification

import android.Manifest
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.window.DialogProperties
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import br.com.bruxismhelper.R
import br.com.bruxismhelper.platform.notification.data.AppChannel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
private const val PERMISSION_POST_NOTIFICATIONS = Manifest.permission.POST_NOTIFICATIONS
private const val APP_PACKAGE = "app_package"
private const val APP_UID = "app_uid"
private const val PROVIDER_APP_PACKAGE = "android.provider.extra.APP_PACKAGE"
private const val APP_NOTIFICATION_SETTINGS = "android.settings.APP_NOTIFICATION_SETTINGS"

class NotificationPermissionCheckerHelper @Inject constructor(){
    private var activityResultLauncher: ActivityResultLauncher<String>? = null
    private var _activity: ComponentActivity? = null
    private var _fragment: Fragment? = null

    private var _grantedBlock: (() -> Unit)? = null
    private var _deniedBlock: (() -> Unit)? = null

    private var _viewModel: NotificationViewModel? = null
    private var shouldShowNotificationRationale: Boolean = false

    private var notificationViewEvent: State<NotificationViewEvent>? = null

    //region REGISTER FOR RESULT

    /**
     * Register the [activity] to get the result of notification request permission.
     * If user allows to send notification, [grantedBlock] will be invoked. Otherwise [deniedBlock] will be invoked
     *
     * @param activity Owner of permission request
     * @param grantedBlock Function to be invoked when user allows to send notifications
     * @param deniedBlock Function to be invoked when user denies to send notifications
     */
    fun registerForResult(
        activity: ComponentActivity,
        grantedBlock: (() -> Unit)? = null,
        deniedBlock: (() -> Unit)? = null
    ){
        _activity = activity
        activityResultLauncher =
            registerForNotificationPermissionResult(activity, grantedBlock, deniedBlock)

        _grantedBlock = grantedBlock
        _deniedBlock = deniedBlock
    }


    fun registerForResult(
        fragment: Fragment, grantedBlock: (() -> Unit)? = null, deniedBlock: (() -> Unit)? = null
    ){
        _fragment = fragment
        activityResultLauncher =
            registerForNotificationPermissionResult(fragment, grantedBlock, deniedBlock)

        _grantedBlock = grantedBlock
        _deniedBlock = deniedBlock
    }

    private fun registerForNotificationPermissionResult(
        activity: ComponentActivity,
        grantedBlock: (() -> Unit)? = null,
        deniedBlock: (() -> Unit)? = null
    ): ActivityResultLauncher<String> {
        return activity.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            buildActivityResultNotificationCallback(grantedBlock, deniedBlock)
        )
    }

    private fun registerForNotificationPermissionResult(
        fragment: Fragment,
        grantedBlock: (() -> Unit)? = null,
        deniedBlock: (() -> Unit)? = null
    ): ActivityResultLauncher<String> {
        return fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            buildActivityResultNotificationCallback(grantedBlock, deniedBlock)
        )
    }

    private fun buildActivityResultNotificationCallback(
        grantedBlock: (() -> Unit)? = null,
        deniedBlock: (() -> Unit)? = null
    ): ActivityResultCallback<Boolean> {
        return ActivityResultCallback { isGranted ->
            if (isGranted) {
                grantedBlock?.invoke()
            } else {
                deniedBlock?.invoke()
            }
        }
    }

    @Composable
    fun composeInit(): NotificationPermissionCheckerHelper {
        _viewModel = hiltViewModel<NotificationViewModel>()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            shouldShowNotificationRationale = _activity?.shouldShowNotificationRationale()
                ?: _fragment?.shouldShowNotificationRationale() ?: false
        }

        return this
    }
    //endregion

    /**
     * Verifies if user has allowed app to send notifications.
     * Also verifies if user has muted channels that wasn't for general purpose.
     *
     * If notifications are disabled or some non-general channel are muted,
     * an Alert will be shown to conduce user to allow this behavior.
     *
     * Is possible that the runtime permission be called or user be redirected to
     * notification setting. It depends of Android response of shouldShowRequestPermissionRationale
     *
     * If user is redirected to setting, a deny button will be shown in thia Alert and it's action,
     * the deniedBlock, will be invoked
     *
     * To see witch alerts could be shown, look at [handleNotificationFlowStateEvent] implementation
     *
     * @throws NullPointerException if you not call [registerForResult] before
     */
    fun checkNotificationStatus() {
        val notificationChannels = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            (getContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).notificationChannels
        } else {
            null
        }
        _viewModel?.checkNotificationStatus(
            areNotificationDisabled(),
            shouldShowNotificationRationale,
            notificationChannels
        )
    }

    /**
     * Invokes grantedBlock, configured at registerForResult,
     * if app has permission to send notification and the [appChannel] is enabled.
     * Otherwise, an Alert will be shown to conduce user to allow this notification channel.
     *
     * Is possible that the runtime permission be called or user be redirected to
     * notification setting. It depends of Android response of shouldShowRequestPermissionRationale
     *
     * If user is redirected to setting, a deny button will be shown in thia Alert and it's action,
     * the deniedBlock, will be invoked
     *
     * @param appChannel WhitebookChannel that needs to be enabled
     * @throws NullPointerException if you not call [registerForResult] before
     */
    fun runGrantedBlockOrRequestPermission(
        appChannel: AppChannel = AppChannel.DEFAULT
    ) {
        if (areNotificationDisabled() || appChannel.isChannelDisabled()) {
            _viewModel?.verifyDeniedReasonForFeature(
                shouldShowNotificationRationale,
                appChannel
            )
        } else {
            _grantedBlock?.invoke()
        }
    }

    @Composable
    fun NotificationAlert() {
        val notificationFlowState = _viewModel?.notificationViewEvent?.collectAsState(
            initial = NotificationViewEvent()
        )

        when (notificationFlowState?.value?.state) {
            NotificationFlowState.DENIED_SHOW_EXPLANATION -> {
                NotificationPermissionExplanation(
                    onCloseRequest = { _viewModel?.clearNotificationViewEvent() },
                    R.string.notification_alert_title_new_install,
                    R.string.notification_alert_message_new_install
                )
            }

            NotificationFlowState.DENIED_FEATURE_DEPENDENCY -> {
                RedirectToNotificationSettingAlert(
                    onCloseRequest = { _viewModel?.clearNotificationViewEvent() },
                    R.string.notification_alert_title_feature_request,
                    getFeatureMessage(notificationViewEvent!!.value.channelBlocked),
                    showDenyButton = true
                )
            }

            NotificationFlowState.DENIED_REDIRECT_TO_SETTINGS -> {
                RedirectToNotificationSettingAlert(
                    onCloseRequest = { _viewModel?.clearNotificationViewEvent() },
                    R.string.notification_alert_title_feature_dependency,
                    getContext().getString(R.string.notification_alert_message_feature_dependency),
                    showDenyButton = false
                )
            }
            else -> {
                /* DIALOG NOT SHOWN */
                Log.d(this::class.java.name, "Nothing to show")
            }
        }
    }

    private fun getFeatureMessage(channelID: AppChannel.ChannelID?): String {
        val featureName = when (channelID) {
            AppChannel.ChannelID.BRUXISM -> getContext().getString(R.string.notification_alert_message_feature_bruxism)
            else -> null
        }

        return getContext().getString(
            R.string.notification_alert_message_feature_request,
            featureName
        )
    }

    private fun areNotificationDisabled(): Boolean {
        val context = getContext()
        return NotificationManagerCompat.from(context).areNotificationsEnabled().not()
    }

    private fun AppChannel.isChannelDisabled(): Boolean {
        return NotificationManagerCompat.from(getContext())
            .getNotificationChannelCompat(channelId.name)?.importance == NotificationManagerCompat.IMPORTANCE_NONE
    }

    //region ALERTS
    @Composable
    private fun NotificationPermissionExplanation(
        onCloseRequest: () -> Unit,
        @StringRes title: Int,
        @StringRes explanation: Int
    ) {
        AlertDialog(
            title = {
                Text(text = stringResource(id = title))
            },
            text = {
                Text(text = stringResource(id = explanation))
            },
            onDismissRequest = {
                onCloseRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            launchPermissionPostNotification()
                        }
                        onCloseRequest()
                    }
                ) {
                    Text(text = stringResource(id = R.string.activate))
                }
            },
            properties = DialogProperties(dismissOnClickOutside = false)
        )
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun launchPermissionPostNotification() {
        activityResultLauncher?.launch(PERMISSION_POST_NOTIFICATIONS) ?: run {
            throw NullPointerException("ActivityResultLauncher not found. Did you call registerForResult function?")
        }
    }

    @Composable
    private fun RedirectToNotificationSettingAlert(
        onCloseRequest: () -> Unit,
        @StringRes title: Int,
        explanation: String,
        showDenyButton: Boolean,
    ) {
        AlertDialog(
            title = {
                    Text(text = stringResource(id = title))
            },
            text = {
                   Text(text = explanation)
            },
            onDismissRequest = {
                onCloseRequest()
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        this@NotificationPermissionCheckerHelper.getContext()
                            .startActivity(buildSettingsNotificationIntent())
                        onCloseRequest()
                    }
                ) {
                    Text(text = stringResource(id = R.string.activate))
                }
            },
            dismissButton = {
                if(showDenyButton) {
                    TextButton(
                        onClick = {
                            _deniedBlock?.invoke()
                            onCloseRequest()
                        }
                    ) {
                        Text(text = stringResource(id = R.string.ignore))
                    }
                }
            }
        )
    }
    //endregion

    //region NOTIFICATION INTENT
    private fun buildSettingsNotificationIntent(): Intent {
        return Intent().apply {
            action = getNotificationIntentAction()
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            putSettingsNotificationExtras()
        }
    }

    private fun Intent.putSettingsNotificationExtras() {
        //for Android 5-7
        putExtra(APP_PACKAGE, getContext().packageName)
        putExtra(APP_UID, getContext().applicationInfo.uid)
        // for Android 8 and above
        putExtra(PROVIDER_APP_PACKAGE, getContext().packageName)
    }

    private fun getNotificationIntentAction(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            Settings.ACTION_APP_NOTIFICATION_SETTINGS
        else
            APP_NOTIFICATION_SETTINGS
    }
    //endregion

    //region SHOULD_SHOW_NOTIFICATION_RATIONALE EXTENSION
    /**
     * Verifies if it's necessary to show rationale for notification.
     * When application runs for the first time, this function will return false
     *
     * @return true if it's necessary to show notification rationale
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun ComponentActivity.shouldShowNotificationRationale(): Boolean {
        return this.shouldShowRequestPermissionRationale(PERMISSION_POST_NOTIFICATIONS).not()
    }

    /**
     * Verifies if it's necessary to show rationale for notification.
     * When application runs for the first time, this function will return false
     *
     * @return true if it's necessary to show notification rationale
     */
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun Fragment.shouldShowNotificationRationale(): Boolean {
        return this.shouldShowRequestPermissionRationale(PERMISSION_POST_NOTIFICATIONS).not()
    }
    //endregion

    @Throws(NullPointerException::class)
    private fun getContext(): Context {
        if (_activity == null && _fragment == null) {
            throw NullPointerException("Fragment and Activity are null. Its not possible to find context")
        } else {
            return _activity ?: _fragment!!.requireContext()
        }
    }
}
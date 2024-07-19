package br.com.bruxismhelper

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import br.com.bruxismhelper.feature.navigation.NavigationHost
import br.com.bruxismhelper.platform.notification.NotificationPermissionCheckerHelper
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : ComponentActivity() {
    @Inject
    lateinit var notificationPermissionCheckerHelper: NotificationPermissionCheckerHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        notificationPermissionCheckerHelper.registerForResult(
            activity = this
        )

        setContent {
            notificationPermissionCheckerHelper.composeInit().checkNotificationStatus()
            App(
                notificationAlert = { notificationPermissionCheckerHelper.NotificationAlert() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App(notificationAlert: (@Composable () -> Unit)? = null) {
    val appBarTitle: MutableIntState = remember { mutableIntStateOf(R.string.app_name) }

    BruxismHelperTheme {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = appBarTitle.intValue),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    },
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    )
                )
            }
        ) {
            Column(
                modifier = Modifier
                    .padding(it)
                    .background(MaterialTheme.colorScheme.background)
            ) {
                notificationAlert?.invoke()
                NavigationHost(appBarTitle)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppPreview() {
    App()
}
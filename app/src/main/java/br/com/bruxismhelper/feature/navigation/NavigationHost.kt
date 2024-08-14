package br.com.bruxismhelper.feature.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.register.presentation.ui.RegisterForm
import br.com.bruxismhelper.feature.registerBruxism.presentation.ui.RegisterBruxismForm
import br.com.bruxismhelper.feature.waiting.ui.WaitingScreen
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun NavigationHost(appBarTitle: MutableIntState = mutableIntStateOf(R.string.app_name)) {
    val navController = rememberNavController()
    NavHost(
        startDestination = AppRoute.Waiting,
        navController = navController,
    ) {
        composable(route = AppRoute.Waiting) {
            WaitingScreen()
        }
        composable(route = AppRoute.Register) {
            RegisterForm(
                modifier = Modifier.padding(start = 26.dp, end = 26.dp),
                appBarTitle = appBarTitle,
                onRegistrationFinished = { navController.navigate(AppRoute.BruxismRegister) },
                onRegistrationIgnored = { navController.navigate(AppRoute.Waiting) },
            )
        }
        composable(route = AppRoute.BruxismRegister) {
            RegisterBruxismForm(
                appBarTitle = appBarTitle,
                onActivityRegistrationFinished = {
                    navController.navigate(AppRoute.Waiting)
                }
            )
        }

        //TODO CREATE SUCCESS SCREEN
    }
}

@Preview(showBackground = true)
@Composable
private fun NavPreview() {
    BruxismHelperTheme {
        NavigationHost()
    }
}
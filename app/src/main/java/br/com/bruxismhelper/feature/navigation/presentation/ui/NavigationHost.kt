package br.com.bruxismhelper.feature.navigation.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.navigation.presentation.model.AppRoute
import br.com.bruxismhelper.feature.navigation.presentation.NavigationViewModel
import br.com.bruxismhelper.feature.register.presentation.ui.RegisterForm
import br.com.bruxismhelper.feature.registerBruxism.presentation.ui.RegisterBruxismForm
import br.com.bruxismhelper.feature.waiting.ui.WaitingScreen

@Composable
fun NavigationHost(
    appBarTitle: MutableIntState = mutableIntStateOf(R.string.app_name),
    viewModel: NavigationViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    NavStateRoute(viewModel = viewModel, navController = navController)

    NavHost(
        startDestination = AppRoute.Splash,
        navController = navController,
    ) {
        composable(route = AppRoute.Splash) {
            WaitingScreen(messageStringRes = R.string.splash_message)
        }

        composable(route = AppRoute.Waiting) {
            WaitingScreen(messageStringRes = R.string.waiting_message)
        }

        composable(route = AppRoute.Register) {
            RegisterForm(
                modifier = Modifier.padding(start = 26.dp, end = 26.dp),
                appBarTitle = appBarTitle,
                onRegistrationFinished = {
                    navController.navigate(AppRoute.BruxismRegister) {
                        viewModel.setRegisterScreenShown()
                        popUpTo(AppRoute.Register) { inclusive = true }
                    }
                },
                onRegistrationIgnored = {
                    navController.navigate(AppRoute.Waiting) {
                        viewModel.setRegisterScreenShown()
                        popUpTo(AppRoute.Register) { inclusive = true }
                    }
                },
            )
        }

        composable(route = AppRoute.BruxismRegister) {
            RegisterBruxismForm(
                appBarTitle = appBarTitle,
                onActivityRegistrationFinished = {
                    navController.navigate(AppRoute.Waiting) {
                        popUpTo(AppRoute.BruxismRegister) { inclusive = true }
                    }
                }
            )
        }

        //TODO CREATE SUCCESS SCREEN
    }
}

@Composable
private fun NavStateRoute(
    viewModel: NavigationViewModel,
    navController: NavHostController
) {
    val routeState by viewModel.viewState.collectAsState()

    LaunchedEffect(routeState) {
        navController.navigate(routeState) { launchSingleTop = true }
    }
}
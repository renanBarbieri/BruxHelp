package br.com.bruxismhelper.feature.navigation.presentation.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.agreement.presentation.ui.AgreementScreen
import br.com.bruxismhelper.feature.idle.ui.IdleScreen
import br.com.bruxismhelper.feature.idle.ui.waiting.WaitingIcon
import br.com.bruxismhelper.feature.navigation.presentation.NavigationViewModel
import br.com.bruxismhelper.feature.navigation.presentation.model.AppRoute
import br.com.bruxismhelper.feature.register.presentation.ui.RegisterForm
import br.com.bruxismhelper.feature.registerBruxism.presentation.ui.RegisterBruxismForm

@Composable
fun NavigationHost(
    appBarTitle: MutableIntState = mutableIntStateOf(R.string.app_name),
    forceResponse: MutableState<Boolean> = mutableStateOf(false),
    showForceIcon: MutableState<Boolean> = mutableStateOf(false),
    viewModel: NavigationViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
) {
    NavStateRoute(viewModel = viewModel, navController = navController, forceResponse = forceResponse)

    NavHost(
        startDestination = AppRoute.Splash,
        navController = navController,
    ) {
        composable(route = AppRoute.Splash) {
            appBarTitle.intValue = R.string.empty
            IdleScreen(
                centerIcon = { WaitingIcon() },
                messageStringRes = R.string.loading
            )
        }

        composable(route = AppRoute.Agreement) {
            appBarTitle.intValue = R.string.agreement_title
            AgreementScreen(onAgreed = {
                viewModel.setTermShown()
            })
        }

        composable(route = AppRoute.Waiting) {
            appBarTitle.intValue = R.string.app_name
            showForceIcon.value = true
            IdleScreen(
                centerIcon = { WaitingIcon() },
                messageStringRes = R.string.waiting_message
            )
        }

        composable(route = AppRoute.Register) {
            appBarTitle.intValue = R.string.register_title
            RegisterForm(
                modifier = Modifier.padding(start = 26.dp, end = 26.dp),
                onRegistrationFinished = { viewModel.setRegisterScreenShown() },
                onRegistrationIgnored = { viewModel.setRegisterScreenShown() },
            )
        }

        composable(route = AppRoute.BruxismRegister) {
            appBarTitle.intValue = R.string.register_bruxism_title
            showForceIcon.value = false
            RegisterBruxismForm(
                onActivityRegistrationFinished = {
                    forceResponse.value = false
                    viewModel.setBruxismFormAnswered()
                }
            )
        }
    }
}

@Composable
private fun NavStateRoute(
    viewModel: NavigationViewModel,
    navController: NavHostController,
    forceResponse: MutableState<Boolean> = mutableStateOf(false),
) {
    if(forceResponse.value) {
        navController.navigate(AppRoute.BruxismRegister) {
            launchSingleTop = true
            popUpToTop(navController)
        }
    } else {
        val routeState by viewModel.viewState.collectAsState()

        LaunchedEffect(routeState) {
            navController.navigate(routeState) {
                launchSingleTop = true
                popUpToTop(navController)
            }
        }
    }

}
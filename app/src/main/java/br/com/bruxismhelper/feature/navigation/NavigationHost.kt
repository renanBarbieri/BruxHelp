package br.com.bruxismhelper.feature.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import br.com.bruxismhelper.MainScreen
import br.com.bruxismhelper.R
import br.com.bruxismhelper.feature.register.RegisterForm
import br.com.bruxismhelper.feature.registerBruxism.RegisterBruxism
import br.com.bruxismhelper.ui.theme.BruxismHelperTheme

@Composable
fun NavigationHost(appBarTitle: MutableIntState = mutableIntStateOf(R.string.app_name)) {
    val navController = rememberNavController()
    NavHost(
        startDestination = AppRoute.Main,
        navController = navController,
    ) {
        composable(route = AppRoute.Main) {
            MainScreen(
                isRegistered = false, //TODO get register from db
                onNewUser = { navController.navigate(AppRoute.Register) },
                onUserRegistered = { navController.navigate(AppRoute.BruxismRegister) }
            )
        }
        composable(route = AppRoute.Register) {
            RegisterForm(
                modifier = Modifier.padding(26.dp),
                onRegistrationFinished = { navController.navigate(AppRoute.BruxismRegister) })
        }
        composable(route = AppRoute.BruxismRegister) {
            RegisterBruxism(onActivityRegistrationFinished = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NavPreview() {
    BruxismHelperTheme {
        NavigationHost()
    }
}
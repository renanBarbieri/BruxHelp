package br.com.bruxismhelper.feature.navigation.presentation.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.PopUpToBuilder
import androidx.navigation.compose.composable
import br.com.bruxismhelper.feature.navigation.presentation.model.AppRoute

internal fun NavController.navigate(
    route: AppRoute,
    builder: NavOptionsBuilder.() -> Unit = {}
) {
    navigate(route.name, builder)
}

internal fun NavOptionsBuilder.popUpTo(route: AppRoute, popUpToBuilder: PopUpToBuilder.() -> Unit = {}) {
    popUpTo(route.name, popUpToBuilder)
}

internal fun NavGraphBuilder.composable(
    route: AppRoute,
    arguments: List<NamedNavArgument> = emptyList(),
    deepLinks: List<NavDeepLink> = emptyList(),
    content: @Composable (NavBackStackEntry) -> Unit
) {
    composable(route.name, arguments, deepLinks, content)
}

@Composable
internal fun NavHost(
    navController: NavHostController,
    startDestination: AppRoute,
    modifier: Modifier = Modifier,
    route: String? = null,
    builder: NavGraphBuilder.() -> Unit
) {
    androidx.navigation.compose.NavHost(
        navController,
        startDestination.name,
        modifier,
        route,
        builder
    )
}
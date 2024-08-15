package br.com.bruxismhelper.feature.navigation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationRepository: NavigationRepository,
): ViewModel() {

    private val _viewState = MutableStateFlow(AppRoute.Splash)
    internal val viewState: StateFlow<AppRoute> = _viewState

    init {
        val isRegisterScreenShown = navigationRepository.isRegisterScreenShown()
        val isAlarmFired = navigationRepository.isAlarmFired()

        val newRoute = when {
            !isRegisterScreenShown -> AppRoute.Register
            isRegisterScreenShown && isAlarmFired -> AppRoute.BruxismRegister
            else -> AppRoute.Waiting
        }

        _viewState.update { newRoute }
    }

    fun setRegisterScreenShown() {
        navigationRepository.setRegisterScreenShown()
    }
}

interface NavigationRepository {
    fun setRegisterScreenShown()
    fun isRegisterScreenShown(): Boolean
    fun isAlarmFired(): Boolean
}
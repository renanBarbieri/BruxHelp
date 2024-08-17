package br.com.bruxismhelper.feature.navigation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bruxismhelper.feature.navigation.domain.repository.NavigationRepository
import br.com.bruxismhelper.feature.navigation.presentation.model.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationRepository: NavigationRepository,
): ViewModel() {

    private val _viewState = MutableStateFlow(AppRoute.Splash)
    internal val viewState: StateFlow<AppRoute> = _viewState

    init {
        viewModelScope.launch {
            val isRegisterScreenShownEvent = navigationRepository.isRegisterScreenShown()
            val isAlarmFiredEvent = navigationRepository.isAlarmFired()

            calculateRoute(isRegisterScreenShownEvent, isAlarmFiredEvent)
        }
    }

    private fun calculateRoute(isRegisterScreenShown: Boolean, isAlarmFired: Boolean) {
        val newRoute = when {
            !isRegisterScreenShown -> AppRoute.Register
            isRegisterScreenShown && isAlarmFired -> AppRoute.BruxismRegister
            else -> AppRoute.Waiting
        }

        _viewState.update { newRoute }
    }

    fun setRegisterScreenShown() {
        viewModelScope.launch {
            navigationRepository.setRegisterScreenShown()
        }
    }
}
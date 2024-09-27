package br.com.bruxismhelper.feature.navigation.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bruxismhelper.feature.navigation.domain.repository.NavigationRepository
import br.com.bruxismhelper.feature.navigation.presentation.model.AppRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val navigationRepository: NavigationRepository,
) : ViewModel() {

    private val _viewState = MutableStateFlow(AppRoute.Splash)
    internal val viewState: StateFlow<AppRoute> = _viewState

    init {
        viewModelScope.launch {
            val isRegisterShowFlow = navigationRepository.isRegisterScreenShown()
            val isAlarmFiredFlow = navigationRepository.isAlarmFired()
            val isTermAgreedFlow = navigationRepository.isAgreementScreenShown()

            combine(
                isRegisterShowFlow,
                isAlarmFiredFlow,
                isTermAgreedFlow
            ) { isRegisterScreenShown, isAlarmFired, _ ->
                calculateRoute(isRegisterScreenShown, isAlarmFired, true)
            }.collect { newRoute ->
                _viewState.update { newRoute }
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun calculateRoute(
        isRegisterScreenShown: Boolean,
        isAlarmFired: Boolean,
        isTermAgreed: Boolean
    ): AppRoute {
        return when {
            !isTermAgreed -> AppRoute.Agreement
            !isRegisterScreenShown -> AppRoute.Register
            isRegisterScreenShown && isAlarmFired -> AppRoute.BruxismRegister
            else -> AppRoute.Waiting
        }
    }

    fun setRegisterScreenShown() {
        viewModelScope.launch {
            navigationRepository.setRegisterScreenShown()
        }
    }

    fun setBruxismFormAnswered() {
        viewModelScope.launch {
            navigationRepository.setBruxismFormAnswered()
        }
    }

    fun setTermShown() {
        viewModelScope.launch {
            navigationRepository.setAgreementScreenShown()
        }
    }
}
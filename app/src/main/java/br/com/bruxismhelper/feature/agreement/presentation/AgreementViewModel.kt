package br.com.bruxismhelper.feature.agreement.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.bruxismhelper.feature.agreement.domain.repository.AgreementRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AgreementViewModel @Inject constructor(
    val repository: AgreementRepository
): ViewModel() {

    fun onAgree() {
        viewModelScope.launch {
            repository.setAsAgreed()
        }
    }
}
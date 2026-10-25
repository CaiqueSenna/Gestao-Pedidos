package com.caiquesenna.gestaopedidos.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.caiquesenna.gestaopedidos.domain.usecase.dashboard.ObservarResumoUseCase
import com.caiquesenna.gestaopedidos.domain.usecase.dashboard.ResumoDashboard
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    observarResumo: ObservarResumoUseCase
) : ViewModel(){

    val resumo: StateFlow<ResumoDashboard> = observarResumo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ResumoDashboard()
        )
}
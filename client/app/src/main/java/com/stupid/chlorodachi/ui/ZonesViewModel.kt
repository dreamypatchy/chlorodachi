package com.stupid.chlorodachi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.stupid.chlorodachi.data.Datasource
import com.stupid.chlorodachi.model.Zone
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ZonesViewModel (
    private val datasource: Datasource
) : ViewModel() {
    private val _uiState = MutableStateFlow(ZonesUiState())
    val uiState: StateFlow<ZonesUiState> = _uiState.asStateFlow()

    private var loadedZones: List<Zone> = emptyList()

    init {
        loadZones()
    }

    private fun loadZones() {
        viewModelScope.launch {
            val zones = withContext(Dispatchers.Default) {
                datasource.loadDemoZones()
            }
            loadedZones = zones
            _uiState.update { it.copy(isLoading = false, zones = zones.map(Zone::toUiModel)) }
        }
    }

    fun formStateFor(zoneId: String): ZoneFormUiState? =
        loadedZones.firstOrNull { it.id == zoneId }?.toFormState()

    companion object {
        val Factory = viewModelFactory {
            initializer { ZonesViewModel(Datasource()) }
        }
    }
}
package com.stupid.chlorodachi.ui

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

open class ZoneFormViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ZoneFormUiState())
    val uiState: StateFlow<ZoneFormUiState> = _uiState.asStateFlow()

    fun setForm(state: ZoneFormUiState) { _uiState.value = state }

    fun onNameChange(value: String) = _uiState.update { it.copy(name = value) }
    fun onPlantChange(value: String) = _uiState.update { it.copy(plant = value) }
    fun onXChange(value: Double) = _uiState.update { it.copy(x = value) }
    fun onYChange(value: Double) = _uiState.update { it.copy(y = value) }
    fun onWChange(value: Double) = _uiState.update { it.copy(w = value) }
    fun onHChange(value: Double) = _uiState.update { it.copy(h = value) }
    fun onMinIlluminanceChange(value: Double) = _uiState.update { it.copy(minIlluminance = value) }
    fun onMaxIlluminanceChange(value: Double) = _uiState.update { it.copy(maxIlluminance = value) }
    fun onValidTemperatureChange(value: ClosedFloatingPointRange<Double>) = _uiState.update { it.copy(validTemperature = value) }
    fun onValidHumidityChange(value: ClosedFloatingPointRange<Double>) = _uiState.update { it.copy(validHumidity = value) }
    fun onValidMoistureChange(value: ClosedFloatingPointRange<Double>) = _uiState.update { it.copy(validMoisture = value) }
}

class ZoneAddViewModel : ZoneFormViewModel()

class ZoneEditViewModel : ZoneFormViewModel() {
    private val _editingZoneId = MutableStateFlow<String?>(null)
    val editingZoneId: StateFlow<String?> = _editingZoneId.asStateFlow()

    fun start(zoneId: String, form: ZoneFormUiState) {
        setForm(form)
        _editingZoneId.value = zoneId
    }

    fun stop() {
        _editingZoneId.value = null
    }
}
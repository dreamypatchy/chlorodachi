package com.stupid.chlorodachi.ui

data class DashboardUiState(
    val isLoading: Boolean = true,
    val zones: List<ZoneUiModel> = emptyList()
)

enum class SoilStatus { DRY, MOIST, WET, UNKNOWN }

data class ZoneUiModel(
    val id: String,
    val name: String,
    val gridX: Int,
    val gridY: Int,
    val gridWidth: Int,
    val gridHeight: Int,
    val temperature: Double?,
    val humidity: Double?,
    val illuminance: Double?,
    val soilStatus: SoilStatus,
    val needsAttention: Boolean
)
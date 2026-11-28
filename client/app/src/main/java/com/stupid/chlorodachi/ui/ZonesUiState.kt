package com.stupid.chlorodachi.ui

data class ZonesUiState(
    val isLoading: Boolean = true,
    val zones: List<ZoneUiModel> = emptyList()
)
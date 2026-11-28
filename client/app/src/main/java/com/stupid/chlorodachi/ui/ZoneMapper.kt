package com.stupid.chlorodachi.ui

import com.stupid.chlorodachi.model.Zone
import com.stupid.chlorodachi.model.ZoneInformation

fun Zone.toUiModel() : ZoneUiModel {
    val info = zoneInformation
    return ZoneUiModel(
        id = id,
        name = name,
        gridX = x.toInt(),
        gridY = y.toInt(),
        gridWidth = w.toInt(),
        gridHeight = h.toInt(),
        temperature = info?.temperature,
        humidity = info?.humidity,
        illuminance = info?.illuminance,
        soilStatus = info?.soilStatus() ?: SoilStatus.UNKNOWN,
        needsAttention = info?.hasReadingOutOfRange() ?: false
    )
}

fun Zone.toFormState(): ZoneFormUiState {
    val defaults = ZoneFormUiState()
    val info = zoneInformation
    val ranges = info?.ranges
    return ZoneFormUiState(
        name = name,
        plant = info?.plant.orEmpty(),
        x = x,
        y = y,
        w = w,
        h = h,
        minIlluminance = ranges?.illuminance?.start ?: defaults.minIlluminance,
        maxIlluminance = ranges?.illuminance?.endInclusive ?: defaults.maxIlluminance,
        validTemperature = ranges?.temperature ?: defaults.validTemperature,
        validHumidity = ranges?.humidity ?: defaults.validHumidity,
        validMoisture = ranges?.moisture ?: defaults.validMoisture,
    )
}

private fun ZoneInformation.soilStatus(): SoilStatus {
    val moisture = moisture ?: return SoilStatus.UNKNOWN
    val ranges = ranges ?: return SoilStatus.UNKNOWN
    return when (moisture) {
        in ranges.dryMoisture -> SoilStatus.DRY
        in ranges.wetMoisture -> SoilStatus.WET
        in ranges.moistMoisture -> SoilStatus.MOIST
        else -> SoilStatus.UNKNOWN
    }
}

private fun ZoneInformation.hasReadingOutOfRange(): Boolean {
    val ranges = ranges ?: return false
    return outside(temperature, ranges.temperature) ||
            outside(humidity, ranges.humidity) ||
            outside(moisture, ranges.moisture) ||
            outside(illuminance, ranges.illuminance)
}

private fun outside(value: Double?, range: ClosedFloatingPointRange<Double>) = value != null && value !in range
package com.stupid.chlorodachi.model

data class ZoneRange(
    val temperature: ClosedFloatingPointRange<Double>,
    val humidity: ClosedFloatingPointRange<Double>,
    val moisture: ClosedFloatingPointRange<Double>,
    val illuminance: ClosedFloatingPointRange<Double>
) {
    val dryMoisture: ClosedFloatingPointRange<Double>
        get() = (moisture.start - 5) .. (moisture.start + 5)
    val wetMoisture: ClosedFloatingPointRange<Double>
        get() = (moisture.endInclusive - 5) .. (moisture.endInclusive + 5)
    val moistMoisture: ClosedFloatingPointRange<Double>
        get() = dryMoisture.endInclusive .. wetMoisture.start
}
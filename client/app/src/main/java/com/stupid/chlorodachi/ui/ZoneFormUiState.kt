package com.stupid.chlorodachi.ui

val VALID_TEMPERATURE_RANGE = 0.0..40.0
val VALID_HUMIDITY_RANGE = 0.0..100.0
val VALID_MOISTURE_RANGE = 0.0..100.0

data class ZoneFormUiState(
    val name: String = "",
    val plant: String = "",
    val x: Double = 0.0,
    val y: Double = 0.0,
    val w: Double = 0.0,
    val h: Double = 0.0,
    val minIlluminance: Double = 1000.0,
    val maxIlluminance: Double = 100000.0,
    val validTemperature: ClosedFloatingPointRange<Double> = 0.0..40.0,
    val validHumidity: ClosedFloatingPointRange<Double> = 0.0..100.0,
    val validMoisture: ClosedFloatingPointRange<Double> = 0.0..100.0,
)

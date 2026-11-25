package com.stupid.chlorodachi.model

data class ZoneInformation(
    val plant: String = "",
    val temperature: Double? = null,
    val humidity: Double? = null,
    val moisture: Double? = null,
    val illuminance: Double? = null,
    val ranges: ZoneRange? = null
)

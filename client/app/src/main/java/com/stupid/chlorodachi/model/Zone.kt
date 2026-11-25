package com.stupid.chlorodachi.model

data class Zone(
    val id: String,
    val name: String,
    val x: Double,
    val y: Double,
    val w: Double,
    val h: Double,
    val devices: List<Device>,
    val zoneInformation: ZoneInformation? = null
)
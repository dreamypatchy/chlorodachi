package com.stupid.chlorodachi.model

data class Device(
    val serialNumber: String,
    val name: String? = null,
    val deviceType: String? = null,
    val activated: Boolean = false,
    val lastRun: Int? = null
)

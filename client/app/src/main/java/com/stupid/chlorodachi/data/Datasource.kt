package com.stupid.chlorodachi.data

import com.stupid.chlorodachi.model.Device
import com.stupid.chlorodachi.model.Zone
import com.stupid.chlorodachi.model.ZoneInformation
import com.stupid.chlorodachi.model.ZoneRange

class Datasource() {
    fun loadDemoZones(): List<Zone> {
        return listOf<Zone>(
            Zone(
                id = "1_f9f354faa93872",
                name = "Клубника",
                x = 5.0,
                y = 1.0,
                w = 1.0,
                h = 2.0,
                devices = listOf<Device>(
                    Device(
                        serialNumber = "1a"
                    ),
                    Device(
                        serialNumber = "1b"
                    )
                ),
                zoneInformation = ZoneInformation(
                    plant = "fragaria x ananassa",
                    temperature = 5.0,
                    humidity = 50.0,
                    moisture = 25.0,
                    illuminance = 10000.0,
                    ranges = ZoneRange(
                        temperature = 8.0..32.0,
                        humidity = 40.0..80.0,
                        moisture = 25.0..80.0,
                        illuminance = 4000.0..60000.0
                    )
                )
            ),
            Zone(
                id = "2_a7bd5faf4d1dc1",
                name = "Помидоры",
                x = 1.0,
                y = 1.0,
                w = 2.0,
                h = 2.0,
                devices = listOf<Device>(
                    Device(
                        serialNumber = "2a",
                        name = "Датчик влаги",
                        deviceType = "sensor"
                    ),
                    Device(
                        serialNumber = "2b",
                        name = "Датчик температуры",
                        deviceType = "sensor"
                    ),
                    Device(
                        serialNumber = "2c",
                        name = "Система полива",
                        deviceType = "actuator",
                        activated = true,
                        lastRun = 1795431167
                    )
                ),
                zoneInformation = ZoneInformation(
                    plant = "tomato master no. 2 f1",
                    temperature = 13.0,
                    humidity = 55.0,
                    moisture = 50.0,
                    illuminance = 10000.0,
                    ranges = ZoneRange(
                        temperature = 10.0..32.0,
                        humidity = 30.0..70.0,
                        moisture = 20.0..65.0,
                        illuminance = 3500.0..130000.0
                    )
                )
            ),
            Zone(
                id = "3_a7bd5faf4d1dc1",
                name = "Огурцы",
                x = 3.0,
                y = 1.0,
                w = 2.0,
                h = 1.0,
                devices = listOf<Device>(
                    Device(
                        serialNumber = "3a",
                        name = "Датчик света",
                        deviceType = "sensor"
                    )
                ),
                zoneInformation = ZoneInformation(
                    plant = "cucumber bon odori f1",
                    temperature = 7.0,
                    humidity = 65.0,
                    moisture = 65.0,
                    illuminance = 10000.0,
                    ranges = ZoneRange(
                        temperature = 6.0..35.0,
                        humidity = 30.0..80.0,
                        moisture = 25.0..75.0,
                        illuminance = 3500.0..85000.0
                    )
                )
            ),
            Zone(
                id = "4_a7bd5faf4d1dc1",
                name = "Картофель",
                x = 3.0,
                y = 2.0,
                w = 2.0,
                h = 1.0,
                devices = listOf<Device>(),
                zoneInformation = ZoneInformation(
                    plant = "solanum tuberosum",
                    temperature = 5.0,
                    humidity = 55.0,
                    moisture = 30.0,
                    illuminance = 10000.0,
                    ranges = ZoneRange(
                        temperature = 5.0..35.0,
                        humidity = 30.0..80.0,
                        moisture = 20.0..65.0,
                        illuminance = 3500.0..80000.0
                    )
                )
            ),
            Zone(
                id = "5_a7bd5faf4d1dc1",
                name = "Дыни",
                x = 1.0,
                y = 3.0,
                w = 1.0,
                h = 1.0,
                devices = listOf<Device>(),
                zoneInformation = ZoneInformation(
                    plant = "galia melon fandango f1 (trf11)",
                    temperature = 16.0,
                    humidity = 55.0,
                    moisture = 55.0,
                    illuminance = 10000.0,
                    ranges = ZoneRange(
                        temperature = 5.0..35.0,
                        humidity = 30.0..80.0,
                        moisture = 15.0..60.0,
                        illuminance = 4000.0..80000.0
                    )
                )
            ),
            Zone(
                id = "6_a7bd5faf4d1dc1",
                name = "Базилик",
                x = 2.0,
                y = 3.0,
                w = 3.0,
                h = 1.0,
                devices = listOf<Device>(),
                zoneInformation = ZoneInformation(
                    plant = "ocimum basilicum",
                    temperature = 19.0,
                    humidity = 55.0,
                    moisture = 55.0,
                    illuminance = 10000.0,
                    ranges = ZoneRange(
                        temperature = 8.0..32.0,
                        humidity = 30.0..80.0,
                        moisture = 15.0..60.0,
                        illuminance = 2500.0..60000.0
                    )
                )
            ),
            Zone(
                id = "7_a7bd5faf4d1dc1",
                name = "Арбузы",
                x = 5.0,
                y = 3.0,
                w = 1.0,
                h = 1.0,
                devices = listOf<Device>(),
                zoneInformation = ZoneInformation(
                    plant = "water melon amphion f1",
                    temperature = 19.0,
                    humidity = 55.0,
                    moisture = 55.0,
                    illuminance = 10000.0,
                    ranges = ZoneRange(
                        temperature = 10.0..40.0,
                        humidity = 30.0..80.0,
                        moisture = 22.0..65.0,
                        illuminance = 4200.0..120000.0
                    )
                )
            )
        )
    }
}
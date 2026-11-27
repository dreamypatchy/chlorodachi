from typing import List, Literal
from enum import Enum
from beanie import Document, PydanticObjectId, Link
from pydantic import BaseModel, Field
from datetime import datetime

'''
Users is a document model that has one greenhouse in it, 
greenhouse contains multiple zones, zones contain information, 
informations is constantly changed.

Device is also document model that is linked to one particular zone.

There's also acceptable ranges for automatization purposes.

Dimensional values are used for the greenhouse canvas inside the mobile app.

Maybe separating schemas and functions in different files.
UPD: the're separated

TODO: 
+ functions that work with schemas
+ working information schema
- information change functions 
+ schemas connection with API
- acceptable ranges for autmoatization
'''

class Metric(str, Enum):
    temperature = "temperature"
    humidity = "humidity"
    moisture = "moisture"
    illuminance = "illuminance"

class DeviceRole(str, Enum):
    sensor = "sensor"
    actuator = "actuator"

class UnregisteredDevices(Document):
    serial_number: str

class Session(Document):
    _id: str
    user_id: str
    is_revoked: bool = False
    created_at: int
    expires_at: int

class SensorConfig(BaseModel):
    metric: Metric
    last_value: float | None = None
    last_update: int | None = None

class ActuatorConfig(BaseModel):
    action_type: Literal["switch", "level"] = "switch"
    is_on: bool = False
    level: float = 0.0

class Device(Document):
    serial_number: str = Field(..., unique = True)
    name: str | None = None
    role: DeviceRole
    device_type: str | None = None
    isActivated: bool = False
    activationDate: int | None = None

    sensor: SensorConfig | None = None
    actuator: ActuatorConfig | None = None

    class Settings:
        name = "device"

class Automation(BaseModel):
    name: str | None = None
    enabled: bool = True
    metric: Metric

    min_value: float | None = None
    max_value: float | None = None
    trigger_sensor: Link[Device] | None = None
    actuator: Link[Device] 

    trigger_value_min: float | None = None
    trigger_value_max: float | None = None

    reset_when_normal: bool = True

class ZoneRange(BaseModel):
    maxhumidity: float | None = None
    minhumidity: float | None = None
    maxtemperature: float | None = None
    mintemperature: float | None = None
    maxmoisture: float | None = None
    minmoisture: float | None = None
    maxilluminance: float | None = None
    minilluminance: float | None = None

class ZoneInformation(BaseModel):
    plant: str | None = None
    temperature: float = 0.0
    humidity: float = 0.0
    moisture: float = 0.0
    illuminance: float = 0.0
    zonerange: ZoneRange | None = None
    automation: List[Automation] = Field(default_factory=list)

class Zone(BaseModel):
    id: PydanticObjectId = Field(default_factory=PydanticObjectId)
    name: str | None = None
    x: float
    y: float
    h: float
    w: float

    devices: list[Link[Device]] = Field(default_factory=list)
    zoneinformation: ZoneInformation = Field(default_factory=ZoneInformation)

class Greenhouse(BaseModel):
    name: str
    w: float
    h: float
    zones: List[Zone] = Field(default_factory=list)

class User(Document):
    username: str
    password: str
    greenhouse: Link[Greenhouse] | None = None

    class Settings:
        name = "user"        


#TODO rest of the functions. NO MORE
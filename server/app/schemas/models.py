from typing import List
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

class Device(Document):
    serial_number: str
    name: str | None = None
    device_type: str | None = None
    isActivated: bool = False
    activationDate: int | None = None

    class Settings:
        name = "device"

class ZoneRange(BaseModel):
    maxhumidity: float
    minhumidity: float
    maxtemperature: float
    mintemperature: float
    maxmoisture: float
    minmoisture: float
    maxilluminance: float
    minilluminance: float

class ZoneInformation(BaseModel):
    plant: str
    temperature: float
    humidity: float
    moisture: float
    illuminance: float
    ranges: ZoneRange | None = None

class Zone(BaseModel):
    id: PydanticObjectId = Field(default_factory=PydanticObjectId)
    name: str | None = None
    x: float
    y: float
    h: float
    w: float

    devices: list[Link[Device]] | None = None

    zoneinformation: ZoneInformation | None = None

class Greenhouse(BaseModel):
    name: str
    w: float
    h: float
    zones: List[Zone] | None = None

class User(Document):
    username: str
    password: str
    greenhouse: Greenhouse | None = None

    class Settings:
        name = "user"        


#TODO rest of the functions. NO MORE
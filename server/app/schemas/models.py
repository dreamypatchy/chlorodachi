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

class Session(Document):
    _id: str
    user_id: str
    is_revoked: bool = False
    created_at: int
    expires_at: int

class Device(Document):
    serial_number: str
    name: str | None = None
    device_type: str | None = None
    isActivated: bool = False
    activationDate: int | None = None

    class Settings:
        name = "device"

class Automation(BaseModel):
    trigger: Link[Device] | None = None
    trigger_value_min: float | None = None
    actuator: Link[Device] | None = None
    trigger_value_max: float | None = None


class ZoneInformation(BaseModel):
    plant: str | None = None
    temperature: float = 0.0
    humidity: float = 0.0
    moisture: float = 0.0
    illuminance: float = 0.0
    automation: List[Automation] | None = None
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
    greenhouse: Link[Greenhouse] | None = None

    class Settings:
        name = "user"        


#TODO rest of the functions. NO MORE
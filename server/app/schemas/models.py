from typing import List, Optional
from beanie import Document
from pydantic import BaseModel

'''
Users is a document model that has one greenhouse in it, 
greenhouse contains multiple zones, zones contain information, 
informations is constantly changed.

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
    ranges: ZoneRange

class Zones(BaseModel):
    x: float
    y: float
    h: float
    w: float
    zoneinformation: ZoneInformation

class Greenhouse(BaseModel):
    name: str
    w: float
    h: float
    zones: List[Zones]

class User(Document):
    username: str
    password: str
    greenhouse: Greenhouse

    class Settings:
        name = "users"        


#TODO rest of the functions. NO MORE
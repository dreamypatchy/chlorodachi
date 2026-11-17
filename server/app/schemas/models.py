from typing import List, Optional
from beanie import Link, Document
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

class ZoneInformation(BaseModel):
    plant: str
    temperature: float
    humidity: float
    moisture: float
    illuminance: float

    
    class Settings:
        name = "information"

class Zones(Document):
    x: float
    y: float
    h: float
    w: float
    zoneinformation: ZoneInformation

    class Settings:
        name = "zones"

class Greenhouse(BaseModel):
    name: str
    w: float
    h: float
    zones: List[Zones]

    class Settings:
        name = "greenhouse"

class User(Document):
    username: str
    password: str
    greenhouse: Greenhouse

    class Settings:
        name = "users"        


#TODO rest of the functions. NO MORE
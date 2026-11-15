from typing import List
from beanie import Link, Document
from pydantic import BaseModel

'''
Users is a document model that has one greenhouse in it, greenhouse contains multiple zones, zones contain information, informations is constantly changed.

Dimensional values are used for the greenhouse canvas inside the mobile app.

Maybe separating schemas and functions in different files.

TODO: 
- functions that work with schemas - in other file
- working information schema
- information change functions 
- schemas connection with API - in other file
'''

class Information(BaseModel):
    data: str
    #TODO

    class Settings:
        name = "information"

class Zones(Document):
    x: int
    y: int
    h: int
    w: int
    information: Information

    class Settings:
        name = "zones"

class Greenhouse(BaseModel):
    name: str
    w: int
    h: int
    zones: List[Link[Zones]]

    class Settings:
        name = "greenhouse"

class User(Document):
    username: str
    password: str
    greenhouse: Greenhouse

    class Settings:
        name = "users"        


#TODO rest of the functions. NO MORE
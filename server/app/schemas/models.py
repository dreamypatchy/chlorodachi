from typing import List
from motor.motor_asyncio import AsyncIOMotorClient
from beanie import init_beanie, Link, Document

'''
Users is a document model that has one greenhouse in it, greenhouse contains multiple zones, zones contain information, informations is constantly changed.

Dimensional values are used for the greenhouse canvas inside the mobile app.

Maybe separating schemas and functions in different files.

TODO: 
- functions that work with schemas
- working information schema
- information change functions
- schemas connection with API
'''

class Information(Document):
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

class Greenhouse(Document):
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


async def start_db():
    client = AsyncIOMotorClient("mongodb://localhost:27017")
    await init_beanie(database=client.db_name, document_models=[User])


#TODO rest functions
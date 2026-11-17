from pymongo import AsyncMongoClient 
from beanie import init_beanie

from app.core.config import settings
from app.schemas.models import User, Zones, Greenhouse, ZoneInformation

'''
Something for the DB, not sure for now

All of the database queries and updates should be handled here.
'''

async def init_db():
    global client
    client = AsyncMongoClient(settings.mongodb_uri, serverSelectionTimeoutMS=5000)

    db = client[settings.mongodb_name]

    await init_beanie(database=db, document_models=[User, Zones])


async def close_db():
    if client:
        client.close() 

async def find_user(name):
    return await User.find_one(User.username == name)

async def create_test_user():
    if not await find_user("test"):
        try:
            test_zoneinformation = ZoneInformation(plant = "potaTOES", temperature = 36.6, humidity = 60.0, moisture = 85.5, illuminance = 10.0)
            test_zones = Zones(x = 1, y = 1, h = 1, w = 1, zoneinformation = test_zoneinformation)
            await test_zones.insert()
            test_greenhouse = Greenhouse(name = "test_greenhouse", w = 1, h = 1, zones = [test_zones, test_zones])
            test_user = User(username = "test", password = "test", greenhouse = test_greenhouse)
            await test_user.insert()

            return {"Succes": "User created succesfuly"}
        except:
            return {"Problem": "User not created due to the problems"}

    else:
        return {"Problem": "User already created"}

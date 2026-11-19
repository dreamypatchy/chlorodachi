from pymongo import AsyncMongoClient
from pymongo.errors import ConfigurationError, ExecutionTimeout
from beanie import init_beanie

from app.core.config import settings
from app.schemas.models import User, Zones, Greenhouse, ZoneInformation, ZoneRange

'''
Something for the DB, not sure for now

All of the database queries and updates should be handled here.
'''

async def init_db():
    global client
    client = AsyncMongoClient(settings.mongodb_uri, serverSelectionTimeoutMS=5000)

    db = client[settings.mongodb_name]

    await init_beanie(database=db, document_models=[User])


async def close_db():
    if client:
        client.close() 

async def find_user(name):
    found_user = await User.find_one(User.username == name)
    if found_user:
        return {"user": found_user}
    else:
        return {"Problem": f"User {name} doesn't exist"}

async def create_test_user():
    fi_user = await find_user("test")
    if not fi_user["user"]:
        try:
            test_zoneranges = ZoneRange(maxhumidity = 85, minhumidity = 40, maxtemperature = 40, mintemperature = 14.5, maxmoisture = 90, minmoisture = 60, maxilluminance = 8000, minilluminance = 80000)
            test_zoneinformation = ZoneInformation(plant = "potaTOES", temperature = 36.6, humidity = 60.0, moisture = 85.5, illuminance = 14000, ranges = test_zoneranges)
            test_zones = Zones(x = 1, y = 1, h = 1, w = 1, zoneinformation = test_zoneinformation)
            test_greenhouse = Greenhouse(name = "test_greenhouse", w = 1, h = 1, zones = [test_zones])
            test_user = User(username = "test", password = "test", greenhouse = test_greenhouse)
            await test_user.insert()

            return {"Succes": "User created succesfuly"}
        except AttributeError:
            return {"Problem": "Something with the creation"}
        except ConfigurationError as conferr:
            return {"Problem": conferr}
        except ExecutionTimeout as exctime:
            return {"Problem": exctime}

    else:
        return {"Problem": "User already created"}


async def delete_test_user():
    test_user = await find_user("test")
    if test_user:
        try:
            result = await test_user["user"].delete_many()
            return {"Succes": "User deleted"}
        except: 
            return {"Problem": "User wasn't deleted"}

    else:
        return {"Problem": "User don't exist"}

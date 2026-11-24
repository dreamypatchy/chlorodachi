from pymongo import AsyncMongoClient
from pymongo.errors import ConfigurationError, ExecutionTimeout, ServerSelectionTimeoutError
from beanie import init_beanie, PydanticObjectId

from app.core.config import settings
from app.schemas.models import User, Zone, Greenhouse, ZoneInformation, ZoneRange, Device
from app.core.security import verify_password, hash_password

'''
Something for the DB, not sure for now

All of the database queries and updates should be handled here.
'''

async def init_db():
    global client
    try:
        client = AsyncMongoClient(settings.mongodb_uri, serverSelectionTimeoutMS=5000)

        db = client[settings.mongodb_name]

        await init_beanie(database=db, document_models=[User, Device])
    
    except ServerSelectionTimeoutError:
        client = AsyncMongoClient(settings.mongodb_uri, serverSelectionTimeoutMS=5000)

        db = client[settings.mongodb_name]

        await init_beanie(database=db, document_models=[User, Device])


async def close_db():
    if client:
        client.close() 

async def find_user(name):
    found_user = await User.find_one(User.username == name)
    if found_user:
        return {"user": found_user}
    else:
        return {"user": False}

async def find_device(serial):
    found_device = await Device.find_one(Device.serial_number == serial)
    if found_device:
        return {"deivce": found_device}
    else:
        return {"device": False}

async def delete_user(name: str):
    user = await find_user(name)
    if user["user"]:
        try:
            await User.delete_all()
            return {"Success":"User was deleted"}
        except: 
            return {"Problem": "User wasn't deleted"}

    else:
        return {"Problem": "User doesn't exist"}


'''
Rta 6 test queries
'''

async def create_test_user():
    fi_user = await find_user("test")
    if not fi_user["user"]:
        try:
            test_zoneranges = ZoneRange(maxhumidity = 85, minhumidity = 40, maxtemperature = 40, mintemperature = 14.5, maxmoisture = 90, minmoisture = 60, maxilluminance = 8000, minilluminance = 80000)
            test_zoneinformation = ZoneInformation(plant = "potaTOES", temperature = 36.6, humidity = 60.0, moisture = 85.5, illuminance = 14000, ranges = test_zoneranges)
            test_zones = Zone(x = 1, y = 1, h = 1, w = 1, zoneinformation = test_zoneinformation)
            test_greenhouse = Greenhouse(name = "test_greenhouse", w = 1, h = 1, zones = [test_zones])
            test_user = User(username = "test", password = hash_password("123123"), greenhouse = test_greenhouse)
            await test_user.insert()

            return {"Succes": "User created succesfuly"}
        except AttributeError:
            return {"Problem": "Something with the creation"}
        except ConfigurationError as conferr:
            return {"Problem": conferr}
        except ExecutionTimeout as exctime:
            return {"Problem": exctime}

    else:
        return {"Problem": "User already exists"}

async def create_test_device(serial: str, name: str = None):
    try:
        test_device = Device(serial_number = serial, name = name)
        await test_device.insert()
        return {"Success": "Device created"}
    except:
        return {"Problem" : "Something went wrong"}

async def link_device_zone(zone_id: str, device_serial: str):
    device = await Device.find_one({"serial_number": device_serial})
    if not device:
        return {"Problem": "Device was not found"}

    target_id = PydanticObjectId(zone_id)

    user = await User.find_one({"greenhouse.zones.id": target_id})
    
    if not user:
        return {"Problem": "Zone was not found"}

    zone = next((z for z in user.greenhouse.zones if z.id == target_id), None)

    if not zone:
        return {"Problem": "Zone was not found"}

    if not zone.devices:
        zone.devices = []

    zone.devices.append(device)
    await user.save()
    
    return {"Succes": "Device was linked"}
    

async def create_scratch_user(username: str, password: str):
    fi_user = await find_user(username)
    if not fi_user["user"]:
        try:
            user = User(username = username, password = hash_password(password))
            await user.insert()
            return {"Succes": "User was created"}
        except:
            return {"Problem": "User was not created"}
    else:
        return {"Problem": "User exists"}

async def create_scratch_greenhouse(username: str, greenhouse_name: str, w: float, h: float):
    fi_user = await find_user(username)
    if fi_user["user"]:
        try:
            greenhouse = Greenhouse(name = greenhouse_name, w = w, h = h)
            await fi_user["user"].set({User.greenhouse: greenhouse})
            await fi_user["user"].save()
            return {"Succes": "Greenhouse was created and set"}
        except:
            return {"Problem": "Greenhouse was not created"}
    else:
        return {"Problem": "User for this greenhouse is not exists"}

async def create_scratch_zone(username: str, x: float, y: float, h: float, w: float):
    fi_user = await find_user(username)
    if fi_user["user"]:
        #try:
            zone = Zone(x = x, y = y, h = h, w = w)
            await fi_user["user"].set({User.greenhouse.zones: zone})
            return {"Succes": "Zone was added"}
        #except:
        #    return {"Problem": "Zone was not created"}
    else:
        return {"Problem": "User is not exists"}
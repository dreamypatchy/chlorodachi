from pymongo import AsyncMongoClient
from pymongo.errors import ConfigurationError, ExecutionTimeout, ServerSelectionTimeoutError
from beanie import init_beanie, PydanticObjectId
from datetime import datetime, timedelta

from app.core.config import settings
from app.schemas.models import *
from app.core.security import verify_password, hash_password, create_session

from app.core.exceptions import *

'''
Something for the DB, not sure for now

All of the database queries and updates should be handled here.
'''

async def init_db():
    global client
    client = AsyncMongoClient(settings.mongodb_uri, serverSelectionTimeoutMS=5000)
    db = client[settings.mongodb_name]
    max_attempts = 5 
    for attempt in range (1, max_attempts):
        try:
            await init_beanie(database=db, document_models=[User, Device, Session])
            break
        except ServerSelectionTimeoutError:
            print(f"DB initialization failed. Attempt: {attempt}/{max_attempts}. Trying again.")
            if attempt == max_attempts:
                print(f"Connection with {settings.mongodb_name} not established.")
                break

async def close_db():
    if client:
        client.close() 

async def find_user(name: str):
    found_user = await User.find_one({"username": name})
    if found_user:
        return found_user
    else:
        return None

async def find_device(serial: str):
    found_device = await Device.find_one({"serial_number": serial})
    if found_device:
        return found_device
    else:
        return None

async def find_session(sid: str):
    found_session = await Session.find_one({"_id": sid})
    if found_session:
        return found_session
    else:
        return None

async def get_user_id(username: str):
    fi_user = await find_user(username)
    if fi_user:
        return str(fi_user.id)
    raise UserDoesntExists

'''
Creating parts of the database
'''
async def insert_session(uid: str, now: datetime):
    fi_session = await find_session(sid)
    if not fi_session:
        try:
            create_session(uid, sid, now, expire_delta)
            session = Session(_id = sid, user_id = uid, created_at = now, expires_at = expire_delta)
            await session.insert()
        except:
            raise SessionCreationError
    else:
        raise SessionAlreadyExists

    raise SessionDoesntExists

async def create_scratch_user(username: str, password: str):
    fi_user = await find_user(username)
    if not fi_user:
        try:
            user = User(username = username, password = hash_password(password))
            await user.insert()
        except:
            raise UserCreationError
    else:
        raise UserAlreadyExists

async def create_scratch_greenhouse(username: str, greenhouse_name: str, w: float, h: float):
    fi_user = await find_user(username)
    if fi_user:
        try:
            greenhouse = Greenhouse(name = greenhouse_name, w = w, h = h)
            await fi_user.set({User.greenhouse: greenhouse})
            await fi_user.save()
        except:
            raise GreenhouseCreationError
    else:
        raise UserDoesntExists

async def create_scratch_zone(username: str, x: float, y: float, h: float, w: float):
    fi_user = await find_user(username)
    if fi_user:
        try:
            zone = Zone(x = x, y = y, h = h, w = w)
            await fi_user.set({User.greenhouse.zones: zone})
        except:
            raise ZoneCreationError
    else:
        raise UserDoesntExists

async def create_scratch_device(serial: str, role: str):
    fi_device = await find_device(serial=serial)
    if not fi_device:
        try:
            if role == DeviceRole.sensor:
                role = DeviceRole.sensor
            elif role == DeviceRole.actuator:
                role = DeviceRole.actuator

            device = Device(serial_number=serial, role=role)

            await device.insert()
        except:
            raise DeviceCreationError
    else:
        raise DeviceAlreadyExists

async def put_unregistered(serial_number: str):
    if not UnregisteredDevices.find_one({"serial_number": serial_number}):
        device = UnregisteredDevices(serial_number=serial_number)
        device.insert()
    else:
        raise DeviceAlreadyExists

'''
Configuring parts of the database
'''

async def link_device_zone(zone_id: str, device_serial: str):
    device = await Device.find_one({"serial_number": device_serial})
    if not device:
        raise DeviceNotFound()

    target_id = PydanticObjectId(zone_id)

    user = await User.find_one({"greenhouse.zones.id": target_id})
    
    if not user:
        raise UserDoesntExists("User for this zone doesn't exists")

    zone = next((z for z in user.greenhouse.zones if z.id == target_id), None)

    if not zone:
        raise ZoneNotFound("Zone was not found")

    if not zone.devices:
        zone.devices = []

    zone.devices.append(device)
    await user.save()

'''
Queries
'''

async def get_unregistered():
    device_list = []
    for device in await UnregisteredDevices.serial_number.find_one({}):
        device_list.append(device)
    
    return device_list

'''
Deletions
'''

async def delete_user(name: str):
    user = await find_user(name)
    if user:
        try:
            await user.delete()
        except: 
            raise NotDeleted

    else:
        raise UserAlreadyExists



'''
Rta 6 test queries

Mostly deprecated
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

        except:
            raise UserCreationError
    else:
        raise UserAlreadyExists

async def create_test_device(serial: str, name: str = None):
    try:
        test_device = Device(serial_number = serial, name = name)
        await test_device.insert()
    except:
        raise DeviceCreationError
    


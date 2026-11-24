from fastapi import APIRouter, HTTPException
from app.core.security import token_decode, token_encode, Token
from app.core.database import find_user, create_test_user, delete_user, create_scratch_greenhouse, create_scratch_user, create_scratch_zone, create_test_device, link_device_zone, find_device


'''
Something made to test the functionality of the database and JWT

TODO:
- Handle the JWTs
- Think about what to do
'''

router = APIRouter(prefix="/api/v1/test", tags=["Test"])

'''
JWT tests
'''

@router.get("/token", response_model=Token)
async def test_get_token(info_to_encode: str):
    try:
        token = token_encode(str(info_to_encode))
        return token
    except:
        raise HTTPException(status_code=400, detail="Type error")

@router.post("/token/decode")
async def test_decode_token(token: Token):
    try:
        return {"info": token_decode(token)}
    except:
        raise HTTPException(status_code=400, detail="Token problem")


'''
DB tests
'''

@router.get("/db/test_user")
async def test_db_user_create():
    try:
        return await create_test_user()
    except:
       raise HTTPException(status_code=400, detail="User creation problem")

@router.post("/db/get_user")
async def test_db_get_user(username: str):
    try:
        return await find_user(username)
    except:
        raise HTTPException(status_code=400, detail="Malformed information")

@router.post("/db/delete_user")
async def test_db_user_delete(username: str):
    try:
        return await delete_user(username)
    except:
        raise HTTPException(status_code=400, detail="User was not deleted")

@router.post("/db/link_device")
async def test_link_device(zone_id: str, serial: str):
    return await link_device_zone(zone_id=zone_id, device_serial=serial)

@router.post("/db/create_device")
async def test_create_device(serial: str, name: str = None):
    return await create_test_device(serial=serial, name=name)

@router.post("/db/find_device")
async def test_find_device(serial: str):
    return await find_device(serial=serial)

@router.post("/db/scratch_user")
async def scratch_user_create(username: str, password: str):
    try:
        return await create_scratch_user(username, password)
    except:
        raise HTTPException(status_code=400, detail="Scratch user can not be created")

@router.post("/db/scratch_greenhouse")
async def scratch_greenhouse_create(username: str, greenhouse_name: str, w: float, h: float):
    try:
        return await create_scratch_greenhouse(username, greenhouse_name, w, h)
    except:
        raise HTTPException(status_code=400, detail="Scratch greenhouse was not created")

@router.post("/db/scratch_zone")
async def scratch_zone_create(username: str, x: float, y: float, h: float, w: float):
    try:
        return await create_scratch_zone(username, x, y, h, w)
    except:
        raise HTTPException(status_code=400, detail="Scratch zone was not created")
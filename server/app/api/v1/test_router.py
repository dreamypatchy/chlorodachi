from fastapi import APIRouter, HTTPException
from app.core.security import token_decode, token_encode, Token
from app.core.database import find_user, create_test_user


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
        return {"user": await find_user(username)}
    except:
        raise HTTPException(status_code=400, detail="Malformed information")
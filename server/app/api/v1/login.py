from fastapi import APIRouter, status, HTTPException
from app.core.security import token_decode, token_encode, verify_password, create_session
from app.core.database import get_user_id, create_scratch_user, find_user, User
from datetime import datetime, timedelta

from app.core.exceptions import UserDoesntExists, UserAlreadyExists, InvalidOrExpiredToken


'''
Everything connected to the JWT and logins
'''

router = APIRouter(prefix="/api/v1", tags=["JWT and Logins"])

@router.post("/registration", status_code=status.HTTP_200_OK)
async def registration(token: str):
    try:
        token = token_decode(token=token)
        if token["typ"] == "registration" or token["exp"] > int(datetime.now().timestamp()):
            try:
                info = token["info"]
                user = info["user"]
                password = info["pass"]

                try: 
                    await create_scratch_user(username=user, password=password)
                except UserAlreadyExists:
                    raise HTTPException(status_code=status.HTTP_409_CONFLICT)

                return await get_user_id(user)
            except UserDoesntExists:
                raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)
            except:
                raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST)
        else:
            raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)
    except InvalidOrExpiredToken:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)


@router.post("/login", status_code=status.HTTP_200_OK)
async def login(token: str):
    try:
        token = token_decode(token=token)
        if token["typ"] == "login" or token["exp"] > int(datetime.now().timestamp()):
            info = token["info"]
            user = info["user"]
            password = info ["pass"]

            try:
                fi_user = await find_user(user)
            except UserDoesntExists:
                raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)

            if verify_password(password=password, hashed_password=fi_user.password):
                return create_session(fi_user.id)
            raise InvalidOrExpiredToken
    except InvalidOrExpiredToken:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)
from fastapi import APIRouter, HTTPException, status, Request
from app.core.security import token_decode, token_encode
from app.core.database import find_user, find_device, get_unregistered, put_unregistered
from app.core.exceptions import *
from app.core.utils import now_timestamp

from pydantic import BaseModel

'''
Router for the greeenhouse
'''

class PayloadSensos(BaseModel):
    serial_number: str
    value: float
    timestamp: int | None = None

router = APIRouter(prefix="/api/v1/greenhouse", tags=["Greenhouse"])

@router.post("/telemetry")
async def get_telemetry(payload: PayloadSensos, request: Request):
    try:
        pass
    except ValueError:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST)


@router.post("/unregistered/post", status_code=status.HTTP_200_OK)
async def post_unregistered_devices(token: str):
    try:
        token = token_decode(token=token)
        serial = token["sub"]
        if token["typ"] == "registration" and token["exp"] > now_timestamp():
            put_unregistered(serial_number=serial_number)
        else:
            raise InvalidOrExpiredToken
    except InvalidOrExpiredToken:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED)
    except DeviceAlreadyExists:
        raise HTTPException(status_code=status.HTTP_409_CONFLICT)

@router.get("/unregistered/get")
async def get_unregistered_devices():
    return {"devices": get_unregistered()}
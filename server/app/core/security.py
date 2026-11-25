import jwt
import uuid
from pydantic import BaseModel
from datetime import datetime, timedelta, timezone
from argon2 import PasswordHasher
from argon2.exceptions import VerifyMismatchError
from json import dumps 

from app.core.config import settings
from app.core.exceptions import InvalidOrExpiredToken, MalformedTokenPayload

ph = PasswordHasher()
'''
Security part of the core with JWT handling and password hashing. 
'''

def _load_key(key: str) -> str:
    return key.strip('"').strip("'").replace("\\n", "\n")

#sub sid iat exp type
def token_encode(sub: str, sid: str, expire_delta: timedelta, now: datetime, token_type: str, info: dict[str, Any] | None = None) -> str:
    exp = int(expire_delta.total_seconds())
    now = int(now.timestamp())
    try:
        payload = {"sub": sub, "sid": sid, "exp": now + exp, "crt": now, "typ": token_type}
    except:
        raise MalformedTokenPayload("Bad payload")
    if info is not None:
        payload["info"] = info

    key = _load_key(settings.private_key)
    encoded_token = jwt.encode(payload, key, algorithm=settings.algorithm)

    return encoded_token


def create_session(uid: str) -> dict:
    now = datetime.now(timezone.utc)
    sid = str(uuid.uuid4())
    uid = str(uid)

    access_token = token_encode(sub = uid, sid = sid, expire_delta = timedelta(minutes = settings.token_eol_minutes), now = now, token_type = "access")
    refresh_token = token_encode(sub = uid, sid = sid, expire_delta = timedelta(days = settings.token_eol_days), now = now, token_type = "refresh")

    return {"sid": sid, "access_token": access_token, "refresh_token": refresh_token}

def token_decode(token: str) -> dict:
    key = _load_key(settings.public_key)
    return jwt.decode(token, key, algorithms=[settings.algorithm])

def hash_password(password: str) -> str:
    return ph.hash(password)

def verify_password(password: str, hashed_password: str) -> bool:
    try:
        ph.verify(hash=hashed_password, password=password)
        return True
    except:
        return False

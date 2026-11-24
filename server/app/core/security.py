import jwt
import uuid
from pydantic import BaseModel
from datetime import datetime, timedelta, timezone
from argon2 import PasswordHasher
from argon2.exceptions import VerifyMismatchError

from app.core.config import settings

ph = PasswordHasher()
'''
Security part of the core with JWT handling and password hashing. 
'''


#sub sid iat exp type
def token_encode(sub: str, sid: str, expire_delta: timedelta, now: timedelta, token_type: str) -> str:
    payload = {"sub": sub, "sid": sid, "exp": now + expire, "crt": now, "typ": token_type}
    key = settings.private_key.strip('"').strip("'").replace("\\n", "\n")
    encoded_token = jwt.encode(payload, key, algorithm=settings.algorithm)

    return Token(token_info=encoded_token)

def create_session(uid: str) -> dict:
    now = datetime.now(timezone.utc)
    sid = str(uuid.uuid4())

    access_token = token_encode(sub = uid, sid = sid, expire_delta = timedelta(minutes = token_eol_mintues), now = datetime.now(timezone.utc), token_type = "access")
    refresh_token = token_encode(sub = uid, sid = sid, expire_delta = timedelta(days = token_eol_days), now = datetime.now(timezone.utc), token_type = "refresh")

    return {"sid": sid, "access_token": access_token, "refresh_token": refresh_token, "session_id": sid}

    

def token_decode(token: Token) -> str:
    key = settings.public_key.strip('"').strip("'").replace("\\n", "\n")
    return jwt.decode(token.token_info, key, algorithms=[settings.algorithm])



def hash_password(password: str) -> str:
    return ph.hash(password)

def verify_password(password: str, hashed_password: str) -> bool:
    try:
        ph.verify(password, hashed_password)
        return True
    except:
        return False

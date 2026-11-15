import jwt
from app.core.config import settings
from datetime import datetime, timedelta, timezone

class Token():
    token_info: str
    token_type: str = "bearer"

def token_encode(sub: str) -> Token:
    expire = datetime.now(timezone.utc) + timedelta(minutes=settings.token_eol)

    payload = {"sub": sub, "exp": expire}

    encoded_token = jwt.encode(payload, settings.private_key, algorithm=settings.algorithm)

    return Token(token_info=encoded_token)

def token_decode(token: str):
    return jwt.decode(token, settings.public_key, algorithms=[settings.algorithm])
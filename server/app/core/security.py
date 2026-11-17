import jwt
from app.core.config import settings
from pydantic import BaseModel
from datetime import datetime, timedelta, timezone

'''
Security part of the core with JWT handling.
'''

class Token(BaseModel):
    token_info: str
    token_type: str = "bearer"

def token_encode(sub: str) -> Token:
    expire = datetime.now(timezone.utc) + timedelta(minutes=settings.token_eol)
    payload = {"sub": sub, "exp": expire}
    key = settings.private_key.strip('"').strip("'").replace("\\n", "\n")
    encoded_token = jwt.encode(payload, key, algorithm=settings.algorithm)

    return Token(token_info=encoded_token)

def token_decode(token: Token):
    key = settings.public_key.strip('"').strip("'").replace("\\n", "\n")
    return jwt.decode(token.token_info, key, algorithms=[settings.algorithm])
import jwt
from config import Settings

class Token():
    token_info: str

def token_encode(data: dict) -> Token:
    return Token(jwt.encode(data, Settings.private_key, algorithm=Settings.algorithm))

def token_decode(token: Token):
    return jwt.decode(token, Settings.public_key, algorithms=[Settings.algorithm])
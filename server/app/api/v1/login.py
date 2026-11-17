from fastapi import APIRouter
from app.core.security import token_decode, token_encode, Token


'''
Everything connected to the JWT and logins
'''

router = APIRouter(prefix="/api/v1/login", tags=["JWT and Logins"])
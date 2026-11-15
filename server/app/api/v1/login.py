from fastapi import APIRouter

'''
Everything connected to the JWT and logins
'''

router = APIRouter(prefix="/api/v1/login", tags=["JWT and Logins"])

#test query
@router.get("/test")
async def test_root():
    return {"v1_test": "aaadfasdfasdf"}

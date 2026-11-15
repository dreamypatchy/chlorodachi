'''
Something for the DB, not sure
'''

from pymongo import AsyncMongoClient 
from beanie import init_beanie

from app.core.config import settings
from app.schemas.models import User, Zones

async def init_db():
    global client
    client = AsyncMongoClient(settings.mongodb_uri, serverSelectionTimeoutMS=5000)

    db = client[settings.mongodb_name]

    await init_beanie(database=client.db_name, document_models=[User, Zones])


async def close_db():
    if client:
        client.close() 

from fastapi import FastAPI
from contextlib import asynccontextmanager

from app.core.config import settings
from app.core.database import init_db, close_db
from app.api.v1.login import router as login_router

@asynccontextmanager
async def lifespan(app: FastAPI):
    print(f"Starting: {settings.title}")
    await init_db()
    print("Starting: DB")

    yield
    await close_db()
    print("Shutting down: DB")
    print(f"Shutting down {settings.title}")

app = FastAPI(title=settings.title, debug=settings.title, lifespan=lifespan)

@app.get("/")
async def home():
    return {"the root": "the rootest root"}

app.include_router(login_router)
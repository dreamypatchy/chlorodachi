from dotenv import dotenv_values
from pydantic_settings import BaseSettings

config = dotenv_values(".env")

class Settings(BaseSettings):
    token_eol: int = config.TOKEN_LIFESPAN_MINUTES
    algorithm: str = config.ALGORITHM
    private_key: str = config.PRIVATE_KEY
    public_key: str = config.PUBLIC_KEY
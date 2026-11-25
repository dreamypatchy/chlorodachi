from pathlib import Path
from pydantic import Field
from pydantic_settings import BaseSettings, SettingsConfigDict

ENV_PATH = Path(__file__).resolve().parents[2] / ".env"

class Settings(BaseSettings):
    token_eol_minutes: int = Field(alias="TOKEN_LIFESPAN_MINUTES")
    token_eol_days: int = Field(alias="TOKEN_LIFESPAN_DAYS")
    algorithm: str = Field(alias="ALGORITHM", default="RS256")
    private_key: str = Field(alias="PRIVATE_KEY")
    public_key: str = Field(alias="PUBLIC_KEY")

    mongodb_uri: str = Field(alias="MONGODB_URI")
    mongodb_name: str = Field(alias="MONGODB_NAME")

    title: str = "Cholorodachi something"
    debug: bool = Field(alias="DEBUG", default=False)

    model_config = SettingsConfigDict(
        env_file = ENV_PATH,
        env_file_encoding = "UTF-8",
        extra = "ignore"
    )




settings = Settings()
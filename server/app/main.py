from fastapi import FastAPI

app = FastAPI(title="Codename Green", debug=True)

@app.get("/ping")
async def pong():
    return {"ping":"pong"}
<img src="../assets/logo.png" width="60" align="left"/>

# Chlorodachi Server


\
**Author:** Ilya Kopytov

## Stack 

* [FastAPI](https://fastapi.tiangolo.com/) 

* [MongoDB](https://mongodb.com)

## Description
Headless API server for the clorodachi project.

## Usage

Create a `.env` file inside the `server/` directory:

```ini
TOKEN_LIFESPAN_MINUTES=30
PRIVATE_KEY="-----BEGIN PRIVATE KEY-----\n...\n-----END PRIVATE KEY-----"
PUBLIC_KEY="-----BEGIN PUBLIC KEY-----\n...\n-----END PUBLIC KEY-----"
ALGORITHM="RS256"
MONGODB_URI="mongodb://localhost:27017"
MONGODB_NAME="chlorodachi"
DEBUG=True
```

---

**Dev mode:**

Navigate to the `server/` folder, activate your virtual environment, and install dependencies:
```bash
cd server
python -m venv .venv
source .venv/bin/activate
pip install -r requirements.txt
```
Start the development server:
```bash
fastapi dev app/main.py
```
---
\
**Docker:**\
*No docker support yet*


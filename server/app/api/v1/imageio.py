from fastapi import APIRouter, HTTPException, UploadFile, File, status, Request
from fastapi.responses import FileResponse
from app.core.utils import save_image, get_image_path, delete_image, move_image, create_image_links

router = APIRouter(prefix="/api/v1/image", tags=["Image IO"])

@router.get("/links")
async def get_images(request: Request):
    return create_image_links(str(request.base_url)+"api/v1/image")


@router.post("/upload", status_code=status.HTTP_200_OK)
async def upload_image(file: UploadFile):
    if file.content_type == "image/png":
        if file.size < 5000000:
            save_image(file=file)
        else:
            raise HTTPException(status_code=status.HTTP_413_CONTENT_TOO_LARGE)
    else:
        raise HTTPException(status_code=status.HTTP_415_UNSUPPORTED_MEDIA_TYPE)

@router.get("/{filename}", response_class=FileResponse)
async def get_image(filename: str):
    try:
        file = get_image_path(filename=filename)
    except OSError:
        raise HTTPException(status_code=status.HTTP_400_BAD_REQUEST)
    except FileNotFoundError:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)
    return FileResponse(path=file, media_type="image/jpeg")

@router.get("/delete/{filename}", status_code=status.HTTP_200_OK)
async def remove_image(filename: str):
    try:
        delete_image(filename=filename)
    except FileNotFoundError: 
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)

@router.post("/rename/{filename}")
async def rename_image(filename: str, new_filename: str):
    try:
        return move_image(filename=filename, new_filename=new_filename)
    except FileNotFoundError:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND)
    except FileExistsError:
        raise HTTPException(status_code=status.HTTP_409_CONFLICT)


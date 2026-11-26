from fastapi import UploadFile
from pathlib import Path, PurePosixPath
import shutil

images_folder_path = Path.cwd().joinpath("app").joinpath("images")
allowed_extansions = {".png", ".jpeg", ".jpg"}

def save_image(file: UploadFile) -> None:
    try:
        destanation = images_folder_path.joinpath(file.filename).absolute()

        try:
            with destanation.open("wb") as buffer:
                shutil.copyfileobj(file.file, buffer)
        except FileNotFoundError:
            raise TypeError
    finally:
        file.file.close()

def get_image_path(filename: str):
    image = (images_folder_path / filename).resolve()

    if not image.is_file():
        raise FileNotFoundError

    return image

def delete_image(filename: str):
    image = get_image_path(filename=filename)

    image.unlink()

def move_image(filename: str, new_filename: str):
    image = get_image_path(filename=filename)

    if get_image_path(filename=new_filename).exists():
        raise FileExistsError

    image.rename(image.parents[0] / new_filename)

def create_image_links(base_link: str):
    images = []

    for file in images_folder_path.iterdir():
        if file.suffix in allowed_extansions:
            path = f"{base_link}/{file.name}"
            images.append(path)

    return {"images": images}


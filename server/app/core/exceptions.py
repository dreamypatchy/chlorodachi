#Token

class InvalidOrExpiredToken(Exception):
    pass

class MalformedTokenPayload(Exception):
    pass

#User

class UserAlreadyExists(Exception):
    pass

class UserCreationError(Exception):
    pass

class UserDoesntExists(Exception):
    pass

#Greenhouse

class GreenhouseCreationError(Exception):
    pass

#Device

class DeviceCreationError(Exception):
    pass

class DeviceNotFound(Exception):
    pass

#Zone

class ZoneNotFound(Exception):
    pass

class ZoneCreationError(Exception):
    pass

#General

class NotDeleted(Exception):
    pass
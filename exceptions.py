class OxfordDictException(Exception):
    def __init__(self, statuscode, msg):
        self.statuscode = statuscode
        self.msg = msg


class BadRequestException(OxfordDictException):
    pass


class WordNotFoundException(OxfordDictException):
    pass


class AuthenticationError(OxfordDictException):
    pass


class ServiceUnavailableError(OxfordDictException):
    pass


class UnsupportedLanguageException(OxfordDictException):
    pass

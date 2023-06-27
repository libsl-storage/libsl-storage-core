package com.example.libslstorage.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN, reason = NO_REFRESH_TOKEN_ERROR_MESSAGE)
class NoRefreshTokenException : RuntimeException(NO_REFRESH_TOKEN_ERROR_MESSAGE)

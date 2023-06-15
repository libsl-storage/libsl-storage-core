package com.example.libslstorage.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST, reason = EMAIL_ALREADY_EXISTS_ERROR_MESSAGE)
class EmailAlreadyExistsException(
    email: String
): RuntimeException("Account with email $email is already exists")

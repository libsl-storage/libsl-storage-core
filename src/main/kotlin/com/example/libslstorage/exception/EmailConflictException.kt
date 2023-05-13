package com.example.libslstorage.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT, reason = "Email already taken")
class EmailConflictException(email: String): RuntimeException("Account with email $email is already exists")

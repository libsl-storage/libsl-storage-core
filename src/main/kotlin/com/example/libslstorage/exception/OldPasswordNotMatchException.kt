package com.example.libslstorage.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.FORBIDDEN, reason = "Old password does not match")
class OldPasswordNotMatchException: RuntimeException("Old password does not match the current account password")

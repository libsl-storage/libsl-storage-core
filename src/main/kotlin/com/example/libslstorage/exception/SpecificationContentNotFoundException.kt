package com.example.libslstorage.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class SpecificationContentNotFoundException(id: Long): RuntimeException("Specification $id has no content")

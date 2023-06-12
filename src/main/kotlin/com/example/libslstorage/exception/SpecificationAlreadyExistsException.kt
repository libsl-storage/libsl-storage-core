package com.example.libslstorage.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class SpecificationAlreadyExistsException(
    name: String,
    path: String
): RuntimeException("Specification with name $name is already exists in directory $path")

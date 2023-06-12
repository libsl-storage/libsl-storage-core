package com.example.libslstorage.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class DirectoryAlreadyExistsException(
    name: String,
    path: String
): RuntimeException("Directory with name $name is already exists in directory $path")

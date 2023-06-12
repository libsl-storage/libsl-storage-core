package com.example.libslstorage.dto.directory

data class CreateDirectoryRequest(
    val name: String,
    val parentId: Long?
)

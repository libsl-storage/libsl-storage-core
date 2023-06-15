package com.example.libslstorage.dto.directory

data class DirectoryResponse(
    val id: Long,
    val parentId: Long?,
    val name: String
)
